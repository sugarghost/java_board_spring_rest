package com.board.spring.rest.yoony.error;

import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * 전역 예외 처리를 위한 클래스
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 26.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 에러 메시지를 가져오기 위해 사용하는 메시지 소스
   */
  @Autowired
  private MessageSource messageSource;


  /**
   * CustomException 예외를 처리하고 에러 응답을 반환한다.
   * <p>에러 코드와 에러 메시지를 {@link ResponseEntity}에 담아서 반환한다.
   * <p>에러 코드는 {@link ErrorCode}에 정의된 에러 코드를 사용한다.
   * <p>에러 메시지는 {@link MessageSource}를 사용하여 현재 로케일에 맞는 에러 메시지를 가져온다.
   *
   * @param ex CustomException 예외
   * @return ResponseEntity<ErrorResponse> 에러 응답을 담은 ResponseEntity 객체
   * @see ResponseEntity
   * @see ErrorResponse
   * @see CustomException
   * @since 2023. 02. 26.
   */
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
    log.error("handleCustomException", ex);
    Locale currentLocale = LocaleContextHolder.getLocale();
    String errorMessage = messageSource.getMessage(ex.getErrorCode().getErrorCode(), null,
        currentLocale);
    ErrorResponse response = new ErrorResponse(ex.getErrorCode(), errorMessage);
    return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
  }


  @ExceptionHandler(Exception.class)
  public ResponseEntity handleException(Exception ex) {
    log.error("handleException", ex);
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), "INTERNAL_SERVER_ERROR");
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}