package com.sld.backend.modules.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sld.backend.config.WechatAuthProperties;
import com.sld.backend.common.enums.UserStatus;
import com.sld.backend.common.enums.UserType;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.common.util.JwtUtil;
import com.sld.backend.common.util.PasswordUtil;
import com.sld.backend.modules.auth.dto.request.LoginRequest;
import com.sld.backend.modules.auth.dto.request.RegisterRequest;
import com.sld.backend.modules.auth.dto.request.WechatLoginRequest;
import com.sld.backend.modules.auth.dto.response.AuthResponse;
import com.sld.backend.modules.auth.dto.response.WechatAuthorizeResponse;
import com.sld.backend.modules.user.mapper.UserMapper;
import com.sld.backend.modules.auth.service.AuthService;
import com.sld.backend.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 认证服务实现
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String WECHAT_STATE_KEY_PREFIX = "auth:wechat:state:";

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final WechatAuthProperties wechatAuthProperties;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public AuthResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        User existUser = userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername())
        );
        if (existUser != null) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }

        // 检查邮箱是否已注册
        existUser = userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getEmail, request.getEmail())
        );
        if (existUser != null) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(PasswordUtil.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        String userTypeStr = request.getUserType() != null ? request.getUserType() : "personal";
        UserType userType = UserType.PERSONAL; // default
        for (UserType type : UserType.values()) {
            if (type.getCode().equalsIgnoreCase(userTypeStr)) {
                userType = type;
                break;
            }
        }
        user.setUserType(userType);
        user.setStatus(UserStatus.ACTIVE);
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.insert(user);

        // 生成 Token
        String token = jwtUtil.generateToken(user.getId(), user.getUserType().getCode());

        return AuthResponse.builder()
            .userId(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .userType(user.getUserType().getCode())
            .token(token)
            .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // 查找用户（支持用户名或邮箱登录）
        User user = userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getAccount())
                .or()
                .eq(User::getEmail, request.getAccount())
        );

        if (user == null) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }

        // 验证密码
        if (!PasswordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }

        // 检查账号状态
        if (user.getStatus() == UserStatus.DISABLED) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        // 生成 Token
        String token = jwtUtil.generateToken(user.getId(), user.getUserType().getCode());

        return AuthResponse.builder()
            .userId(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .userType(user.getUserType().getCode())
            .token(token)
            .build();
    }

    @Override
    public WechatAuthorizeResponse getWechatAuthorizeUrl() {
        ensureWechatEnabled();
        String state = RandomUtil.randomString(24);
        stringRedisTemplate.opsForValue().set(
            WECHAT_STATE_KEY_PREFIX + state,
            "1",
            Duration.ofMinutes(5)
        );
        String redirectUri = URLEncoder.encode(
            wechatAuthProperties.getRedirectUri(),
            StandardCharsets.UTF_8
        );
        String authorizeUrl = String.format(
            "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect",
            wechatAuthProperties.getAppId(),
            redirectUri,
            wechatAuthProperties.getScope(),
            state
        );
        return WechatAuthorizeResponse.builder()
            .authorizeUrl(authorizeUrl)
            .state(state)
            .build();
    }

    @Override
    public AuthResponse wechatLogin(WechatLoginRequest request) {
        ensureWechatEnabled();
        validateState(request.getState());

        JSONObject tokenJson = getWechatAccessToken(request.getCode());
        String openid = tokenJson.getStr("openid");
        String unionid = tokenJson.getStr("unionid");
        String accessToken = tokenJson.getStr("access_token");

        if (StrUtil.isBlank(openid) || StrUtil.isBlank(accessToken)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "微信授权失败，请重试");
        }

        JSONObject userInfo = getWechatUserInfo(accessToken, openid);
        String nickname = userInfo.getStr("nickname");
        String avatar = userInfo.getStr("headimgurl");
        if (StrUtil.isBlank(unionid)) {
            unionid = userInfo.getStr("unionid");
        }

        User user = findByWechat(openid, unionid);
        if (user == null) {
            user = createWechatUser(openid, unionid, nickname, avatar);
        } else {
            user.setWechatOpenid(openid);
            user.setWechatUnionid(unionid);
            if (StrUtil.isNotBlank(nickname)) {
                user.setNickname(nickname);
            }
            if (StrUtil.isNotBlank(avatar)) {
                user.setAvatar(avatar);
            }
            user.setLastLoginTime(LocalDateTime.now());
            userMapper.updateById(user);
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUserType().getCode());
        return AuthResponse.builder()
            .userId(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .userType(user.getUserType().getCode())
            .token(token)
            .build();
    }

    private void ensureWechatEnabled() {
        if (!wechatAuthProperties.isEnabled()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "微信登录未启用");
        }
        if (StrUtil.hasBlank(
            wechatAuthProperties.getAppId(),
            wechatAuthProperties.getAppSecret(),
            wechatAuthProperties.getRedirectUri()
        )) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "微信登录配置不完整");
        }
    }

    private void validateState(String state) {
        String key = WECHAT_STATE_KEY_PREFIX + state;
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "授权已过期，请重新发起微信登录");
        }
        stringRedisTemplate.delete(key);
    }

    private JSONObject getWechatAccessToken(String code) {
        String url = UriComponentsBuilder
            .fromHttpUrl("https://api.weixin.qq.com/sns/oauth2/access_token")
            .queryParam("appid", wechatAuthProperties.getAppId())
            .queryParam("secret", wechatAuthProperties.getAppSecret())
            .queryParam("code", code)
            .queryParam("grant_type", "authorization_code")
            .build()
            .toUriString();
        String response = HttpUtil.get(url);
        JSONObject json = JSONUtil.parseObj(response);
        if (json.containsKey("errcode")) {
            throw new BusinessException(
                ErrorCode.BAD_REQUEST,
                "微信授权失败: " + json.getStr("errmsg")
            );
        }
        return json;
    }

    private JSONObject getWechatUserInfo(String accessToken, String openid) {
        String url = UriComponentsBuilder
            .fromHttpUrl("https://api.weixin.qq.com/sns/userinfo")
            .queryParam("access_token", accessToken)
            .queryParam("openid", openid)
            .queryParam("lang", "zh_CN")
            .build()
            .toUriString();
        String response = HttpUtil.get(url);
        JSONObject json = JSONUtil.parseObj(response);
        if (json.containsKey("errcode")) {
            throw new BusinessException(
                ErrorCode.BAD_REQUEST,
                "微信用户信息拉取失败: " + json.getStr("errmsg")
            );
        }
        return json;
    }

    private User findByWechat(String openid, String unionid) {
        if (StrUtil.isNotBlank(unionid)) {
            User byUnionId = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getWechatUnionid, unionid)
            );
            if (byUnionId != null) {
                return byUnionId;
            }
        }
        return userMapper.selectOne(
            new LambdaQueryWrapper<User>().eq(User::getWechatOpenid, openid)
        );
    }

    private User createWechatUser(
        String openid,
        String unionid,
        String nickname,
        String avatar
    ) {
        String username = "wx_" + RandomUtil.randomString(8);
        String email = String.format("wx_%s@wx.local", RandomUtil.randomString(12));
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(PasswordUtil.encode(IdUtil.fastSimpleUUID()));
        user.setUserType(UserType.PERSONAL);
        user.setStatus(UserStatus.ACTIVE);
        user.setNickname(StrUtil.blankToDefault(nickname, username));
        user.setAvatar(avatar);
        user.setWechatOpenid(openid);
        user.setWechatUnionid(unionid);
        user.setWechatNickname(nickname);
        user.setWechatAvatar(avatar);
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.insert(user);
        return user;
    }
}
