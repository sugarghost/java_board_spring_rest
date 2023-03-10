package com.board.spring.rest.yoony.comment;

import java.sql.Timestamp;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * 댓글 DTO
 *
 * @author yoony
 * @version 1.0
 * @see CommentMapper
 * @since 2023. 02. 26.
 */
@Data
@Alias("CommentDTO")
public class CommentDTO {

  /**
   * 댓글 id(auto increment)
   */
  private long commentId;
  /**
   * 게시글 id(foreign key: article table)
   */
  private long articleId;
  /**
   * 내용
   */
  @NotEmpty(message = "내용을 입력해 주세요.")
  @Pattern(regexp = "^.{1,255}$", message = "내용을 255자 이하로 입력해 주세요.")
  private String content;
  /**
   * 작성일(current_timestamp())
   */
  private Timestamp createdDate;

}
