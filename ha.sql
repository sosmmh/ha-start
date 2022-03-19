CREATE TABLE `user`
(
    `id`    bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `name`  varchar(30) NOT NULL DEFAULT '',
    `age`   int(11) NOT NULL,
    `email` varchar(50) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


CREATE TABLE `ha_message`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `msg_id`        varchar(64)           DEFAULT '' COMMENT '事务消息唯一ID',
    `ref_id`        varchar(64)           DEFAULT NULL COMMENT '业务id；可作为分表key',
    `send_args`     varchar(128)          DEFAULT NULL COMMENT 'mq参数；比如topic、partition；或者exchange，routingkey',
    `message`       varchar(512) NOT NULL DEFAULT '' COMMENT '消息内容',
    `status`        int(4) NOT NULL DEFAULT '1' COMMENT '状态；1=发送中',
    `retry_count`   int(4) DEFAULT '0' COMMENT '重试次数；最多3次，可配置',
    `next_exc_time` bigint(15) NOT NULL DEFAULT '0' COMMENT '下次执行时间',
    `type`          varchar(16)           DEFAULT NULL COMMENT 'mq类型；kafka、rabbitmq',
    `error`         varchar(256)          DEFAULT NULL COMMENT '错误日志；',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;