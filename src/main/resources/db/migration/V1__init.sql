CREATE TABLE `payment`
(
    id                BIGINT         NOT NULL AUTO_INCREMENT,
    number            VARCHAR(255)   NOT NULL,
    sum               DECIMAL(19, 2) NOT NULL,
    payment_date_time TIMESTAMP      NOT NULL,
    order_id          BIGINT         NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE `item`
(
    id       BIGINT         NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255)   NOT NULL,
    price    DECIMAL(19, 2) NOT NULL,
    order_id BIGINT         NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE `order`
(
    id             BIGINT                                                  NOT NULL AUTO_INCREMENT,
    number         VARCHAR(255)                                            NOT NULL,
    order_status   ENUM ('CREATED', 'PROCESSING', 'SHIPPING', 'DELIVERED') NOT NULL,
    total_items    DECIMAL(19, 2)                                          NOT NULL,
    total_payments DECIMAL(19, 2)                                          NOT NULL,
    PRIMARY KEY (id)
);

alter table payment
    add constraint FK458pu56xefty15ugupb46wrin foreign key (order_id) references `order` (`id`);
alter table item
    add constraint FK7uhgl4ukxmr9cifj0e3cersf2 foreign key (order_id) references `order` (`id`);