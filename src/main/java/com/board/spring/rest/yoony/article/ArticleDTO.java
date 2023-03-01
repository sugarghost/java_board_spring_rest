package com.board.spring.rest.yoony.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Date;
import java.sql.Timestamp;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 게시글 DTO 클래스
 *
 * @author yoony
 * @version 1.0
 * @see ArticleMapper
 * @since 2023. 02. 26.
 */
@Data
@Alias("ArticleDTO")
public class ArticleDTO {

  /**
   * 게시글 ID(auto increment)
   */
  private long articleId;
  /**
   * 카테고리 ID(foreign key: category table)
   */
  private long categoryId;
  /**
   * 카테고리 이름(DB상에 컬럼은 존재하지 않고 DTO에만 존재)
   */
  private String categoryName;
  /**
   * 작성자(3~4자)
   */
  private String writer;
  /**
   * 비밀번호(4~15자, 영문, 숫자, 특수문자 조합)
   */
  private String password;
  /**
   * 제목(4~99자)
   */
  private String title;
  /**
   * 내용(4~1999자)
   */
  private String content;
  /**
   * 조회수
   */
  private int viewCount;
  /**
   * 파일 존재 여부(DB상에 컬럼은 존재하지 않고 DTO에만 존재) boolean으로 잡으니깐 JSP에서 인식을 못함
   */
  private Boolean isFileExist;
  /**
   * 작성일(current_timestamp())
   */
  private Timestamp createdDate;
  /**
   * 수정일
   */
  private Timestamp modifiedDate;

  /**
   * 작성자가 유효한지 검사 null 체크, isEmpty 체크 정규식 검사: 3~4자
   *
   * @return 유효하면 true, 아니면 false
   */
  public boolean isWriterValid() {
    return writer != null && !writer.isEmpty() && writer.matches("^.{3,4}$");
  }

  /**
   * 비밀번호가 유효한지 검사 null 체크, isEmpty 체크 정규식 검사: 영문, 숫자, 특수문자 조합으로 4~15자
   *
   * @return 유효하면 true, 아니면 false
   */
  public boolean isPasswordValid() {
    return password != null && !password.isEmpty() && password.matches(
        "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{4,15}$");
  }

  /**
   * 제목이 유효한지 검사 null 체크, isEmpty 체크 정규식 검사: 4~99자
   *
   * @return 유효하면 true, 아니면 false
   */
  public boolean isTitleValid() {
    return title != null && !title.isEmpty() && title.matches("^.{4,99}$");
  }

  /**
   * 내용이 유효한지 검사 null 체크, isEmpty 체크 정규식 검사: 4~1999자
   *
   * @return 유효하면 true, 아니면 false
   */
  public boolean isContentValid() {
    return content != null && !content.isEmpty() && content.matches("^.{4,1999}$");
  }

  /**
   * 작성일이 유효한지 검사 null 체크
   *
   * @return 유효하면 true, 아니면 false
   */
  public boolean isCreatedDateValid() {
    return createdDate != null;
  }

  /**
   * 수정일이 유효한지 검사 null 체크
   *
   * @return 유효하면 true, 아니면 false
   */
  public boolean isModifiedDateValid() {
    return modifiedDate != null;
  }

  /**
   * 게시글 등록이 유효한지 검사
   *
   * @return 유효하면 true, 아니면 false
   * @see #isWriterValid() #isWriterValid()#isWriterValid()
   * @see #isPasswordValid() #isPasswordValid()#isPasswordValid()
   * @see #isTitleValid() #isTitleValid()#isTitleValid()
   * @see #isContentValid() #isContentValid()#isContentValid()
   */
  public boolean isInsertArticleValid() {
    return isWriterValid()
        && isPasswordValid()
        && isTitleValid()
        && isContentValid();
  }

  /**
   * 게시글 수정이 유효한지 검사
   *
   * @return 유효하면 true, 아니면 false
   * @see #isWriterValid() #isWriterValid()#isWriterValid()
   * @see #isPasswordValid() #isPasswordValid()#isPasswordValid()
   * @see #isTitleValid() #isTitleValid()#isTitleValid()
   * @see #isContentValid() #isContentValid()#isContentValid()
   */
  public boolean isUpdateArticleValid() {
    return isWriterValid()
        && isPasswordValid()
        && isTitleValid()
        && isContentValid();
  }

  /**
   * 파일 존재 여부를 반환
   */
  public boolean isFileExist() {
    return isFileExist;
  }
}
