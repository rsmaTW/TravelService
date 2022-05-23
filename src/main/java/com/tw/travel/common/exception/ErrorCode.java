package com.tw.travel.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {


    VALIDATION_FAILED(11000001, "validation failed"),
    SYS_ERROR(11000002, "system error"),

    ORDER_NOT_FOUND(10000001, "order not exist, order id is %s"),
    ORDER_STATUS_CAN_NOT_CREATE_INVOICE(10000007, "order can not create invoice, order id is %s, status is %s"),
    ;


    private Integer code;
    private String message;
}
