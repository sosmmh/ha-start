CREATE TABLE `user`
(
    `id`    bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `name`  varchar(30) NOT NULL DEFAULT '',
    `age`   int(11) NOT NULL,
    `email` varchar(50) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1504811716079648770 DEFAULT CHARSET=utf8mb4;


CREATE TABLE `ha_message`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `msg_id`        varchar(64)           DEFAULT '' COMMENT '业务唯一ID',
    `topic`         varchar(64)  NOT NULL DEFAULT '',
    `message`       varchar(512) NOT NULL DEFAULT '',
    `status`        int(4) NOT NULL DEFAULT '1' COMMENT '发送状态；1=发送中，2=发送失败',
    `retry_count`   int(4) DEFAULT '0',
    `next_exc_time` bigint(15) NOT NULL DEFAULT '0' COMMENT '下一次发送时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;