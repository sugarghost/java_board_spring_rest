package com.board.spring.rest.yoony.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사용자 정의 예외처리에서 사용하기 위한 미리 정의된 에러 코드
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {
  /**
   * 잘못된 요청 처리
   */
  BAD_REQUEST(400, "COMMON.ERR.400", "BAD REQUEST"),
  /**
   * 요청한 페이지를 찾을 수 없음
   */
  NOT_FOUND(404, "COMMON.ERR.404", "PAGE NOT FOUND"),
  /**
   * 내부 서버 오류
   */
  INTER_SERVER_ERROR(500, "COMMON.ERR.500", "INTER SERVER ERROR"),

  // ARTICLE
  /**
   * 게시글 ID가 유효하지 않음
   */
  ARTICLE_ID_NOT_VALID(400, "ARTICLE.ERR.400.DTO.ID", "ARTICLE ID NOT VALID"),
  /**
   * 게시글 패스워드가 유효하지 않음
   */
  ARTICLE_PASSWORD_NOT_VALID(400, "ARTICLE.ERR.400.DTO.PASSWORD", "ARTICLE PASSWORD NOT VALID"),
  /**
   * 게시글 추가시 유효하지 않은 값이 있음
   */
  ARTICLE_INSERT_NOT_VALID(400, "ARTICLE.ERR.400.ACTION.INSERT", "ARTICLE INSERT NOT VALID"),
  /**
   * 게시글 수정시 유효하지 않은 값이 있음
   */
  ARTICLE_UPDATE_NOT_VALID(400, "ARTICLE.ERR.400.ACTION.UPDATE", "ARTICLE UPDATE NOT VALID"),

  /**
   * 게시글을 찾을 수 없음
   */
  ARTICLE_NOT_FOUND(404, "ARTICLE.ERR.404", "ARTICLE NOT FOUND"),

  /**
   * 게시글 추가 실패
   */
  ARTICLE_INSERT_FAIL(500, "ARTICLE.ERR.500.ACTION.INSERT", "ARTICLE INSERT FAIL"),
  /**
   * 게시글 수정 실패
   */
  ARTICLE_UPDATE_FAIL(500, "ARTICLE.ERR.500.ACTION.UPDATE", "ARTICLE UPDATE FAIL"),
  /**
   * 게시글 삭제 실패
   */
  ARTICLE_DELETE_FAIL(500, "ARTICLE.ERR.500.ACTION.DELETE", "ARTICLE DELETE FAIL"),

  // FILE
  /**
   * File ID가 유효하지 않음
   */
  File_ID_NOT_VALID(400, "FILE.ERR.400.DTO.ID", "FILE ID NOT VALID"),

  /**
   * 파일을 찾을 수 없음
   */
  FILE_NOT_FOUND(404, "FILE.ERR.404", "FILE NOT FOUND"),
  /**
   * 파일 추가 실패
   */
  FILE_INSERT_FAIL(500, "FILE.ERR.500.ACTION.INSERT", "FILE INSERT FAIL"),
  /**
   * 파일 삭제 실패
   */
  FILE_DELETE_FAIL(500, "FILE.ERR.500.ACTION.DELETE", "FILE DELETE FAIL"),

  // COMMENT
  /**
   * 댓글 ID가 유효하지 않음
   */
  COMMENT_ID_NOT_VALID(400, "COMMENT.ERR.400.DTO.ID", "COMMENT ID NOT VALID"),
  /**
   * 댓글 내용이 유효하지 않음
   */
  COMMENT_CONTENT_NOT_VALID(400, "COMMENT.ERR.400.DTO.CONTENT", "COMMENT CONTENT NOT VALID"),
  /**
   * 댓글을 찾을 수 없음
   */
  COMMENT_NOT_FOUND(404, "COMMENT.ERR.404", "COMMENT NOT FOUND"),
  /**
   * 댓글 추가 실패
   */
  COMMENT_INSERT_FAIL(500, "COMMENT.ERR.500.ACTION.INSERT", "COMMENT INSERT FAIL"),
  ;

  /**
   * HTTP 상태 코드
   */
  private int status;
  /**
   * 정의된 에러 코드
   */
  private String errorCode;
  /**
   * 정의된 에러 메시지
   */
  private String message;
}