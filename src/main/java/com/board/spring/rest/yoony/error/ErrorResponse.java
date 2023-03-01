package com.board.spring.rest.yoony.error;

import lombok.Getter;
import lombok.Setter;

/**
 * ErrorResponse
 * <p>사용자 정의 예외처리에서 사용하기 위한 에러 응답 클래스
 * <p>에러 코드, 에러 메시지, HTTP 상태 코드를 담는다.
 *
 * @author yoony
 * @version 1.0
 * @see ErrorCode
 * @since 2023. 02. 26.
 */
@Getter
@Setter
public class ErrorResponse {

  /**
   * HTTP 상태 코드
   */
  private int status;
  /**
   * 에러 메시지
   */
  private String message;
  /**
   * 에러 코드
   */
  private String code;

  /**
   * ErrorResponse 생성자
   * <p>ErrorCode를 사용하여 에러 응답을 생성한다.
   *
   * @param errorCode 에러 코드
   * @author yoony
   * @version 1.0
   * @see ErrorCode 에러 코드
   * @since 2023. 02. 26.
   */
  public ErrorResponse(ErrorCode errorCode) {
    this.status = errorCode.getStatus();
    this.message = errorCode.getMessage();
    this.code = errorCode.getErrorCode();
  }

  /**
   * ErrorResponse 생성자
   * <p>ErrorCode에 대응되는 에러 메시지를 사용하여 에러 응답을 생성한다.
   *
   * @param errorCode 에러 코드
   * @param message   에러 메시지
   * @author yoony
   * @version 1.0
   * @see ErrorCode 에러 코드
   * @since 2023. 02. 26.
   */
  public ErrorResponse(ErrorCode errorCode, String message) {
    this.status = errorCode.getStatus();
    this.message = message;
    this.code = errorCode.getErrorCode();
  }

  public ErrorResponse(int status, String message, String code) {
    this.status = status;
    this.message = message;
    this.code = code;
  }

}