package com.board.spring.rest.yoony.comment;

import java.sql.Timestamp;
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
  private String content;
  /**
   * 작성일(current_timestamp())
   */
  private Timestamp createdDate;

  /**
   * 댓글 내용이 유효한지 검사하는 메소드
   *
   * @return 댓글 내용이 유효하면 true, 유효하지 않으면 false
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  public boolean isContentValid() {
    return content != null && !content.isEmpty() && content.matches("^.{1,255}$");
  }
}
