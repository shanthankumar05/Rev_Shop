package com.revshop.main;

import com.revshop.model.Product;
import com.revshop.model.User;
import com.revshop.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class MainApp {

    private static final Logger logger = LogManager.getLogger(MainApp.class);

    // ================= SAFE INTEGER INPUT =================
    private static int safeInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("❌ Please enter a valid number: ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine();
        return val;
    }

    public static void main(String[] args) {

        logger.info("RevShop application started");

        try (Scanner sc = new Scanner(System.in)) {

            AuthService authService = new AuthService();

            while (true) {
                try {
                    System.out.println("\n===== REVSHOP =====");
                    System.out.println("1. Register Buyer");
                    System.out.println("2. Register Seller");
                    System.out.println("3. Login");
                    System.out.println("4. Forgot Password");
                    System.out.println("5. Exit");
                    System.out.print("Choose: ");

                    int choice = safeInt(sc);

                    switch (choice) {
                        case 1 -> registerBuyer(sc, authService);
                        case 2 -> registerSeller(sc, authService);
                        case 3 -> login(sc, authService);
                        case 4 -> forgotPassword(sc, authService);
                        case 5 -> {
                            logger.info("Application exited by user");
                            System.out.println("✅ Thank you for using RevShop!");
                            return;
                        }
                        default -> System.out.println("❌ Invalid choice!");
                    }

                } catch (Exception e) {
                    logger.error("Error in main menu", e);
                    System.out.println("❌ Invalid input. Try again.");
                }
            }

        } catch (Exception e) {
            logger.fatal("Application crashed", e);
        }
    }

    // ================= AUTH =================

    private static void registerBuyer(Scanner sc, AuthService authService) {

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        String secQ = getSecurityQuestion(sc);

        System.out.print("Security Answer: ");
        String secA = sc.nextLine();

        System.out.println(
                authService.registerBuyer(name, email, pass, secQ, secA)
                        ? "✅ Buyer Registered!"
                        : "❌ Buyer Register Failed!"
        );
    }

    private static void registerSeller(Scanner sc, AuthService authService) {

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        System.out.print("Business Name: ");
        String business = sc.nextLine();

        System.out.print("Phone: ");
        String phone = sc.nextLine();

        String secQ = getSecurityQuestion(sc);

        System.out.print("Security Answer: ");
        String secA = sc.nextLine();

        System.out.println(
                authService.registerSeller(name, email, pass, business, phone, secQ, secA)
                        ? "✅ Seller Registered!"
                        : "❌ Seller Register Failed!"
        );
    }

    private static void login(Scanner sc, AuthService authService) {

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        User user = authService.login(email, pass);

        if (user != null) {
            System.out.println("\n✅ Login Success! Welcome " + user.getName());
            System.out.println("Role: " + user.getRole());
            loggedInMenu(sc, authService, user);
        } else {
            System.out.println("❌ Invalid Credentials!");
        }
    }

    private static void forgotPassword(Scanner sc, AuthService authService) {

        System.out.print("Enter your registered Email: ");
        String email = sc.nextLine();

        String question = authService.getSecurityQuestion(email);
        if (question == null) {
            System.out.println("❌ Email not found!");
            return;
        }

        System.out.println("Security Question: " + question);
        System.out.print("Enter Security Answer: ");
        String secA = sc.nextLine();

        System.out.print("Enter New Password: ");
        String newPass = sc.nextLine();

        System.out.println(
                authService.forgotPasswordReset(email, secA, newPass)
                        ? "✅ Password Reset Successful!"
                        : "❌ Wrong Answer / Reset Failed!"
        );
    }

    // ================= ROLE =================

    private static void loggedInMenu(Scanner sc, AuthService authService, User user) {

        if ("BUYER".equalsIgnoreCase(user.getRole())) {
            buyerMenu(sc, authService, user);
        } else if ("SELLER".equalsIgnoreCase(user.getRole())) {
            sellerMenu(sc, authService, user);
        } else {
            System.out.println("❌ Unknown role!");
        }
    }

    // ================= BUYER MENU =================

    private static void buyerMenu(Scanner sc, AuthService authService, User user) {

        ProductService productService = new ProductService();
        CartService cartService = new CartService();
        OrderService orderService = new OrderService();
        NotificationService notificationService = new NotificationService();
        ReviewService reviewService = new ReviewService();
        FavoritesService favoritesService = new FavoritesService();

        while (true) {
            try {
                System.out.println("\n===== BUYER MENU =====");
                System.out.println("1. View All Products");
                System.out.println("2. Search Product");
                System.out.println("3. Browse by Category");
                System.out.println("4. Add Product to Cart");
                System.out.println("5. View Cart");
                System.out.println("6. Update Cart Quantity");
                System.out.println("7. Remove Item from Cart");
                System.out.println("8. Checkout");
                System.out.println("9. View Order History");
                System.out.println("10. View Notifications");
                System.out.println("11. Add Review & Rating");
                System.out.println("12. View Product Reviews");
                System.out.println("13. Change Password");
                System.out.println("14. Logout");
                System.out.println("15. Add to Favorites");
                System.out.println("16. View Favorites");
                System.out.println("17. Remove from Favorites");
                System.out.print("Choose: ");

                int ch = safeInt(sc);

                switch (ch) {

                    case 1 -> {
                        var products = productService.viewAllProducts();
                        if (products.isEmpty()) {
                            System.out.println("⚠ No products available.");
                        } else {
                            for (Product p : products) {
                                double avg = reviewService.getAverageRating(p.getProductId());
                                int cnt = reviewService.getReviewCount(p.getProductId());
                                System.out.println(
                                        "ID: " + p.getProductId() +
                                                " | Name: " + p.getProductName() +
                                                " | Description: " + p.getDescription() +
                                                " | MRP: " + p.getMrp() +
                                                " | *Discount Price*: " + p.getDiscountedPrice() +
                                                " | Stock: " + p.getStock() +
                                                " | ⭐ " + String.format("%.1f", avg) +
                                                " (" + cnt + " reviews)"
                                );
                            }
                        }
                    }

                    case 2 -> {
                        System.out.print("Enter keyword: ");
                        String keyword = sc.nextLine();
                        var products = productService.searchProducts(keyword);
                        if (products.isEmpty()) {
                            System.out.println("⚠ No products found.");
                        } else {
                            products.forEach(p ->
                                    System.out.println(
                                            "ID: " + p.getProductId() +
                                                    " | Name: " + p.getProductName() +
                                                    " | Description: " + p.getDescription() +
                                                    " | MRP: " + p.getMrp() +
                                                    " | *Discount Price*: " + p.getDiscountedPrice() +
                                                    " | Stock: " + p.getStock()
                                    ));
                        }
                    }

                    case 3 -> {
                        System.out.println("Categories:");
                        System.out.println("1. Mobiles");
                        System.out.println("2. Laptops");
                        System.out.println("3. Fashion");
                        System.out.println("4. Home Appliances");
                        System.out.print("Enter Category ID: ");

                        int catId = safeInt(sc);
                        var products = productService.browseByCategory(catId);

                        if (products.isEmpty()) {
                            System.out.println("⚠ No products in this category.");
                        } else {
                            products.forEach(p ->
                                    System.out.println(
                                            "ID: " + p.getProductId() +
                                                    " | Name: " + p.getProductName() +
                                                    " | Description: " + p.getDescription() +
                                                    " | MRP: " + p.getMrp() +
                                                    " | *Discount Price*: " + p.getDiscountedPrice() +
                                                    " | Stock: " + p.getStock()
                                    ));
                        }
                    }

                    case 4 -> {
                        System.out.print("Product ID: ");
                        int pid = safeInt(sc);
                        System.out.print("Qty: ");
                        int qty = safeInt(sc);

                        System.out.println(
                                cartService.addToCart(user.getUserId(), pid, qty)
                                        ? "✅ Added to cart!"
                                        : "❌ Failed!"
                        );
                    }

                    case 5 -> {
                        var items = cartService.viewCart(user.getUserId());

                        if (items.isEmpty()) {
                            System.out.println("🛒 Your cart is empty.");
                        } else {
                            System.out.println("\n===== YOUR CART =====");
                            double total = 0;

                            for (var i : items) {
                                double subTotal = i.getPrice() * i.getQuantity();
                                total += subTotal;

                                System.out.println(
                                        "Product: " + i.getProductName() +
                                                " | Qty: " + i.getQuantity() +
                                                " | Price: " + i.getPrice() +
                                                " | Subtotal: " + subTotal
                                );
                            }

                            System.out.println("✅ Total Amount: " + total);
                        }
                    }


                    case 6 -> {
                        System.out.print("Product ID: ");
                        int pid = safeInt(sc);
                        System.out.print("New Qty: ");
                        int q = safeInt(sc);

                        System.out.println(
                                cartService.updateQty(user.getUserId(), pid, q)
                                        ? "✅ Updated!"
                                        : "❌ Failed!"
                        );
                    }

                    case 7 -> {
                        System.out.print("Product ID: ");
                        int pid = safeInt(sc);

                        System.out.println(
                                cartService.removeItem(user.getUserId(), pid)
                                        ? "✅ Removed!"
                                        : "❌ Failed!"
                        );
                    }

                    case 8 -> {
                        System.out.print("Shipping Address: ");
                        String ship = sc.nextLine();

                        System.out.print("Billing Address: ");
                        String bill = sc.nextLine();

                        System.out.println("Select Payment Method:");
                        System.out.println("1. Cash on Delivery (COD)");
                        System.out.println("2. UPI");
                        System.out.print("Choose: ");

                        int payChoice = safeInt(sc);
                        String paymentMethod;

                        if (payChoice == 2) {
                            System.out.print("Enter UPI ID (example: name@upi): ");
                            String upi = sc.nextLine();
                            paymentMethod = "UPI (" + upi + ")";
                        } else {
                            paymentMethod = "COD";
                        }

                        System.out.println(
                                orderService.checkout(user.getUserId(), ship, bill, paymentMethod)
                                        ? "✅ Checkout Done!"
                                        : "❌ Checkout Failed!"
                        );
                    }


                    case 9 -> {
                        logger.info("Buyer {} viewing order history", user.getUserId());

//                        System.out.println("\n===== YOUR ORDER HISTORY =====");
                        orderService.showBuyerOrderHistory(user.getUserId());
                        System.out.println("===== END OF ORDER HISTORY =====");
                    }

                    case 10 -> {
                        notificationService.viewNotifications(user.getUserId());

                        System.out.print("Enter Notification ID to mark as read (0 to skip): ");
                        int nid = safeInt(sc);

                        if (nid != 0) {
                            boolean ok = notificationService.markRead(nid, user.getUserId());
                            System.out.println(ok
                                    ? "✅ Notification marked as read!"
                                    : "❌ Invalid Notification ID!");
                        }
                    }


                    case 11 -> {
                        System.out.print("Product ID: ");
                        int pid = safeInt(sc);
                        System.out.print("Rating (1-5): ");
                        int rating = safeInt(sc);
                        System.out.print("Comment: ");
                        String c = sc.nextLine();

                        System.out.println(
                                reviewService.addReview(pid, user.getUserId(), rating, c)
                                        ? "✅ Review Added!"
                                        : "❌ Failed!"
                        );
                    }

                    case 12 -> {
                        System.out.print("Product ID: ");
                        int pid = safeInt(sc);
                        reviewService.viewProductReviews(pid);
                    }

                    case 13 -> {
                        System.out.print("Old Password: ");
                        String o = sc.nextLine();
                        System.out.print("New Password: ");
                        String n = sc.nextLine();

                        System.out.println(
                                authService.changePassword(user.getUserId(), o, n)
                                        ? "✅ Password Changed!"
                                        : "❌ Failed!"
                        );
                    }

                    case 14 -> {
                        logger.info("Buyer logout userId={}", user.getUserId());
                        System.out.println("✅ Logged out successfully!");
                        return;
                    }

                    case 15 -> {
                        System.out.print("Product ID: ");
                        int pid = safeInt(sc);
                        boolean ok = favoritesService.add(user.getUserId(), pid);
                        System.out.println(ok
                                ? "✅ Added to Favorites!"
                                : "❌ Already in Favorites / Failed!");
                    }

                    case 16 -> {
                        logger.info("Buyer {} viewing favorites", user.getUserId());

//                        System.out.println("\n===== YOUR FAVORITES =====");
                        favoritesService.view(user.getUserId());
                        System.out.println("===== END OF FAVORITES =====");
                    }


                    case 17 -> {
                        System.out.print("Product ID: ");
                        int pid = safeInt(sc);
                        boolean ok = favoritesService.remove(user.getUserId(), pid);
                        System.out.println(ok
                                ? "✅ Removed from Favorites!"
                                : "❌ Not found in Favorites!");

                    }

                    default -> System.out.println("❌ Invalid choice!");
                }

            } catch (Exception e) {
                logger.error("Buyer menu error", e);
                System.out.println("❌ Invalid input!");
            }
        }
    }

    // ================= SELLER MENU =================

    private static void sellerMenu(Scanner sc, AuthService authService, User user) {

        ProductService productService = new ProductService();
        OrderService orderService = new OrderService();
        NotificationService notificationService = new NotificationService();
        ReviewService reviewService = new ReviewService();

        while (true) {
            try {
                System.out.println("\n===== SELLER MENU =====");
                System.out.println("1. Add Product");
                System.out.println("2. View My Products");
                System.out.println("3. Update Stock");
                System.out.println("4. Update Price");
                System.out.println("5. Delete Product");
                System.out.println("6. View My Orders");
                System.out.println("7. View Notifications");
                System.out.println("8. View Reviews");
                System.out.println("9. Change Password");
                System.out.println("10. Logout");
                System.out.print("Choose: ");

                int ch = safeInt(sc);

                switch (ch) {

                    case 1 -> {
                        Product p = new Product();
                        p.setSellerId(user.getUserId());

                        System.out.println("Categories:");
                        System.out.println("1. Mobiles");
                        System.out.println("2. Laptops");
                        System.out.println("3. Fashion");
                        System.out.println("4. Home Appliances");
                        System.out.print("Category ID: ");
                        p.setCategoryId(safeInt(sc));

                        System.out.print("Product Name: ");
                        p.setProductName(sc.nextLine());

                        System.out.print("Description: ");
                        p.setDescription(sc.nextLine());

                        System.out.print("MRP: ");
                        p.setMrp(sc.nextDouble());
                        System.out.print("Discounted Price: ");
                        p.setDiscountedPrice(sc.nextDouble());
                        System.out.print("Stock: ");
                        p.setStock(sc.nextInt());
                        System.out.print("Threshold Stock: ");
                        p.setThresholdStock(sc.nextInt());
                        sc.nextLine();

                        System.out.println(
                                productService.addProduct(p)
                                        ? "✅ Product Added!"
                                        : "❌ Failed!"
                        );
                    }

                    case 2 -> {
                        var products = productService.viewSellerProducts(user.getUserId());
                        if (products.isEmpty()) {
                            System.out.println("⚠ You have not added any products yet.");
                        } else {
                            products.forEach(p -> {
                                String alert = (p.getStock() <= p.getThresholdStock())
                                        ? " ⚠ LOW STOCK"
                                        : "";

                                System.out.println(
                                        "ID: " + p.getProductId() +
                                                " | Name: " + p.getProductName() +
                                                " | MRP: " + p.getMrp() +
                                                " | *Discount Price*: " + p.getDiscountedPrice() +
                                                " | Stock: " + p.getStock() +
                                                " | Threshold: " + p.getThresholdStock() +
                                                alert
                                );
                            });
                        }
                    }


                    case 3 -> {
                        System.out.print("Product ID: ");
                        int pid = safeInt(sc);
                        System.out.print("New Stock: ");
                        int stock = safeInt(sc);

                        System.out.println(
                                productService.updateStock(pid, user.getUserId(), stock)
                                        ? "✅ Stock Updated!"
                                        : "❌ Failed!"
                        );
                    }

                    case 4 -> {
                        System.out.print("Product ID: ");
                        int pid = safeInt(sc);
                        System.out.print("New MRP: ");
                        double mrp = sc.nextDouble();
                        System.out.print("New Discounted Price: ");
                        double dp = sc.nextDouble();
                        sc.nextLine();

                        System.out.println(
                                productService.updatePrice(pid, user.getUserId(), mrp, dp)
                                        ? "✅ Price Updated!"
                                        : "❌ Failed!"
                        );
                    }

                    case 5 -> {
                        System.out.print("Product ID: ");
                        int pid = safeInt(sc);

                        System.out.println(
                                productService.deleteProduct(pid, user.getUserId())
                                        ? "✅ Product Deleted!"
                                        : "❌ Failed!"
                        );
                    }

                    case 6 -> {
                        logger.info("Seller {} viewing orders", user.getUserId());

                        System.out.println("\n===== YOUR ORDERS =====");
                        orderService.showSellerOrders(user.getUserId());
                        System.out.println("===== END OF ORDERS =====");
                    }


                    case 7 -> {
                        notificationService.viewNotifications(user.getUserId());

                        System.out.print("Enter Notification ID to mark as read (0 to skip): ");
                        int nid = safeInt(sc);

                        if (nid != 0) {
                            boolean ok = notificationService.markRead(nid, user.getUserId());
                            System.out.println(ok
                                    ? "✅ Notification marked as read!"
                                    : "❌ Invalid Notification ID!");
                        }
                    }

                    case 8 -> {
                        logger.info("Seller {} viewing product reviews", user.getUserId());

//                        System.out.println("\n===== REVIEWS FOR YOUR PRODUCTS =====");
                        reviewService.viewSellerProductReviews(user.getUserId());
                        System.out.println("===== END OF REVIEWS =====");
                    }


                    case 9 -> {
                        System.out.print("Old Password: ");
                        String oldP = sc.nextLine();
                        System.out.print("New Password: ");
                        String newP = sc.nextLine();

                        System.out.println(
                                authService.changePassword(user.getUserId(), oldP, newP)
                                        ? "✅ Password Changed!"
                                        : "❌ Failed!"
                        );
                    }

                    case 10 -> {
                        logger.info("Seller logged out successfully userId={}", user.getUserId());
                        System.out.println("✅ Logged out successfully!");
                        return;
                    }


                    default -> System.out.println("❌ Invalid choice!");
                }

            } catch (Exception e) {
                logger.error("Seller menu error", e);
                System.out.println("❌ Invalid input!");
            }
        }
    }

    // ================= SECURITY QUESTIONS =================

    private static String getSecurityQuestion(Scanner sc) {

        while (true) {
            System.out.println("\nChoose Security Question:");
            System.out.println("1. What is your pet name?");
            System.out.println("2. What is your first school name?");
            System.out.println("3. What is your favourite food?");
            System.out.println("4. What is your birthplace?");
            System.out.println("5. What is your best friend name?");
            System.out.print("Enter choice (1-5): ");

            // ✅ Validate integer input
            if (!sc.hasNextInt()) {
                System.out.println("❌ Invalid input! Please enter a number (1-5).");
                sc.nextLine(); // clear invalid input
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1: return "What is your pet name?";
                case 2: return "What is your first school name?";
                case 3: return "What is your favourite food?";
                case 4: return "What is your birthplace?";
                case 5: return "What is your best friend name?";
                default:
                    System.out.println("❌ Invalid choice! Please select between 1 and 5.");
            }
        }
    }
}
