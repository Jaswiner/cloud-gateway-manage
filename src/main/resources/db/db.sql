SET NAMES utf8;

-- 网关路由表
DROP TABLE IF EXISTS route;
CREATE TABLE route
(
    id           bigint(20) PRIMARY KEY COMMENT 'id',
    route_id     VARCHAR(100) NOT NULL COMMENT '路由id',
    uri          VARCHAR(100) NOT NULL COMMENT 'uri路径',
    predicates   TEXT         NOT NULL COMMENT '判定器',
    filters      TEXT COMMENT '过滤器',
    orders       INT COMMENT '排序',
    description  VARCHAR(500) COMMENT '描述',
    status       tinyint(2)            DEFAULT 1 COMMENT '状态：1-有效，0-无效',
    created_time DATETIME     NOT NULL DEFAULT now() COMMENT '创建时间',
    updated_time DATETIME     NOT NULL DEFAULT now() COMMENT '更新时间',
    created_by   VARCHAR(100) NOT NULL COMMENT '创建人',
    updated_by   VARCHAR(100) NOT NULL COMMENT '更新人'
) COMMENT '网关路由表';

CREATE UNIQUE INDEX ux_cloud_gateway_uri ON gateway (uri);


-- DML初始数据
-- 路由数据
INSERT INTO route (id, route_id, uri, predicates, filters, orders, description, status, created_time, updated_time, created_by, updated_by)
VALUES
(101,
 'uua-server',
 'lb://uua-server:8000',
 '[{"name":"Path","args":{"pattern":"/uua-server/**"}}]',
 '[{"name":"StripPrefix","args":{"parts":"1"}}]',
 100,
 '授权认证服务网关注册',
 1, now(), now(), 'system', 'system'),
(102,
 'authentication-server',
 'lb://authentication-server:8001',
 '[{"name":"Path","args":{"pattern":"/authentication-server/**"}}]',
 '[{"name":"StripPrefix","args":{"parts":"1"}}]',
 100,
 '签权服务网关注册',
 1, now(), now(), 'system', 'system'),
(103,
 'organization',
 'lb://organization:8010',
 '[{"name":"Path","args":{"pattern":"/organization/**"}}]',
 '[{"name":"StripPrefix","args":{"parts":"1"}}]',
 100,
 '系统管理相关接口',
 1, now(), now(), 'system', 'system'),
(104,
 'gateway-admin',
 'lb://gateway-admin:8445',
 '[{"name":"Path","args":{"pattern":"/gateway-admin/**"}}]',
 '[{"name":"StripPrefix","args":{"parts":"1"}}]',
 100,
 '网关管理相关接口',
 1, now(), now(), 'system', 'system');


DROP TABLE IF EXISTS operation_log;
CREATE TABLE operation_log
(
    id          bigint       NULL,
    app         VARCHAR(30)  NOT NULL COMMENT 'APP名称',
    username    varchar(30)  NOT NULL COMMENT '操作人',
    operation   varchar(255) NOT NULL COMMENT '操作说明',
    start_time  datetime     NOT NULL COMMENT '操作时间',
    time        int          NOT NULL COMMENT '耗时(ms)',
    method      varchar(255) NOT NULL COMMENT '方法',
    params      text         NULL COMMENT '参数',
    ip          varchar(20)  NOT NULL COMMENT '操作者IP',
    method_type varchar(10)  NOT NULL
)
    COMMENT '操作日志' CHARSET = utf8mb4;


