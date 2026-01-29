package com.revshop.service;

import com.revshop.dao.ReviewDAO;

public class ReviewService {

    private ReviewDAO reviewDAO = new ReviewDAO();

    public boolean addReview(int productId, int buyerId, int rating, String comment) {
        return reviewDAO.addReview(productId, buyerId, rating, comment);
    }

    public void viewProductReviews(int productId) {
        reviewDAO.viewReviewsByProduct(productId);
        reviewDAO.showAverageRating(productId);
    }

    public void viewSellerProductReviews(int sellerId) {
        reviewDAO.viewReviewsForSeller(sellerId);
    }
}
