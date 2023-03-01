package com.board.spring.rest.yoony.comment;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * Comment 관련 DAO 인터페이스
 * <p>mapper/CommentMapper.xml에 정의된 쿼리를 실행하기 위한 인터페이스
 *
 * @author yoony
 * @version 1.0
 * @see CommentDTO
 * @since 2023. 02. 26.
 */
@Mapper
public interface CommentMapper {

  /**
   * 댓글을 등록하는 메소드
   *
   * @param commentDTO 게시글 정보를 담은 DTO
   *                   <p>commentId는 Auto Increment이므로 입력하지 않음
   *                   <p>createdDate는 current_timestamp로 입력되므로 입력하지 않음
   * @return int 등록된 댓글의 개수
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  int insertComment(CommentDTO commentDTO);

  /**
   * 댓글 목록을 가져오는 메소드
   *
   * @param articleId 댓글 가져올 대상 게시글 id
   * @return 조회된 댓글 목록을 담은 {@link CommentDTO} 리스트({@link List})
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  List<CommentDTO> selectCommentList(long articleId);
}
