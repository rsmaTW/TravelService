package com.tw.travel.common.exception;

import static java.util.stream.Collectors.toList;

import com.tw.travel.common.response.ErrorResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
@Slf4j
public class BizExceptionHandler {

  private static final String SEPARATOR = ",";

  @ExceptionHandler(value = {BizException.class})
  public ResponseEntity<ErrorResponse> bizExceptionHandler(HttpServletRequest req, BizException e) {
    log.error(
        "[BizExceptionHandler] handle BizException, exception happened when call url = {}, query string = {}, error code = {}, error message = {}",
        req.getRequestURI(), req.getQueryString(), e.getCode(), e.getMessage(), e);
    return new ResponseEntity<>(failedOf(e.getCode(), e.getMessage()),
        e.getHttpStatus());
  }

  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  public ResponseEntity<ErrorResponse> badRequestHandler(
      MethodArgumentNotValidException e) {
    log.error("[BizExceptionHandler] handle MethodArgumentNotValidException, Bad request error", e);
    List<String> errors =
        e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::errorDetail)
            .collect(toList());
    return new ResponseEntity<>(validationFailedError(errors), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {BindException.class})
  public ResponseEntity<ErrorResponse> badRequestHandler(BindException e) {
    log.error("[BizExceptionHandler] handle BindException, Bad request error", e);
    List<String> errors =
        e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::errorDetail)
            .collect(toList());
    return new ResponseEntity<>(validationFailedError(errors), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> badRequestHandler(
      MethodArgumentTypeMismatchException e) {
    log.error("[BizExceptionHandler] handle MethodArgumentTypeMismatchException, Bad request error",
        e);
    return new ResponseEntity<>(
        failedOf(ErrorCode.VALIDATION_FAILED, e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ConstraintViolationException.class, MissingServletRequestPartException.class})
  public ResponseEntity<ErrorResponse> badRequestHandler(ConstraintViolationException e) {
    log.error(
        "[BizExceptionHandler] handle ConstraintViolationException, MissingServletRequestPartException, Bad request error",
        e);
    List<String> errors = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
        .collect(toList());
    return new ResponseEntity<>(validationFailedError(errors), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {HttpMessageNotReadableException.class})
  public ResponseEntity<ErrorResponse> validationParameterTypeFailedHandler(
      HttpServletRequest req,
      HttpMessageNotReadableException e) {
    log.error(
        "[BizExceptionHandler] handle HttpMessageNotReadableException, Error happen when call url = {}, query string = {}, error = {}",
        req.getRequestURI(), req.getQueryString(), e.getMessage(), e);
    return new ResponseEntity<>(
        failedOf(ErrorCode.VALIDATION_FAILED, getDetailMessage(e.getMessage())),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {IllegalArgumentException.class})
  public ResponseEntity<ErrorResponse> illegalArgumentExceptionHandler(
      HttpServletRequest req,
      IllegalArgumentException e) {
    log.error(
        "[BizExceptionHandler] handle IllegalArgumentException, Error happen when call url = {}, query string = {}, error = {}",
        req.getRequestURI(), req.getQueryString(), e.getMessage(), e);

    return new ResponseEntity<>(
        failedOf(ErrorCode.VALIDATION_FAILED, getDetailMessage(e.getMessage())),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> HttpRequestMethodNotSupportedExceptionHandler(
      HttpServletRequest req, HttpRequestMethodNotSupportedException e) {
    log.error(
        "[BizExceptionHandler] handle HttpRequestMethodNotSupportedException, Error happen when call url = {}, query string = {}, error = {}",
        req.getRequestURI(), req.getQueryString(), e.getMessage(), e);
    return new ResponseEntity<>(failedOf(ErrorCode.VALIDATION_FAILED, e.getMessage()),
        HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(value = {RuntimeException.class})
  public ResponseEntity<ErrorResponse> defaultErrorHandler(HttpServletRequest req,
      RuntimeException e) {
    log.error(
        "[BizExceptionHandler] handle RuntimeException, Error happen when call url = {}, query string = {}, error = {}",
        req.getRequestURI(), req.getQueryString(), e.getMessage(), e);

    return new ResponseEntity<>(failedOf(ErrorCode.SYS_ERROR, ErrorCode.SYS_ERROR.getMessage()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ErrorResponse validationFailedError(List<String> fieldErrors) {
    String message = CollectionUtils.isEmpty(fieldErrors) ? ErrorCode.VALIDATION_FAILED.getMessage()
        : String.join(SEPARATOR, fieldErrors);
    return failedOf(ErrorCode.VALIDATION_FAILED, message);
  }

  private ErrorResponse failedOf(ErrorCode errorCode, String message) {
    return new ErrorResponse(errorCode.getCode(), message);
  }

  private String getDetailMessage(String message) {
    if (Objects.isNull(message)) {
      return null;
    }
    return message.split("; ")[0];
  }

  private String errorDetail(org.springframework.validation.FieldError fieldError) {
    return Optional.ofNullable(fieldError)
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .orElse(Strings.EMPTY);
  }
}
