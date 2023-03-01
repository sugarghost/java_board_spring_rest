package com.board.spring.rest.yoony.comment;

import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.ErrorCode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 설명
 *
 * @author YK
 * @version 1.0
 * @fileName CommentService
 * @since 2023-02-28
 */
@Service
public class CommentService {
  @Autowired
  private CommentMapper commentMapper;

  public int insertComment(CommentDTO commentDTO) {
    int result = commentMapper.insertComment(commentDTO);
    if( result < 1 ) {
      throw new CustomException(ErrorCode.COMMENT_INSERT_FAIL);
    }
    return result;
  }

  public List<CommentDTO> selectCommentList(long articleId) {
    List<CommentDTO> commentList = commentMapper.selectCommentList(articleId);
    return commentList;
  }
}
