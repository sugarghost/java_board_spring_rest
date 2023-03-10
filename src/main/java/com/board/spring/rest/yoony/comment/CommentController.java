package com.board.spring.rest.yoony.comment;

import com.board.spring.rest.yoony.article.ArticleService;
import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.CustomExceptionView;
import com.board.spring.rest.yoony.error.ErrorCode;
import com.board.spring.rest.yoony.validation.article.ArticleIdValidation;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * comment 도메인을 처리하는 컨트롤러
 * <p> /v1/articles/{articleId}/comments 요청을 처리함
 *
 * @author YK
 * @version 1.0
 * @fileName CommentController
 * @since 2023-03-04
 */
@RestController
@RequestMapping("/v1/articles/{articleId}/comments")
public class CommentController {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  @Autowired
  ArticleService articleService;
  @Autowired
  CommentService commentService;
  /**
   * 댓글을 생성하는 RequestMapping
   * <p> /v1/articles/{articleId}/comments POST 요청을 처리함
   * @param articleId 댓글을 생성할 게시글의 id
   * @param commentDTO 댓글 정보를 담은 DTO
   * @return ResponseEntity 형태의 응답(성공시 HttpStatus.CREATED) 반환
   * @throws CustomException (유효성 검사 실패시 발생)
   * @throws Exception
   * @see CommentService#insertComment(CommentDTO)
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   */
  @PostMapping
  public ResponseEntity createComment(@ArticleIdValidation @PathVariable long articleId, @Validated @RequestBody CommentDTO commentDTO, Errors errors)
      throws CustomException, Exception {

    if (errors.hasErrors()) {
      throw new CustomExceptionView(ErrorCode.COMMENT_CONTENT_NOT_VALID);
    }
    commentDTO.setArticleId(articleId);

    int result = commentService.insertComment(commentDTO);
    return new ResponseEntity(HttpStatus.CREATED);
  }

  /**
   * 댓글 목록을 가져오는 RequestMapping
   * <p> /v1/articles/{articleId}/comments GET 요청을 처리함
   * @param articleId 댓글을 가져올 게시글의 id
   * @return ResponseEntity 형태의 응답(성공시 HttpStatus.OK) 반환
   * @throws CustomException (유효성 검사 실패시 발생)
   * @throws Exception
   * @see CommentService#selectCommentList(long)
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   */
  @GetMapping
  public ResponseEntity getCommentList(@PathVariable long articleId)
      throws CustomException, Exception {

    if (articleId == 0) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_ID_NOT_VALID);
    }
    if (articleService.isArticleExist(articleId) == false) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_NOT_FOUND);
    }
    List<CommentDTO> commentList = commentService.selectCommentList(articleId);
    return ResponseEntity.ok(commentList);
  }
}
