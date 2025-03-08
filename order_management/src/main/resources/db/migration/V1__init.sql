USE ecommercedb;

CREATE TABLE `order_events`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT,
    `number`       varchar(255) NOT NULL,
    `order_status` varchar(20)  NOT NULL CHECK (`order_status` IN ('CREATED', 'PROCESSING', 'SHIPPING', 'DELIVERED')),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `item_events`
(
    `id`            bigint(20)     NOT NULL AUTO_INCREMENT,
    `name`          varchar(255)   NOT NULL,
    `price`         decimal(19, 2) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `payment_events`
(
    `id`                 bigint(20)     NOT NULL AUTO_INCREMENT,
    `number`             varchar(255)   NOT NULL,
    `sum`                decimal(19, 2) NOT NULL,
    `payment_date_time`  datetime       DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
