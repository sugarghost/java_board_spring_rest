package com.board.spring.rest.yoony.error;

import lombok.Getter;

/**
 * CustomException
 * <p>사용자 정의 예외
 * <p>Action 처리 중 발생한 예외를 처리하기 위한 클래스
 *
 * @author yoony
 * @version 1.0
 * @see ErrorCode
 * @since 2023. 02. 26.
 */
@Getter
public class CustomException extends RuntimeException {

  /**
   * 미리 정의된 에러 코드
   */
  private final ErrorCode errorCode;

  /**
   * CustomException 생성자
   * <p>ErrorCode의 메시지를 사용하여 예외를 생성한다.
   *
   * @param errorCode 에러 코드
   * @author yoony
   * @version 1.0
   * @see ErrorCode 에러 코드
   * @since 2023. 02. 26.
   */
  public CustomException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
