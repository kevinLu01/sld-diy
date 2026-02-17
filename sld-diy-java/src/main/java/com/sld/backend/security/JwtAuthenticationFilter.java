package com.sld.backend.security;

import com.sld.backend.common.constant.CommonConstant;
import com.sld.backend.common.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * JWT 认证过滤器
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(CommonConstant.TOKEN_HEADER);
        String username = null;
        String token = null;
        String userType = null;

        if (authHeader != null && authHeader.startsWith(CommonConstant.TOKEN_PREFIX)) {
            token = authHeader.substring(7);
            try {
                if (jwtUtil.validateToken(token)) {
                    Long userId = jwtUtil.getUserIdFromToken(token);
                    username = userId.toString();
                    userType = jwtUtil.getUserTypeFromToken(token);
                }
            } catch (Exception e) {
                logger.error("JWT Token validation failed", e);
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 根据 userType 设置对应的 Spring Security 角色
            List<SimpleGrantedAuthority> authorities;
            if ("admin".equalsIgnoreCase(userType)) {
                authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
                );
            } else {
                authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            }

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
