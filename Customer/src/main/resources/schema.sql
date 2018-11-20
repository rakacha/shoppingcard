DROP TABLE IF EXISTS CUSTOMER;

CREATE TABLE CUSTOMER
(
    id varchar(36) NOT NULL,
    email varchar(200) NOT NULL,
    firstname varchar(50) DEFAULT NULL,
    lastname varchar(50) DEFAULT NULL,
    PRIMARY KEY (id)
);