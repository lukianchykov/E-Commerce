# USE ecommercedb;
#
# CREATE TABLE `orders`
# (
#     `id`           bigint(20)   NOT NULL AUTO_INCREMENT,
#     `number`       varchar(255) NOT NULL,
#     `order_status` varchar(20)  ,
#     PRIMARY KEY (`id`)
# ) ENGINE = InnoDB
#   DEFAULT CHARSET = utf8mb4;
#
# CREATE TABLE `items`
# (
#     `id`       bigint(20)     NOT NULL AUTO_INCREMENT,
#     `name`     varchar(255)   NOT NULL,
#     `price`    decimal(19, 2) NOT NULL,
#     `order_id` bigint(20)     ,
#     PRIMARY KEY (`id`),
#     KEY `FK_ITEM_ORDER_ID` (`order_id`),
#     CONSTRAINT `FK_ITEM_ORDER_ID` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
# ) ENGINE = InnoDB
#   DEFAULT CHARSET = utf8mb4;
#
# CREATE TABLE `payments`
# (
#     `id`              bigint(20)     NOT NULL AUTO_INCREMENT,
#     `number`          varchar(255)   NOT NULL,
#     `sum`             decimal(19, 2) NOT NULL,
#     `paymentDateTime` datetime       ,
#     `order_id`        bigint(20)     ,
#     PRIMARY KEY (`id`),
#     KEY `FK_PAYMENT_ORDER_ID` (`order_id`),
#     CONSTRAINT `FK_PAYMENT_ORDER_ID` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
# ) ENGINE = InnoDB
#   DEFAULT CHARSET = utf8mb4;
#
# CREATE TABLE `users`
# (
#     `id`       BIGINT       NOT NULL AUTO_INCREMENT,
#     `username` VARCHAR(20)  NOT NULL,
#     `email`    VARCHAR(50)  NOT NULL,
#     `password` VARCHAR(120) NOT NULL,
#     PRIMARY KEY (`id`),
#     UNIQUE KEY `UK_users_username` (`username`),
#     UNIQUE KEY `UK_users_email` (`email`)
# ) ENGINE = InnoDB
#   DEFAULT CHARSET = utf8mb4;
#
# CREATE TABLE `roles`
# (
#     `id`   BIGINT      NOT NULL AUTO_INCREMENT,
#     `name` VARCHAR(20) NOT NULL,
#     PRIMARY KEY (`id`)
# ) ENGINE = InnoDB
#   DEFAULT CHARSET = utf8mb4;
#
# CREATE TABLE `user_roles`
# (
#     `user_id` BIGINT NOT NULL,
#     `role_id` BIGINT NOT NULL,
#     PRIMARY KEY (`user_id`, `role_id`),
#     CONSTRAINT `FK_user_roles_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
#     CONSTRAINT `FK_user_roles_roles` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
# ) ENGINE = InnoDB
#   DEFAULT CHARSET = utf8mb4;
