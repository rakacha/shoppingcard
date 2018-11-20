DROP TABLE IF EXISTS CUSTOMER_SOS;

CREATE TABLE CUSTOMER_SOS
(
    id varchar(36) NOT NULL,
    email varchar(200) NOT NULL,
    firstname varchar(50) DEFAULT NULL,
    lastname varchar(50) DEFAULT NULL,
    PRIMARY KEY (id)
);


DROP TABLE IF EXISTS SALES_ORDER;

CREATE TABLE SALES_ORDER
(
    id varchar(36) NOT NULL,
    order_date date NOT NULL,
    customer_id varchar(50) DEFAULT NULL,
    order_desc varchar(50) DEFAULT NULL,
    price decimal(10,2) NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS ORDER_LINE_ITEM;

CREATE TABLE ORDER_LINE_ITEM
(
    id varchar(36) NOT NULL,
    item_name varchar(200) NOT NULL,
    item_quantity decimal(4) DEFAULT NULL,
    order_id varchar(50) DEFAULT NULL,
    PRIMARY KEY (id)
);