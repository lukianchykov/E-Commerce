USE ecommercedb;

CREATE TABLE `orders`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT,
    `number`       varchar(255) NOT NULL,
    `order_status` varchar(20)  ,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `items`
(
    `id`       bigint(20)     NOT NULL AUTO_INCREMENT,
    `name`     varchar(255)   NOT NULL,
    `price`    decimal(19, 2) NOT NULL,
    `order_id` bigint(20)     ,
    PRIMARY KEY (`id`),
    KEY `FK_ITEM_ORDER_ID` (`order_id`),
    CONSTRAINT `FK_ITEM_ORDER_ID` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `payments`
(
    `id`              bigint(20)     NOT NULL AUTO_INCREMENT,
    `number`          varchar(255)   NOT NULL,
    `sum`             decimal(19, 2) NOT NULL,
    `paymentDateTime` datetime       ,
    `order_id`        bigint(20)     ,
    PRIMARY KEY (`id`),
    KEY `FK_PAYMENT_ORDER_ID` (`order_id`),
    CONSTRAINT `FK_PAYMENT_ORDER_ID` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;