DROP TABLE IF EXISTS ITEM;

CREATE TABLE ITEM
(
    id varchar(36) NOT NULL,
    name varchar(200) NOT NULL,
    description varchar(500) DEFAULT NULL,
    price decimal(5,2) NOT NULL,
    PRIMARY KEY (id)
);