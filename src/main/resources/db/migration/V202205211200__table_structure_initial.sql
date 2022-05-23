DROP TABLE IF EXISTS "public"."travel_order";
CREATE TABLE "public"."travel_order"
(
    "id"                    int8                                       NOT NULL,
    "customer_id"           varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
    "start_time"            timestamp(6),
    "end_time"              timestamp(6),
    "status"                varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
    "starting_point"        varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "destination"           varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "actually_paid_amount"  numeric(10, 2),
    "create_datetime"       timestamp(6)                               NOT NULL DEFAULT now(),
    "update_datetime"       timestamp(6)                               NOT NULL DEFAULT now(),
    PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "public"."travel_order"."id" IS '订单 id';
COMMENT ON COLUMN "public"."travel_order"."customer_id" IS '用户 id';
COMMENT ON COLUMN "public"."travel_order"."start_time" IS '开始运输时间';
COMMENT ON COLUMN "public"."travel_order"."end_time" IS '运输结束时间';
COMMENT ON COLUMN "public"."travel_order"."status" IS '订单状态';
COMMENT ON COLUMN "public"."travel_order"."starting_point" IS '运输起点';
COMMENT ON COLUMN "public"."travel_order"."destination" IS '运输终点';
COMMENT ON COLUMN "public"."travel_order"."actually_paid_amount" IS '订单实际实付金额';
COMMENT ON COLUMN "public"."travel_order"."create_datetime" IS '订单创建时间';
COMMENT ON COLUMN "public"."travel_order"."update_datetime" IS '订单更新时间';
COMMENT ON TABLE "public"."travel_order" IS '订单表';


DROP TABLE IF EXISTS "public"."order_invoice";
CREATE TABLE "public"."order_invoice"
(
    "id"                    int8                                       NOT NULL,
    "order_id"              int8                                       NOT NULL,
    "status"                varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
    "type"                  varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
    "title"                 varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
    "amount"                numeric(10, 2),
    "file_location"         varchar(256) COLLATE "pg_catalog"."default",
    "create_datetime"       timestamp(6)                               NOT NULL DEFAULT now(),
    "update_datetime"       timestamp(6)                               NOT NULL DEFAULT now(),
    PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "public"."order_invoice"."id" IS '发票 id';
COMMENT ON COLUMN "public"."order_invoice"."order_id" IS '订单 id';
COMMENT ON COLUMN "public"."order_invoice"."status" IS '发票状态';
COMMENT ON COLUMN "public"."order_invoice"."type" IS '发票类型';
COMMENT ON COLUMN "public"."order_invoice"."title" IS '发票抬头';
COMMENT ON COLUMN "public"."order_invoice"."amount" IS '发票金额';
COMMENT ON COLUMN "public"."order_invoice"."file_location" IS '发票文件地址';
COMMENT ON COLUMN "public"."order_invoice"."create_datetime" IS '订单创建时间';
COMMENT ON COLUMN "public"."order_invoice"."update_datetime" IS '订单更新时间';
COMMENT ON TABLE "public"."order_invoice" IS '发票表';
