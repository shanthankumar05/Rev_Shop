CREATE TABLE USERS (
                       user_id     NUMBER PRIMARY KEY,
                       name        VARCHAR2(100) NOT NULL,
                       email       VARCHAR2(100) UNIQUE NOT NULL,
                       password    VARCHAR2(100) NOT NULL,
                       phone       VARCHAR2(15),
                       address     VARCHAR2(200),
                       role        VARCHAR2(20) NOT NULL CHECK (role IN ('BUYER','SELLER'))
);

------------------------------------------------------------
-- 2) PRODUCTS TABLE
------------------------------------------------------------
CREATE TABLE PRODUCTS (
                          product_id   NUMBER PRIMARY KEY,
                          seller_id    NUMBER NOT NULL,
                          name         VARCHAR2(150) NOT NULL,
                          category     VARCHAR2(100),
                          price        NUMBER(10,2) NOT NULL,
                          stock        NUMBER NOT NULL,
                          description  VARCHAR2(300),

                          CONSTRAINT fk_products_seller
                              FOREIGN KEY (seller_id)
                                  REFERENCES USERS(user_id)
                                  ON DELETE CASCADE
);

------------------------------------------------------------
-- 3) CART TABLE
------------------------------------------------------------
CREATE TABLE CART (
                      cart_id   NUMBER PRIMARY KEY,
                      buyer_id  NUMBER NOT NULL UNIQUE,

                      CONSTRAINT fk_cart_buyer
                          FOREIGN KEY (buyer_id)
                              REFERENCES USERS(user_id)
                              ON DELETE CASCADE
);

------------------------------------------------------------
-- 4) CART_ITEMS TABLE
------------------------------------------------------------
CREATE TABLE CART_ITEMS (
                            cart_item_id  NUMBER PRIMARY KEY,
                            cart_id       NUMBER NOT NULL,
                            product_id    NUMBER NOT NULL,
                            quantity      NUMBER NOT NULL CHECK (quantity > 0),

                            CONSTRAINT fk_cartitems_cart
                                FOREIGN KEY (cart_id)
                                    REFERENCES CART(cart_id)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_cartitems_product
                                FOREIGN KEY (product_id)
                                    REFERENCES PRODUCTS(product_id)
                                    ON DELETE CASCADE
);

------------------------------------------------------------
-- 5) ORDERS TABLE
------------------------------------------------------------
CREATE TABLE ORDERS (
                        order_id      NUMBER PRIMARY KEY,
                        buyer_id      NUMBER NOT NULL,
                        order_date    DATE DEFAULT SYSDATE,
                        status        VARCHAR2(30) DEFAULT 'PLACED',
                        total_amount  NUMBER(10,2) NOT NULL,

                        CONSTRAINT fk_orders_buyer
                            FOREIGN KEY (buyer_id)
                                REFERENCES USERS(user_id)
                                ON DELETE CASCADE
);

------------------------------------------------------------
-- 6) ORDER_ITEMS TABLE
------------------------------------------------------------
CREATE TABLE ORDER_ITEMS (
                             order_item_id  NUMBER PRIMARY KEY,
                             order_id       NUMBER NOT NULL,
                             product_id     NUMBER NOT NULL,
                             quantity       NUMBER NOT NULL CHECK (quantity > 0),
                             price          NUMBER(10,2) NOT NULL,

                             CONSTRAINT fk_orderitems_order
                                 FOREIGN KEY (order_id)
                                     REFERENCES ORDERS(order_id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_orderitems_product
                                 FOREIGN KEY (product_id)
                                     REFERENCES PRODUCTS(product_id)
                                     ON DELETE CASCADE
);

------------------------------------------------------------
-- 7) REVIEWS TABLE
------------------------------------------------------------
CREATE TABLE REVIEWS (
                         review_id    NUMBER PRIMARY KEY,
                         buyer_id     NUMBER NOT NULL,
                         product_id   NUMBER NOT NULL,
                         rating       NUMBER NOT NULL CHECK (rating BETWEEN 1 AND 5),
                         comment      VARCHAR2(300),
                         review_date  DATE DEFAULT SYSDATE,

                         CONSTRAINT fk_reviews_buyer
                             FOREIGN KEY (buyer_id)
                                 REFERENCES USERS(user_id)
                                 ON DELETE CASCADE,

                         CONSTRAINT fk_reviews_product
                             FOREIGN KEY (product_id)
                                 REFERENCES PRODUCTS(product_id)
                                 ON DELETE CASCADE
);

------------------------------------------------------------
-- 8) FAVORITES TABLE
------------------------------------------------------------
CREATE TABLE FAVORITES (
                           favorite_id  NUMBER PRIMARY KEY,
                           buyer_id     NUMBER NOT NULL,
                           product_id   NUMBER NOT NULL,

                           CONSTRAINT fk_favorites_buyer
                               FOREIGN KEY (buyer_id)
                                   REFERENCES USERS(user_id)
                                   ON DELETE CASCADE,

                           CONSTRAINT fk_favorites_product
                               FOREIGN KEY (product_id)
                                   REFERENCES PRODUCTS(product_id)
                                   ON DELETE CASCADE
);

------------------------------------------------------------
CREATE TABLE categories (
                            category_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            category_name VARCHAR2(100) UNIQUE NOT NULL
);

COMMIT;