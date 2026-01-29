package com.revshop.service;

import com.revshop.dao.FavoritesDAO;

public class FavoritesService {

    private FavoritesDAO favoritesDAO = new FavoritesDAO();

    public boolean add(int buyerId, int productId) {
        return favoritesDAO.addFavorite(buyerId, productId);
    }

    public void view(int buyerId) {
        favoritesDAO.viewFavorites(buyerId);
    }

    public boolean remove(int buyerId, int productId) {
        return favoritesDAO.removeFavorite(buyerId, productId);
    }
}
