package com.revshop.service;

import com.revshop.dao.ReviewDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReviewService {

    private static final Logger logger =
            LogManager.getLogger(ReviewService.class);

    private ReviewDAO reviewDAO = new ReviewDAO();

    // ✅ Add review
    public boolean addReview(int productId,
                             int buyerId,
                             int rating,
                             String comment) {

        logger.info("Add review productId={} buyerId={} rating={}",
                productId, buyerId, rating);

        try {
            boolean result =
                    reviewDAO.addReview(productId, buyerId, rating, comment);

            if (!result) {
                logger.warn("Add review failed productId={} buyerId={}",
                        productId, buyerId);
            }

            return result;

        } catch (Exception e) {
            logger.error("Error adding review productId={} buyerId={}",
                    productId, buyerId, e);
            return false;
        }
    }

    // ✅ View product reviews + average
    public void viewProductReviews(int productId) {

        logger.info("Viewing product reviews productId={}", productId);

        try {
            reviewDAO.viewReviewsByProduct(productId);
            reviewDAO.showAverageRating(productId);
        } catch (Exception e) {
            logger.error("Error viewing product reviews productId={}",
                    productId, e);
            System.out.println("❌ Unable to load product reviews.");
        }
    }

    // ✅ View seller product reviews
    public void viewSellerProductReviews(int sellerId) {

        logger.info("Viewing seller reviews sellerId={}", sellerId);

        try {
            reviewDAO.viewReviewsForSeller(sellerId);
        } catch (Exception e) {
            logger.error("Error viewing seller reviews sellerId={}",
                    sellerId, e);
            System.out.println("❌ Unable to load seller reviews.");
        }
    }

    // ✅ Get average rating
    public double getAverageRating(int productId) {

        logger.debug("Fetching average rating productId={}", productId);

        try {
            return reviewDAO.getAverageRating(productId);
        } catch (Exception e) {
            logger.error("Error fetching average rating productId={}",
                    productId, e);
            return 0.0;
        }
    }

    // ✅ Get review count
    public int getReviewCount(int productId) {

        logger.debug("Fetching review count productId={}", productId);

        try {
            return reviewDAO.getReviewCount(productId);
        } catch (Exception e) {
            logger.error("Error fetching review count productId={}",
                    productId, e);
            return 0;
        }
    }
}
