------------------------------------------------------------
-- RevShop Dummy Data Insert Script (Oracle)
------------------------------------------------------------

------------------------------------------------------------
-- 1) USERS (BUYERS + SELLERS)
------------------------------------------------------------
INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (201, 'TechZone Store', 'techzone@gmail.com', 'Tech@123', '9876543210', 'Bangalore', 'SELLER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (202, 'FashionHub', 'fashionhub@gmail.com', 'Fashion@123', '9876501234', 'Chennai', 'SELLER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (203, 'HomeMart', 'homemart@gmail.com', 'Home@123', '9876512345', 'Hyderabad', 'SELLER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (101, 'Shanthan Kumar', 'shanthan@gmail.com', 'Shan@123', '9998887776', 'Coimbatore', 'BUYER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (102, 'Akash', 'akash@gmail.com', 'Akash@123', '9000011111', 'Salem', 'BUYER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (103, 'Priya', 'priya@gmail.com', 'Priya@123', '9000022222', 'Madurai', 'BUYER');

------------------------------------------------------------
-- 2) PRODUCTS
------------------------------------------------------------
INSERT INTO PRODUCTS (product_id, seller_id, name, category, price, stock, description)
VALUES (301, 201, 'Lenovo Wireless Mouse', 'Electronics', 799, 50, 'Smooth wireless mouse, USB receiver');

INSERT INTO PRODUCTS (product_id, seller_id, name, category, price, stock, description)
VALUES (302, 201, 'Boat Bassheads Earphones', 'Electronics', 999, 35, 'Wired earphones with deep bass');

INSERT INTO PRODUCTS (product_id, seller_id, name, category, price, stock, description)
VALUES (303, 201, 'Samsung 25W Charger', 'Electronics', 1299, 20, 'Fast charger for Android devices');

INSERT INTO PRODUCTS (product_id, seller_id, name, category, price, stock, description)
VALUES (304, 202, 'Men Casual Shirt', 'Fashion', 999, 40, 'Cotton slim-fit casual shirt');

INSERT INTO PRODUCTS (product_id, seller_id, name, category, price, stock, description)
VALUES (305, 202, 'Women Handbag', 'Fashion', 1499, 25, 'Stylish handbag for daily use');

INSERT INTO PRODUCTS (product_id, seller_id, name, category, price, stock, description)
VALUES (306, 202, 'Running Shoes', 'Fashion', 1999, 30, 'Comfortable sports running shoes');

INSERT INTO PRODUCTS (product_id, seller_id, name, category, price, stock, description)
VALUES (307, 203, 'Non-stick Fry Pan', 'Home & Kitchen', 899, 60, 'Easy to clean non-stick pan');

INSERT INTO PRODUCTS (product_id, seller_id, name, category, price, stock, description)
VALUES (308, 203, 'LED Study Lamp', 'Home & Kitchen', 699, 45, 'Adjustable brightness study lamp');

INSERT INTO PRODUCTS (product_id, seller_id, name, category, price, stock, description)
VALUES (309, 203, 'Water Bottle 1L', 'Home & Kitchen', 399, 100, 'BPA-free plastic bottle');

------------------------------------------------------------
-- 3) CART (CART CREATION)
------------------------------------------------------------
-- Buyer 101 cart
INSERT INTO CART (cart_id, buyer_id)
VALUES (401, 101);

-- Buyer 102 cart
INSERT INTO CART (cart_id, buyer_id)
VALUES (402, 102);

------------------------------------------------------------
-- 4) CART_ITEMS (ITEMS INSIDE CART)
------------------------------------------------------------
-- Buyer 101 cart items
INSERT INTO CART_ITEMS (cart_item_id, cart_id, product_id, quantity)
VALUES (501, 401, 301, 2);

INSERT INTO CART_ITEMS (cart_item_id, cart_id, product_id, quantity)
VALUES (502, 401, 304, 1);

INSERT INTO CART_ITEMS (cart_item_id, cart_id, product_id, quantity)
VALUES (503, 401, 308, 1);

-- Buyer 102 cart items
INSERT INTO CART_ITEMS (cart_item_id, cart_id, product_id, quantity)
VALUES (504, 402, 309, 3);

------------------------------------------------------------
-- 5) ORDERS
------------------------------------------------------------
INSERT INTO ORDERS (order_id, buyer_id, order_date, status, total_amount)
VALUES (601, 101, TO_DATE('2026-01-29','YYYY-MM-DD'), 'PLACED', 3397);

INSERT INTO ORDERS (order_id, buyer_id, order_date, status, total_amount)
VALUES (602, 102, TO_DATE('2026-01-28','YYYY-MM-DD'), 'PLACED', 1197);

------------------------------------------------------------
-- 6) ORDER_ITEMS
------------------------------------------------------------
-- Order 601 (Buyer 101)
INSERT INTO ORDER_ITEMS (order_item_id, order_id, product_id, quantity, price)
VALUES (701, 601, 301, 2, 799);

INSERT INTO ORDER_ITEMS (order_item_id, order_id, product_id, quantity, price)
VALUES (702, 601, 304, 1, 999);

INSERT INTO ORDER_ITEMS (order_item_id, order_id, product_id, quantity, price)
VALUES (703, 601, 308, 1, 699);

-- Order 602 (Buyer 102)
INSERT INTO ORDER_ITEMS (order_item_id, order_id, product_id, quantity, price)
VALUES (704, 602, 309, 3, 399);

------------------------------------------------------------
-- 7) REVIEWS
------------------------------------------------------------
INSERT INTO REVIEWS (review_id, buyer_id, product_id, rating, comment, review_date)
VALUES (801, 101, 301, 5, 'Very smooth mouse, worth the price', TO_DATE('2026-01-29','YYYY-MM-DD'));

INSERT INTO REVIEWS (review_id, buyer_id, product_id, rating, comment, review_date)
VALUES (802, 102, 304, 4, 'Shirt quality is good but size runs tight', TO_DATE('2026-01-28','YYYY-MM-DD'));

INSERT INTO REVIEWS (review_id, buyer_id, product_id, rating, comment, review_date)
VALUES (803, 103, 308, 5, 'Perfect brightness for study', TO_DATE('2026-01-27','YYYY-MM-DD'));

------------------------------------------------------------
-- 8) FAVORITES
------------------------------------------------------------
INSERT INTO FAVORITES (favorite_id, buyer_id, product_id)
VALUES (901, 101, 302);

INSERT INTO FAVORITES (favorite_id, buyer_id, product_id)
VALUES (902, 101, 305);

INSERT INTO FAVORITES (favorite_id, buyer_id, product_id)
VALUES (903, 102, 309);

------------------------------------------------------------
COMMIT;
------------------------------------------------------------
INSERT INTO categories (category_name) VALUES ('Mobiles');
INSERT INTO categories (category_name) VALUES ('Laptops');
INSERT INTO categories (category_name) VALUES ('Fashion');
INSERT INTO categories (category_name) VALUES ('Home Appliances');