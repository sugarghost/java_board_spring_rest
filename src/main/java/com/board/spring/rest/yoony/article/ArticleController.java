package com.board.spring.rest.yoony.article;

import static org.springframework.http.ResponseEntity.status;

import com.board.spring.rest.yoony.article.page.PageDTO;
import com.board.spring.rest.yoony.article.search.SearchDTO;
import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.CustomExceptionView;
import com.board.spring.rest.yoony.error.ErrorCode;
import com.board.spring.rest.yoony.util.Security;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 설명
 *
 * @author YK
 * @version 1.0
 * @fileName ArticleController
 * @since 2023-02-28
 */
@RestController
@RequestMapping("/v1/articles")
public class ArticleController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  ArticleService articleService;

  @PostMapping
  public ResponseEntity createArticle(@RequestBody ArticleDTO articleDTO)
      throws CustomException, Exception {

    if (articleDTO.isInsertArticleValid() == false) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_INSERT_NOT_VALID);
    }
    articleService.insertArticle(articleDTO);

    return new ResponseEntity(HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity getArticleList(@ModelAttribute SearchDTO searchDTO)
      throws CustomException, Exception {
    int totalCount = articleService.selectArticleCount(searchDTO);

    List<ArticleDTO> articleList = articleService.selectArticleList(searchDTO);
    if (articleList == null) {
      return status(HttpStatus.NO_CONTENT).body(null);
    }

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-Total-Count", String.valueOf(totalCount));
    return new ResponseEntity(articleList, headers, HttpStatus.OK);
  }

  @GetMapping("/{articleId}")
  public ResponseEntity getArticle(@PathVariable long articleId)
      throws CustomException, Exception {
    if (articleId == 0) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_ID_NOT_VALID);
    }
    ArticleDTO articleDTO = articleService.selectArticle(articleId);
    if (articleDTO.getContent() == null) {
      return status(HttpStatus.NO_CONTENT).body(null);
    }

    articleService.updateArticleHit(articleId);

    return ResponseEntity.ok(articleDTO);
  }

  @PutMapping("/{articleId}")
  public ResponseEntity updateArticle(@PathVariable long articleId,
      @RequestBody ArticleDTO articleDTO)
      throws CustomException, Exception {
    if (articleId == 0) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_ID_NOT_VALID);
    }
    if (articleService.isArticleExist(articleId) == false) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_NOT_FOUND);
    }
    articleDTO.setArticleId(articleId);
    if (articleDTO.isUpdateArticleValid() == false) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_UPDATE_NOT_VALID);
    }
    articleDTO.setPassword(Security.sha256Encrypt(articleDTO.getPassword()));

    if (articleService.isPasswordCorrect(articleDTO) == false) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_PASSWORD_NOT_VALID);
    }

    int result = articleService.updateArticle(articleDTO);

    return ResponseEntity.ok("Hello World");
  }

  @DeleteMapping("/{articleId}")
  public ResponseEntity deleteArticle(@PathVariable long articleId)
      throws CustomException, Exception {

    return ResponseEntity.ok("Hello World");
  }
}
