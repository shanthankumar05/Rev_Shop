package com.revshop.service;

import com.revshop.dao.CartDAO;
import com.revshop.model.CartItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

public class CartService {

    private static final Logger logger =
            LogManager.getLogger(CartService.class);

    private CartDAO cartDAO = new CartDAO();

    // ✅ Get or create cart
    public int getCartId(int buyerId) {

        logger.debug("Fetching cart for buyerId={}", buyerId);

        try {
            return cartDAO.getOrCreateCart(buyerId);
        } catch (Exception e) {
            logger.error("Failed to get cartId for buyerId={}", buyerId, e);
            return -1;
        }
    }

    // ✅ Add to cart
    public boolean addToCart(int buyerId, int productId, int qty) {

        logger.info("Add to cart buyerId={} productId={} qty={}",
                buyerId, productId, qty);

        try {
            int cartId = getCartId(buyerId);

            if (cartId == -1) {
                logger.warn("Invalid cartId for buyerId={}", buyerId);
                return false;
            }

            boolean result = cartDAO.addToCart(cartId, productId, qty);

            if (!result) {
                logger.warn("Add to cart failed cartId={} productId={}",
                        cartId, productId);
            }

            return result;

        } catch (Exception e) {
            logger.error("Error adding to cart buyerId={}", buyerId, e);
            return false;
        }
    }

    // ✅ View cart
    public List<CartItem> viewCart(int buyerId) {

        logger.debug("Viewing cart buyerId={}", buyerId);

        try {
            int cartId = getCartId(buyerId);

            if (cartId == -1) {
                return Collections.emptyList();
            }

            return cartDAO.viewCartItems(cartId);

        } catch (Exception e) {
            logger.error("Error viewing cart buyerId={}", buyerId, e);
            return Collections.emptyList();
        }
    }

    // ✅ Update quantity
    public boolean updateQty(int buyerId, int productId, int newQty) {

        logger.info("Update cart qty buyerId={} productId={} newQty={}",
                buyerId, productId, newQty);

        try {
            int cartId = getCartId(buyerId);

            if (cartId == -1) {
                return false;
            }

            boolean result =
                    cartDAO.updateQuantity(cartId, productId, newQty);

            if (!result) {
                logger.warn("Update qty failed cartId={} productId={}",
                        cartId, productId);
            }

            return result;

        } catch (Exception e) {
            logger.error("Error updating qty buyerId={}", buyerId, e);
            return false;
        }
    }

    // ✅ Remove item
    public boolean removeItem(int buyerId, int productId) {

        logger.info("Remove from cart buyerId={} productId={}",
                buyerId, productId);

        try {
            int cartId = getCartId(buyerId);

            if (cartId == -1) {
                return false;
            }

            boolean result =
                    cartDAO.removeFromCart(cartId, productId);

            if (!result) {
                logger.warn("Remove from cart failed cartId={} productId={}",
                        cartId, productId);
            }

            return result;

        } catch (Exception e) {
            logger.error("Error removing item buyerId={}", buyerId, e);
            return false;
        }
    }
}
