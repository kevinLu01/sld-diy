// 用户相关类型
export interface User {
  id: number;
  username: string;
  email: string;
  phone?: string;
  avatar?: string;
  userType: 'personal' | 'business';
  status: 'active' | 'suspended' | 'deleted';
  createdAt: string;
}

export interface BusinessInfo {
  id: number;
  userId: number;
  companyName: string;
  businessLicense?: string;
  industry?: string;
  address?: string;
  contactPerson?: string;
  verified: boolean;
  verifiedAt?: string;
}

// 产品相关类型
export interface Category {
  id: number;
  name: string;
  slug: string;
  icon?: string;
  parentId?: number;
  children?: Category[];
  productCount?: number;
}

export interface Brand {
  id: number;
  name: string;
  slug: string;
  logo?: string;
  description?: string;
  country?: string;
}

export interface ProductSpec {
  id: number;
  specKey: string;
  specValue: string;
  unit?: string;
}

export interface Product {
  id: number;
  sku: string;
  name: string;
  brand?: Brand;
  brandId?: number;
  category?: Category;
  categoryId: number;
  description?: string;
  price: number;
  originalPrice?: number;
  stockQuantity: number;
  unit: string;
  images: string[];
  videoUrl?: string;
  model3dUrl?: string;
  status: 'active' | 'inactive';
  viewCount: number;
  salesCount: number;
  rating?: number;
  specifications?: ProductSpec[];
  createdAt: string;
  updatedAt: string;
}

// 购物车相关
export interface CartItem {
  id: number;
  productId: number;
  product: Product;
  quantity: number;
  price: number;
}

// 订单相关
export interface ShippingAddress {
  recipient: string;
  phone: string;
  province: string;
  city: string;
  district: string;
  address: string;
  postalCode?: string;
}

export interface OrderItem {
  id: number;
  productId: number;
  product?: Product;
  productName: string;
  productSku: string;
  price: number;
  quantity: number;
  subtotal: number;
}

export interface Order {
  id: number;
  orderNo: string;
  userId: number;
  totalAmount: number;
  discountAmount: number;
  shippingFee: number;
  finalAmount: number;
  status: 'pending' | 'paid' | 'processing' | 'shipped' | 'completed' | 'cancelled';
  paymentMethod?: string;
  paymentTime?: string;
  shippingAddress: ShippingAddress;
  notes?: string;
  items?: OrderItem[];
  createdAt: string;
  updatedAt: string;
}

// DIY相关类型
export interface DiyRequirements {
  scenario: string;
  temperatureRange?: string;
  coolingCapacity?: number;
  capacityUnit?: string;
  volume?: number;
  volumeUnit?: string;
  ambientTemp?: string;
}

export interface DiyProjectItem {
  productId: number;
  product?: Product;
  quantity: number;
  notes?: string;
}

export interface DiyProject {
  id: number;
  userId: number;
  projectName: string;
  scenario?: string;
  customScenarioName?: string;
  temperatureRange?: string;
  coolingCapacity?: number;
  capacityUnit?: string;
  volume?: number;
  volumeUnit?: string;
  ambientTemp?: string;
  selectedProducts: DiyProjectItem[];
  totalPrice: number;
  quotedTotalPrice?: number;
  discountRate?: number;
  discountAmount?: number;
  privateNote?: string;
  shareMode?: 'public' | 'private_offer';
  shareExpiresAt?: string;
  status: 'draft' | 'saved' | 'ordered';
  shared: boolean;
  shareToken?: string;
  createdAt: string;
  updatedAt: string;
}

export interface DiyRecommendation {
  recommendationId: string;
  scenario: string;
  requirements: DiyRequirements;
  products: {
    [key: string]: Product[];
  };
  totalPrice?: number;
  estimatedInstallationFee?: number;
  energyEfficiency?: string;
  estimatedPowerConsumption?: string;
  suggestions?: string[];
  explanations?: Array<{
    productType: string;
    score: number;
    reason: string;
    alternatives?: string[];
  }>;
}

// 解决方案相关
export interface SolutionProduct {
  productId: number;
  product?: Product;
  quantity: number;
  notes?: string;
}

export interface Solution {
  id: number;
  title: string;
  industry?: string;
  scenario?: string;
  description?: string;
  coverImage?: string;
  images?: string[];
  temperatureRange?: string;
  capacityRange?: string;
  features?: string[];
  products?: SolutionProduct[];
  totalPrice: number;
  viewCount: number;
  usageCount: number;
  status: 'draft' | 'published';
  createdAt: string;
  updatedAt: string;
}

// 知识库
export interface KnowledgeArticle {
  id: number;
  title: string;
  category?: string;
  content: string;
  tags?: string[];
  attachments?: string[];
  viewCount: number;
  helpfulCount: number;
  status: 'draft' | 'published';
  createdAt: string;
  updatedAt: string;
}

// API响应类型
export interface ApiResponse<T = any> {
  code: number;
  message?: string;
  data: T;
}

export interface PaginatedResponse<T> {
  total: number;
  page: number;
  limit: number;
  items: T[];
}

// 兼容性类型
export interface CompatibilityCheck {
  compatible: boolean;
  warnings: Array<{
    type: string;
    message: string;
  }>;
  errors: Array<{
    type: string;
    message: string;
  }>;
  compatibilityMatrix: Array<{
    productA: number;
    productB: number;
    status: 'compatible' | 'recommended' | 'incompatible' | 'unknown';
    note?: string;
  }>;
}
