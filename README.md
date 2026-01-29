# RevShop - Console Based E-Commerce Application (Java + Oracle JDBC)

RevShop is a secure console-based e-commerce application for Buyers and Sellers.  
It supports product browsing, cart management, order placement, reviews, favorites, and notifications using Oracle Database.

---

## ✅ Tech Stack
- Java (JDK 21)
- Oracle Database (Schema: REVSHOP)
- JDBC (PreparedStatement)
- Maven
- JUnit 5
- Log4j2
- Git (for version control)

---

## ✅ Features

### ✅ Buyer Features
- Register & Login
- Browse products
- Search products by keyword
- Browse by category
- View product details
- Add to cart / Update quantity / Remove from cart
- Checkout (Shipping + Billing + Payment Method)
- View order history
- Add reviews & ratings
- Add to favorites / View favorites / Remove favorites
- View notifications

### ✅ Seller Features
- Register & Login
- Add product
- Update stock
- Update price
- Delete product
- View orders placed for seller products
- View reviews for seller products
- View notifications
- Low stock alert (stock <= threshold_stock)

### ✅ Common Features
- Change password
- Forgot password / Reset
- Logout

---

## ✅ Project Architecture (Layered)
- Presentation Layer: `MainApp` (Console UI)
- Service Layer: Business Logic
- DAO Layer: Database Operations (JDBC)
- Utility Layer: `DBConnection`
- Database Layer: Oracle DB (REVSHOP schema)

---

## ✅ Folder Structure (Maven)

```text
Rev_Shop/
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java
    │   │   └── com/revshop
    │   │       ├── main
    │   │       ├── service
    │   │       ├── dao
    │   │       ├── model
    │   │       └── util
    │   └── resources
    │       └── log4j2.xml
    └── test
        └── java
            └── com/revshop/test
  ```      
            
## Database Setup (Oracle)
✅ 1) Create Schema User (Run as SYS / SYSTEM)
ALTER SESSION SET "_ORACLE_SCRIPT"=true;

SHOW CON_NAME;
SHOW PDBS;
ALTER SESSION SET CONTAINER="XEPDB1";
CREATE USER revshop IDENTIFIED BY revshop123;
SHOW USER:
GRANT CONNECT, RESOURCE TO revshop;
GRANT CREATE TABLE TO revshop;
GRANT CREATE SEQUENCE TO revshop;
GRANT UNLIMITED TABLESPACE TO revshop;

✅ 2) Login as revshop and Create Tables

Run your full schema SQL file to create the tables:

USERS

CATEGORIES

PRODUCTS

CART

CART_ITEMS

ORDERS

ORDER_ITEMS

REVIEWS

FAVORITES

NOTIFICATIONS

✅ Insert initial categories:

INSERT INTO categories (category_id, category_name) VALUES (1, 'Mobiles');
INSERT INTO categories (category_id, category_name) VALUES (2, 'Laptops');
INSERT INTO categories (category_id, category_name) VALUES (3, 'Fashion');
INSERT INTO categories (category_id, category_name) VALUES (4, 'Home Appliances');
COMMIT;

✅ JDBC Connection Configuration

Update file:
src/main/java/com/revshop/util/DBConnection.java

Example:

private static final String URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
private static final String USER = "revshop";
private static final String PASSWORD = "revshop123";

✅ How to Run the Application

Open project in IntelliJ

Load Maven dependencies (pom.xml)

Run:
com.revshop.main.MainApp

✅ How to Run JUnit Tests

Tests are available under:
src/test/java/com/revshop/test

Run:

SampleTest

AuthServiceTest

ProductServiceTest

✅ Documentation

ERD Diagram: Included in submission

Architecture Diagram: Included in submission

Checkout Flow Diagram: Included in submission

✅ Author

Shanthan Kumar