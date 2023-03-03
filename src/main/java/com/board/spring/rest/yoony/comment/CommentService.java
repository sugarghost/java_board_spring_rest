package com.board.spring.rest.yoony.comment;

import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.ErrorCode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * comment 연관된 데이터 처리를 위한 서비스
 *
 * @author YK
 * @version 1.0
 * @fileName CommentService
 * @since 2023-03-04
 */
@Service
public class CommentService {
  @Autowired
  private CommentMapper commentMapper;

  /**
   * 댓글을 등록하는 메소드
   * @param commentDTO 댓글 정보를 담은 DTO
   * @return int 형태의 생성된 댓글의 수
   * @throws CustomException (댓글 생성 실패시 발생)
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   */
  public int insertComment(CommentDTO commentDTO) {
    int result = commentMapper.insertComment(commentDTO);
    if( result < 1 ) {
      throw new CustomException(ErrorCode.COMMENT_INSERT_FAIL);
    }
    return result;
  }

  /**
   * 댓글 목록을 가져오는 서비스
   * @param articleId 댓글 목록 가져올 게시글 번호
   * @return  List<CommentDTO> 형태의 댓글 목록
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   */
  public List<CommentDTO> selectCommentList(long articleId) {
    List<CommentDTO> commentList = commentMapper.selectCommentList(articleId);
    return commentList;
  }
}
