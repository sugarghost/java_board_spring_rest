package com.board.spring.rest.yoony.article;

import com.board.spring.rest.yoony.validation.article.ArticleIdValidation;
import com.board.spring.rest.yoony.validation.article.ArticleValidationGroups;
import com.board.spring.rest.yoony.validation.article.ContentValidation;
import com.board.spring.rest.yoony.validation.article.PasswordValidation;
import com.board.spring.rest.yoony.validation.article.TitleValidation;
import com.board.spring.rest.yoony.validation.article.WriterValidation;
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
  @ArticleIdValidation(groups = {ArticleValidationGroups.UpdateArticleGroup.class,
      ArticleValidationGroups.DeleteArticleGroup.class})
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
  @WriterValidation(groups = {ArticleValidationGroups.InsertArticleGroup.class,
      ArticleValidationGroups.UpdateArticleGroup.class})
  private String writer;
  /**
   * 비밀번호(4~15자, 영문, 숫자, 특수문자 조합)
   */
  @PasswordValidation(groups = {ArticleValidationGroups.InsertArticleGroup.class,
      ArticleValidationGroups.UpdateArticleGroup.class,
      ArticleValidationGroups.DeleteArticleGroup.class})
  private String password;
  /**
   * 제목(4~99자)
   */
  @TitleValidation(groups = {ArticleValidationGroups.InsertArticleGroup.class,
      ArticleValidationGroups.UpdateArticleGroup.class})
  private String title;
  /**
   * 내용(4~1999자)
   */
  @ContentValidation(groups = {ArticleValidationGroups.InsertArticleGroup.class,
      ArticleValidationGroups.UpdateArticleGroup.class})
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
}
