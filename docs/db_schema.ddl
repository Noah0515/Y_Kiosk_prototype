create database YKiosk_system;
use YKiosk_system;

CREATE TABLE `user_info`
(
    `user_id`    CHAR(255) NOT NULL,
    `name`    VARCHAR(20) NOT NULL,
    `user_type`    VARCHAR(30) NOT NULL,
    `create_at`    DATE NOT NULL,
    PRIMARY KEY ( `user_id` )
);

CREATE TABLE `user_account`
(
    `login_id`    CHAR(60) NOT NULL,
    `user_id`    VARCHAR(255) NOT NULL,
    `pw`    VARCHAR(255) NOT NULL,
    PRIMARY KEY ( `login_id`,`user_id` ),
    FOREIGN KEY (`user_id`) REFERENCES `user_info`(`user_id`)
);

CREATE TABLE `social_login`
(
    `user_id`    CHAR(255) NOT NULL,
    `kakao_email`    CHAR(60) NOT NULL,
    `kakao_id`    CHAR(60) NOT NULL,
    PRIMARY KEY ( `user_id` ),
    FOREIGN KEY (`user_id`) REFERENCES `user_info`(`user_id`)
);


CREATE TABLE `store`
(
    `store_id`    VARCHAR(255) NOT NULL,
    `user_id`    CHAR(255) NOT NULL,
    `store_name`    CHAR(20) NOT NULL,
    `verify_code`    CHAR(60) NOT NULL,
    `state`    VARCHAR(30) NOT NULL,
    `create_date`    DATE NOT NULL,
    PRIMARY KEY ( `store_id` ),
    FOREIGN KEY (`user_id`) REFERENCES `user_info`(`user_id`)
);

CREATE TABLE `employee_position`
(
    `store_id`    VARCHAR(255) NOT NULL,
    `position`    INT NOT NULL,
    `position_name`    CHAR(20) NOT NULL,
    PRIMARY KEY (`store_id`, `position`),
    FOREIGN KEY (`store_id`) REFERENCES `store`(`store_id`)
);

CREATE TABLE `employee`
(
    `user_id`           CHAR(255) NOT NULL,
    `store_id`          VARCHAR(255) NOT NULL,
    `position`          INT       NOT NULL,
    `employee_id`       CHAR(255) NOT NULL,
    `employee_nickname` CHAR(20)  NOT NULL,
    PRIMARY KEY (`employee_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`),
    FOREIGN KEY (`store_id`, `position`) REFERENCES `employee_position` (`store_id`, `position`)
);

CREATE TABLE `menu_group`
(
    `store_id`    VARCHAR(255) NOT NULL,
    `menu_group_id`    INT NOT NULL,
    `menu_group_name`    CHAR(20) NOT NULL,
    PRIMARY KEY ( `menu_group_id` ),
    FOREIGN KEY (`store_id`) REFERENCES `store`(`store_id`)
);

CREATE TABLE `menu_category`
(
    `menu_category_name`    CHAR(20) NOT NULL,
    `menu_group_id`    INT NOT NULL,
    `menu_category_id`    INT NOT NULL,
    PRIMARY KEY ( `menu_category_id` ),
    FOREIGN KEY (`menu_group_id`) REFERENCES `menu_group`(`menu_group_id`)
);

CREATE TABLE `menu`
(
    `menu_name`    CHAR(20) NOT NULL,
    `menu_category_id`    INT NOT NULL,
    `menu_id`    INT NOT NULL,
    `menu_info`    CHAR(30),
    `allergy`    VARCHAR(255),
    `menu_state`    VARCHAR(30) NOT NULL,
    PRIMARY KEY ( `menu_id` ),
    FOREIGN KEY (`menu_category_id`) REFERENCES `menu_category`(`menu_category_id`)
);

CREATE TABLE `menu_option`
(
    `menu_id`    INT NOT NULL,
    `option_name`    VARCHAR(20) NOT NULL,
    `option_id`    INT NOT NULL,
    `selection_num`    INT NOT NULL,
    PRIMARY KEY ( `menu_id`,`option_id` ),
    FOREIGN KEY (`menu_id`) REFERENCES `menu`(`menu_id`)
);


CREATE TABLE `option_category`
(
    `menu_id`    INT NOT NULL,
    `category_id`    INT NOT NULL,
    `option_content`    VARCHAR(20) NOT NULL,
    `option_id`    INT NOT NULL,
    PRIMARY KEY ( `menu_id`,`option_id`,`category_id` ),
    FOREIGN KEY (`menu_id`, `option_id`) REFERENCES `menu_option`(`menu_id`, `option_id`)
);


CREATE TABLE `orders`
(
    `order_num`    INT NOT NULL,
    `store_id`    VARCHAR(255) NOT NULL,
    `menu_group_id`    INT NOT NULL,
    `order_state`    VARCHAR(20) NOT NULL,
    `order_time`    DATETIME NOT NULL,
    `version`   INT NOT NULL DEFAULT 0,
    PRIMARY KEY ( `order_num`,`order_time`,`store_id` ),
    FOREIGN KEY (`store_id`) REFERENCES `store`(`store_id`),
    FOREIGN KEY (`menu_group_id`) REFERENCES `menu_group`(`menu_group_id`)
);


CREATE TABLE `ordered_menu`
(
    `order_num`    INT NOT NULL,
    `menu_id`    INT NOT NULL,
    `order_time`    DATETIME NOT NULL,
    `store_id`    VARCHAR(255) NOT NULL,
    `ordered_menu_seq` INT NOT NULL, --
    `quantity`  INT NOT NULL,
    PRIMARY KEY ( `order_num`,`menu_id`,`order_time`,`store_id`, `ordered_menu_seq`),
    FOREIGN KEY (`order_num`, `order_time`, `store_id`) REFERENCES `orders`(`order_num`, `order_time`, `store_id`),
    FOREIGN KEY (`menu_id`) REFERENCES `menu`(`menu_id`)

);


CREATE TABLE `ordered_menu_option`
(
    `order_num`    INT NOT NULL,
    `menu_id`    INT NOT NULL,
    `cat_menu_id` INT NOT NULL,
    `option_id`    INT NOT NULL,
    `ordered_menu_seq` INT NOT NULL, --
    `category_id`    INT NOT NULL,
    `option_content`    VARCHAR(20) NOT NULL,
    `order_time`    DATETIME NOT NULL,
    `store_id`    VARCHAR(255) NOT NULL,
    PRIMARY KEY ( `order_num`,`menu_id`, `cat_menu_id`, `order_time`,`store_id`, `ordered_menu_seq`, `option_id`, `category_id`),
    FOREIGN KEY (`order_num`, `menu_id`, `order_time`, `store_id`, `ordered_menu_seq`) REFERENCES `ordered_menu`(`order_num`, `menu_id`, `order_time`, `store_id`, `ordered_menu_seq`),
    FOREIGN KEY (`cat_menu_id`, `option_id`, `category_id`) REFERENCES `option_category`(`menu_id`, `option_id`, `category_id`)
);
