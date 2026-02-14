import { create } from 'zustand';
import type { CartItem } from '@/types';
import { cartService } from '@/services/order';

interface CartState {
  items: CartItem[];
  loading: boolean;
  totalAmount: number;
  totalQuantity: number;
  fetchCart: () => Promise<void>;
  addItem: (productId: number, quantity?: number) => Promise<void>;
  updateItem: (itemId: number, quantity: number) => Promise<void>;
  removeItem: (itemId: number) => Promise<void>;
  clearCart: () => Promise<void>;
  calculateTotals: () => void;
}

export const useCartStore = create<CartState>((set, get) => ({
  items: [],
  loading: false,
  totalAmount: 0,
  totalQuantity: 0,

  fetchCart: async () => {
    set({ loading: true });
    try {
      const response = await cartService.getCart();
      set({ items: response.data });
      get().calculateTotals();
    } catch (error) {
      console.error('Failed to fetch cart:', error);
    } finally {
      set({ loading: false });
    }
  },

  addItem: async (productId, quantity = 1) => {
    set({ loading: true });
    try {
      await cartService.addToCart(productId, quantity);
      await get().fetchCart();
    } catch (error) {
      console.error('Failed to add to cart:', error);
      throw error;
    } finally {
      set({ loading: false });
    }
  },

  updateItem: async (itemId, quantity) => {
    set({ loading: true });
    try {
      await cartService.updateCartItem(itemId, quantity);
      await get().fetchCart();
    } catch (error) {
      console.error('Failed to update cart item:', error);
      throw error;
    } finally {
      set({ loading: false });
    }
  },

  removeItem: async (itemId) => {
    set({ loading: true });
    try {
      await cartService.removeFromCart(itemId);
      await get().fetchCart();
    } catch (error) {
      console.error('Failed to remove cart item:', error);
      throw error;
    } finally {
      set({ loading: false });
    }
  },

  clearCart: async () => {
    set({ loading: true });
    try {
      await cartService.clearCart();
      set({ items: [], totalAmount: 0, totalQuantity: 0 });
    } catch (error) {
      console.error('Failed to clear cart:', error);
      throw error;
    } finally {
      set({ loading: false });
    }
  },

  calculateTotals: () => {
    const { items } = get();
    const totalAmount = items.reduce((sum, item) => sum + item.price * item.quantity, 0);
    const totalQuantity = items.reduce((sum, item) => sum + item.quantity, 0);
    set({ totalAmount, totalQuantity });
  },
}));
