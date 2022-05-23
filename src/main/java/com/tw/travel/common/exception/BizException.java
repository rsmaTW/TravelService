package com.tw.travel.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BizException extends RuntimeException {

  private HttpStatus httpStatus;

  private ErrorCode code;

  private String message;

  public BizException(HttpStatus httpStatus, ErrorCode code, Object... messageParams) {
    this.httpStatus = httpStatus;
    this.code = code;
    this.message = String.format(code.getMessage(), messageParams);
  }

  public static BizException paramErrorException(ErrorCode errorCode, Object... messageParams) {
    return new BizException(HttpStatus.BAD_REQUEST, errorCode, messageParams);
  }

  public static BizException notFoundErrorException(ErrorCode errorCode, Object... messageParams) {
    return new BizException(HttpStatus.NOT_FOUND, errorCode, messageParams);
  }

  public static BizException systemErrorException(ErrorCode errorCode, Object... messageParams) {
    return new BizException(HttpStatus.INTERNAL_SERVER_ERROR, errorCode, messageParams);
  }

}
