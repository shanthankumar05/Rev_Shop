package com.revshop.main;

import com.revshop.model.Product;
import com.revshop.model.User;
import com.revshop.service.AuthService;
import com.revshop.service.ProductService;
import com.revshop.service.CartService;
import com.revshop.service.NotificationService;
import com.revshop.service.OrderService;
import java.util.Scanner;
import com.revshop.service.ReviewService;
import com.revshop.service.FavoritesService;
import com.revshop.dao.ReviewDAO;
public class MainApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AuthService authService = new AuthService();

        while (true) {

            System.out.println("\n===== REVSHOP =====");
            System.out.println("1. Register Buyer");
            System.out.println("2. Register Seller");
            System.out.println("3. Login");
            System.out.println("4. Forgot Password");
            System.out.println("5. Exit");
            System.out.print("Choose: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1 -> {
                    System.out.print("Name: ");
                    String name = sc.nextLine();

                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    System.out.print("Password: ");
                    String pass = sc.nextLine();

                    String secQ = getSecurityQuestion(sc);

                    System.out.print("Security Answer: ");
                    String secA = sc.nextLine();

                    boolean ok = authService.registerBuyer(name, email, pass, secQ, secA);
                    System.out.println(ok ? "✅ Buyer Registered!" : "❌ Buyer Register Failed!");
                }

                case 2 -> {
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

                    boolean ok = authService.registerSeller(name, email, pass, business, phone, secQ, secA);
                    System.out.println(ok ? "✅ Seller Registered!" : "❌ Seller Register Failed!");
                }

                case 3 -> {
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

                case 4 -> {
                    System.out.print("Enter your registered Email: ");
                    String email = sc.nextLine();

                    String question = authService.getSecurityQuestion(email);

                    if (question == null) {
                        System.out.println("❌ Email not found!");
                        break;
                    }

                    System.out.println("Security Question: " + question);
                    System.out.print("Enter Security Answer: ");
                    String secA = sc.nextLine();

                    System.out.print("Enter New Password: ");
                    String newPass = sc.nextLine();

                    boolean ok = authService.forgotPasswordReset(email, secA, newPass);
                    System.out.println(ok ? "✅ Password Reset Successful!" : "❌ Wrong Answer / Reset Failed!");
                }

                case 5 -> {
                    System.out.println("✅ Thank you for using RevShop!");
                    sc.close();
                    return;
                }

                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }

    // ✅ Role based redirection
    private static void loggedInMenu(Scanner sc, AuthService authService, User user) {

        if (user.getRole().equalsIgnoreCase("BUYER")) {
            buyerMenu(sc, authService, user);
        } else if (user.getRole().equalsIgnoreCase("SELLER")) {
            sellerMenu(sc, authService, user);
        } else {
            System.out.println("❌ Unknown role!");
        }
    }

    // ✅ Buyer Menu
    private static void buyerMenu(Scanner sc, AuthService authService, User user) {

        ProductService productService = new ProductService();
        CartService cartService = new CartService();
        OrderService orderService = new OrderService();
        NotificationService notificationService = new NotificationService();
        ReviewService reviewService = new ReviewService();
        FavoritesService favoritesService = new FavoritesService();

        while (true) {
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

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {

                case 1 -> {
                    var products = productService.viewAllProducts();

                    if (products.isEmpty()) {
                        System.out.println("⚠ No products available.");
                    } else {
                        System.out.println("\n--- Products ---");

                        for (Product p : products) {

                            // ✅ get rating + review count for each product
                            double avg = reviewService.getAverageRating(p.getProductId());
                            int cnt = reviewService.getReviewCount(p.getProductId());
                            System.out.println("MRP: " + p.getMrp() + " | Price: " + p.getDiscountedPrice());

                            System.out.println("ID: " + p.getProductId()
                                    + " | Name: " + p.getProductName()
//                                    + " | Price: " + p.getDiscountedPrice()

                                    +" | MRP: " + p.getMrp() + " | Discount Price: " + p.getDiscountedPrice()
                                    + " | Stock: " + p.getStock()
                                    + " | ⭐ " + String.format("%.1f", avg) + " (" + cnt + " reviews)");
                        }
                    }
                }
                case 2 -> {
                    System.out.print("Enter keyword: ");
                    String keyword = sc.nextLine();

                    var products = productService.searchProducts(keyword);

                    if (products.isEmpty()) {
                        System.out.println("⚠ No matching products found.");
                    } else {
                        System.out.println("\n--- Search Results ---");
                        for (var p : products) {
                            System.out.println(
                                    "ID: " + p.getProductId() +
                                            " | Name: " + p.getProductName() +
                                            " | Price: " + p.getDiscountedPrice()
                            );
                        }
                    }
                }

                case 3 -> {
                    System.out.println("\nCategories:");
                    System.out.println("1 -> Mobiles");
                    System.out.println("2 -> Laptops");
                    System.out.println("3 -> Fashion");
                    System.out.println("4 -> Home Appliances");

                    System.out.print("Enter Category ID: ");
                    int catId = sc.nextInt();
                    sc.nextLine();

                    var products = productService.browseByCategory(catId);

                    if (products.isEmpty()) {
                        System.out.println("⚠ No products in this category.");
                    } else {
                        System.out.println("\n--- Category Products ---");
                        for (var p : products) {
                            System.out.println(
                                    "ID: " + p.getProductId() +
                                            " | Name: " + p.getProductName() +
                                            " | Price: " + p.getDiscountedPrice()
                            );
                        }
                    }
                }

                case 4 -> {
                    System.out.print("Enter Product ID to Add: ");
                    int productId = sc.nextInt();

                    System.out.print("Enter Quantity: ");
                    int qty = sc.nextInt();
                    sc.nextLine();

                    boolean ok = cartService.addToCart(user.getUserId(), productId, qty);
                    System.out.println(ok ? "✅ Added to cart!" : "❌ Failed to add to cart!");
                }

                case 5 -> {
                    var items = cartService.viewCart(user.getUserId());

                    if (items.isEmpty()) {
                        System.out.println("🛒 Cart is empty!");
                    } else {
                        System.out.println("\n--- Your Cart ---");
                        double total = 0;

                        for (var i : items) {
                            double subTotal = i.getPrice() * i.getQuantity();
                            total += subTotal;

                            System.out.println(
                                    "ProductID: " + i.getProductId() +
                                            " | Name: " + i.getProductName() +
                                            " | Price: " + i.getPrice() +
                                            " | Qty: " + i.getQuantity() +
                                            " | SubTotal: " + subTotal
                            );
                        }

                        System.out.println("✅ Total Amount: " + total);
                    }
                }

                case 6 -> {
                    System.out.print("Enter Product ID to Update Qty: ");
                    int productId = sc.nextInt();

                    System.out.print("Enter New Quantity: ");
                    int newQty = sc.nextInt();
                    sc.nextLine();

                    boolean ok = cartService.updateQty(user.getUserId(), productId, newQty);
                    System.out.println(ok ? "✅ Quantity Updated!" : "❌ Failed to update quantity!");
                }

                case 7 -> {
                    System.out.print("Enter Product ID to Remove: ");
                    int productId = sc.nextInt();
                    sc.nextLine();

                    boolean ok = cartService.removeItem(user.getUserId(), productId);
                    System.out.println(ok ? "✅ Removed from cart!" : "❌ Failed to remove item!");
                }

                // ✅ Checkout
                case 8 -> {
                    System.out.print("Shipping Address: ");
                    String ship = sc.nextLine();

                    System.out.print("Billing Address: ");
                    String bill = sc.nextLine();
                    System.out.println("\nSelect Payment Method:");
                    System.out.println("1. COD");
                    System.out.println("2. UPI");
                    System.out.println("3. CARD");
                    System.out.print("Choose: ");
                    int payChoice = sc.nextInt();
//                    sc.nextLine();

                    String paymentMethod = "";

                    if (payChoice == 1) {
                        paymentMethod = "COD";
                    } else if (payChoice == 2) {
                        System.out.print("Enter UPI ID (example: name@upi): ");
                        String upiId = sc.nextLine();
                        paymentMethod = "UPI (" + upiId + ")";
                    } else if (payChoice == 3) {
                        System.out.print("Enter Card Last 4 Digits: ");
                        String last4 = sc.nextLine();
                        paymentMethod = "CARD (****" + last4 + ")";
                    } else {
                        System.out.println("❌ Invalid payment option! Defaulting to COD");
                        paymentMethod = "COD";
                    }
//                    System.out.print("Payment Method (COD/UPI/CARD): ");
//                    String pay = sc.nextLine().toUpperCase();

                    boolean ok = orderService.checkout(user.getUserId(), ship, bill, paymentMethod);
                    System.out.println(ok ? "✅ Checkout Done!" : "❌ Checkout Failed!");
                }

                // ✅ Buyer order history
                case 9 -> {
                    orderService.showBuyerOrderHistory(user.getUserId());
                }

                // ✅ Buyer notifications
                case 10 -> {
                    notificationService.viewNotifications(user.getUserId());
                    System.out.print("Enter Notification ID to mark as read: ");
                    int nid = sc.nextInt();
                    sc.nextLine();

                    boolean ok = notificationService.markRead(nid, user.getUserId());

                    if (ok) System.out.println("✅ Notification marked as read!");
                    else System.out.println("❌ Invalid Notification ID!");

                }
                case 11 -> {
                    System.out.print("Enter Product ID to Review: ");
                    int productId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Rating (1 to 5): ");
                    int rating = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Comment: ");
                    String comment = sc.nextLine();

                    boolean ok = reviewService.addReview(productId, user.getUserId(), rating, comment);
                    System.out.println(ok ? "✅ Review Added!" : "❌ Review Failed / Already reviewed!");
                }
                case 12 -> {
                    System.out.print("Enter Product ID to View Reviews: ");
                    int productId = sc.nextInt();
                    sc.nextLine();

                    reviewService.viewProductReviews(productId);
                }

                case 13-> {
                    System.out.print("Enter Old Password: ");
                    String oldPass = sc.nextLine();

                    System.out.print("Enter New Password: ");
                    String newPass = sc.nextLine();

                    boolean ok = authService.changePassword(user.getUserId(), oldPass, newPass);
                    System.out.println(ok ? "✅ Password Changed Successfully!" : "❌ Old Password Incorrect!");
                }

                case 14 -> {
                    System.out.println("✅ Logged out successfully!");
                    return;
                }
                case 15 -> {
                    System.out.print("Enter Product ID to Favorite: ");
                    int pid = sc.nextInt();
                    sc.nextLine();

                    boolean ok = favoritesService.add(user.getUserId(), pid);
                    System.out.println(ok ? "✅ Added to Favorites!" : "❌ Failed / Already added!");
                }

                case 16 -> {
                    favoritesService.view(user.getUserId());
                }

                case 17 -> {
                    System.out.print("Enter Product ID to Remove Favorite: ");
                    int pid = sc.nextInt();
                    sc.nextLine();

                    boolean ok = favoritesService.remove(user.getUserId(), pid);
                    System.out.println(ok ? "✅ Removed from Favorites!" : "❌ Not found!");
                }

                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }



    // ✅ Seller Menu
    private static void sellerMenu(Scanner sc, AuthService authService, User user) {

        ProductService productService = new ProductService();
        OrderService orderService = new OrderService();
        NotificationService notificationService = new NotificationService();
        ReviewService reviewService = new ReviewService();

        while (true) {
            System.out.println("\n===== SELLER MENU =====");
            System.out.println("1. Add Product");
            System.out.println("2. View My Products");
            System.out.println("3. Update Stock");
            System.out.println("4. Update Price");
            System.out.println("5. Delete Product");

            System.out.println("6. View My Orders");
            System.out.println("7. View Notifications");
            System.out.println("8. View Reviews for My Products");
            System.out.println("9. Change Password");
            System.out.println("10. Logout");
            System.out.print("Choose: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {

                case 1 -> {
                    Product p = new Product();

                    p.setSellerId(user.getUserId());

                    System.out.print("Category ID (1-Mobiles,2-Laptops,3-Fashion,4-Home): ");
                    p.setCategoryId(sc.nextInt());
                    sc.nextLine();

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

                    System.out.print("Threshold Stock (low stock alert): ");
                    p.setThresholdStock(sc.nextInt());
                    sc.nextLine();

                    boolean ok = productService.addProduct(p);
                    System.out.println(ok ? "✅ Product Added Successfully!" : "❌ Failed to Add Product!");
                }

                case 2 -> {
                    var products = productService.viewSellerProducts(user.getUserId());


                    if (products.isEmpty()) {
                        System.out.println("⚠ No products available.");
                    } else {
                        System.out.println("\n--- Products ---");
                        for (var p : products) {
                            String alert = (p.getStock() <= p.getThresholdStock()) ? "⚠ LOW STOCK" : "";
                            System.out.println("ID: " + p.getProductId()
                                    + " | Name: " + p.getProductName()
                                    + " | Stock: " + p.getStock()
                                    + " | Threshold: " + p.getThresholdStock()
                                    + " " + alert);
                        }
                    }
                }

                case 3 -> {
                    System.out.print("Enter Product ID: ");
                    int productId = sc.nextInt();

                    System.out.print("Enter New Stock: ");
                    int newStock = sc.nextInt();
                    sc.nextLine();

                    boolean ok = productService.updateStock(productId, user.getUserId(), newStock);
                    System.out.println(ok ? "✅ Stock Updated Successfully!" : "❌ Stock Update Failed!");
                }

                case 4 -> {
                    System.out.print("Enter Product ID: ");
                    int productId = sc.nextInt();

                    System.out.print("Enter New MRP: ");
                    double newMrp = sc.nextDouble();

                    System.out.print("Enter New Discounted Price: ");
                    double newDp = sc.nextDouble();
                    sc.nextLine();

                    boolean ok = productService.updatePrice(productId, user.getUserId(), newMrp, newDp);
                    System.out.println(ok ? "✅ Price Updated Successfully!" : "❌ Price Update Failed!");
                }

                case 5 -> {
                    System.out.print("Enter Product ID to Delete: ");
                    int productId = sc.nextInt();
                    sc.nextLine();

                    boolean ok = productService.deleteProduct(productId, user.getUserId());
                    System.out.println(ok ? "✅ Product Deleted!" : "❌ Delete Failed!");
                }

                // ✅ Seller orders
                case 6 -> {
                    orderService.showSellerOrders(user.getUserId());
                }

                // ✅ Seller notifications
                case 7 -> {
                    notificationService.viewNotifications(user.getUserId());
                    System.out.print("Enter Notification ID to mark as read: ");
                    int nid = sc.nextInt();
                    sc.nextLine();

                    boolean ok = notificationService.markRead(nid, user.getUserId());

                    if (ok) System.out.println("✅ Notification marked as read!");
                    else System.out.println("❌ Invalid Notification ID!");
                }
                case 8 -> {
                    reviewService.viewSellerProductReviews(user.getUserId());
                }

                case 9-> {
                    System.out.print("Enter Old Password: ");
                    String oldPass = sc.nextLine();

                    System.out.print("Enter New Password: ");
                    String newPass = sc.nextLine();

                    boolean ok = authService.changePassword(user.getUserId(), oldPass, newPass);
                    System.out.println(ok ? "✅ Password Changed Successfully!" : "❌ Old Password Incorrect!");
                }

                case 10 -> {
                    System.out.println("✅ Logged out successfully!");
                    return;
                }

                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }

    // ✅ Predefined security questions
    private static String getSecurityQuestion(Scanner sc) {

        System.out.println("\nChoose Security Question:");
        System.out.println("1. What is your pet name?");
        System.out.println("2. What is your first school name?");
        System.out.println("3. What is your favourite food?");
        System.out.println("4. What is your birthplace?");
        System.out.println("5. What is your best friend name?");
        System.out.print("Enter choice (1-5): ");

        int qChoice = sc.nextInt();
        sc.nextLine();

        return switch (qChoice) {
            case 1 -> "What is your pet name?";
            case 2 -> "What is your first school name?";
            case 3 -> "What is your favourite food?";
            case 4 -> "What is your birthplace?";
            case 5 -> "What is your best friend name?";
            default -> "What is your pet name?";
        };
    }
}
