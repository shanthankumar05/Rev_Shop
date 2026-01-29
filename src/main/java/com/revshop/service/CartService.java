package com.revshop.service;

import com.revshop.dao.CartDAO;
import com.revshop.model.CartItem;

import java.util.List;

public class CartService {

    private CartDAO cartDAO = new CartDAO();

    public int getCartId(int buyerId) {
        return cartDAO.getOrCreateCart(buyerId);
    }

    public boolean addToCart(int buyerId, int productId, int qty) {
        int cartId = getCartId(buyerId);
        return cartDAO.addToCart(cartId, productId, qty);
    }

    public List<CartItem> viewCart(int buyerId) {
        int cartId = getCartId(buyerId);
        return cartDAO.viewCartItems(cartId);
    }

    public boolean updateQty(int buyerId, int productId, int newQty) {
        int cartId = getCartId(buyerId);
        return cartDAO.updateQuantity(cartId, productId, newQty);
    }

    public boolean removeItem(int buyerId, int productId) {
        int cartId = getCartId(buyerId);
        return cartDAO.removeFromCart(cartId, productId);
    }
}
