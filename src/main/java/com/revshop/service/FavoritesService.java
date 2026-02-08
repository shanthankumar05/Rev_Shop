package com.revshop.service;

import com.revshop.dao.FavoritesDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FavoritesService {

    private static final Logger logger =
            LogManager.getLogger(FavoritesService.class);

    private FavoritesDAO favoritesDAO = new FavoritesDAO();

    // ✅ Add to favorites
    public boolean add(int buyerId, int productId) {

        logger.info("Add to favorites buyerId={} productId={}",
                buyerId, productId);

        try {
            boolean result =
                    favoritesDAO.addFavorite(buyerId, productId);

            if (!result) {
                logger.warn("Add to favorites failed buyerId={} productId={}",
                        buyerId, productId);
            }

            return result;

        } catch (Exception e) {
            logger.error("Error adding favorite buyerId={} productId={}",
                    buyerId, productId, e);
            return false;
        }
    }

    // ✅ View favorites
    public void view(int buyerId) {

        logger.info("View favorites buyerId={}", buyerId);

        try {
            favoritesDAO.viewFavorites(buyerId);
        } catch (Exception e) {
            logger.error("Error viewing favorites buyerId={}", buyerId, e);
            System.out.println("❌ Unable to load favorites.");
        }
    }

    // ✅ Remove favorite
    public boolean remove(int buyerId, int productId) {

        logger.info("Remove favorite buyerId={} productId={}",
                buyerId, productId);

        try {
            boolean result =
                    favoritesDAO.removeFavorite(buyerId, productId);

            if (!result) {
                logger.warn("Remove favorite failed buyerId={} productId={}",
                        buyerId, productId);
            }

            return result;

        } catch (Exception e) {
            logger.error("Error removing favorite buyerId={} productId={}",
                    buyerId, productId, e);
            return false;
        }
    }
}
