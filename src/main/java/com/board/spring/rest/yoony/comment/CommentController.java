package com.board.spring.rest.yoony.comment;

import com.board.spring.rest.yoony.article.ArticleService;
import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.CustomExceptionView;
import com.board.spring.rest.yoony.error.ErrorCode;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 설명
 *
 * @author YK
 * @version 1.0
 * @fileName CommentController
 * @since 2023-02-28
 */
@RestController
@RequestMapping("/v1/articles/{articleId}/comments")
public class CommentController {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  @Autowired
  ArticleService articleService;
  @Autowired
  CommentService commentService;
  @PostMapping
  public ResponseEntity createComment(@PathVariable long articleId, @RequestBody CommentDTO commentDTO)
      throws CustomException, Exception {
    if (articleId == 0) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_ID_NOT_VALID);
    }
    if (articleService.isArticleExist(articleId) == false) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_NOT_FOUND);
    }

    if (commentDTO.isContentValid() == false) {
      throw new CustomExceptionView(ErrorCode.COMMENT_CONTENT_NOT_VALID);
    }
    commentDTO.setArticleId(articleId);

    int result = commentService.insertComment(commentDTO);
    return new ResponseEntity(HttpStatus.CREATED);
  }

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
