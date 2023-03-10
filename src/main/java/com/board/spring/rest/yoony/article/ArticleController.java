package com.board.spring.rest.yoony.article;

import static org.springframework.http.ResponseEntity.status;

import com.board.spring.rest.yoony.article.search.SearchDTO;
import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.ErrorCode;
import com.board.spring.rest.yoony.util.Security;
import com.board.spring.rest.yoony.validation.article.ArticleIdValidation;
import com.board.spring.rest.yoony.validation.article.ArticleValidationGroups;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * article 도메인을 처리하는 컨트롤러
 *
 * @author YK
 * @version 1.0
 * @fileName ArticleController
 * @see ArticleService
 * @since 2023-03-04
 */
@RestController
@RequestMapping("/v1/articles")
public class ArticleController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  ArticleService articleService;

  /**
   * 게시글을 생성하는 RequestMapping /v1/articles POST 요청을 처리함 File 처리를 위해 @RequestPart를 사용함
   *
   * @param articleDTO ArticleDTO 형태의 게시글 정보
   * @param files          MultipartFile[] 형태의 파일 리스트
   * @return ResponseEntity 형태의 응답(성공시 HttpStatus.CREATED) 반환
   * @throws CustomException (유효성 검사 실패시 발생)
   * @throws Exception
   * @see ArticleService#insertArticleAndFiles(ArticleDTO, MultipartFile[])
   */
  // TODO: 아토믹한 발리데이터는 되었지만, 그룹 발리데이션을 하기 위해서는 선언적으로 가는 것이 좋음
  // Validator란 어노테이션이 하나 있고, 값으로 어떤 그룹을 사용할 것인지를 정의하면 됨
  // {} 객체 형태로 fileds, rule등의 요소를 전달하는 방식으로 구현
  @PostMapping
  public ResponseEntity createArticle(
      @Validated(ArticleValidationGroups.InsertArticleGroup.class) @RequestPart("articleDTO") ArticleDTO articleDTO,
      @RequestPart(value = "files", required = false) MultipartFile[] files)
      throws CustomException, Exception {
    articleDTO.setPassword(Security.sha256Encrypt(articleDTO.getPassword()));

    articleService.insertArticleAndFiles(articleDTO, files);

    return new ResponseEntity(HttpStatus.CREATED);
  }


  /**
   * 게시글목록을 가져오는 RequestMapping /v1/articles GET 요청을 처리함 전체 게시물 수를 반환하기 위해 X-Total-Count 헤더에 값을 담아서
   * 반환함
   *
   * @param searchDTO 검색조건을 담은 DTO
   * @return ResponseEntity 성공 시 HttpStatus.OK, 게시물이 없으면 HttpStatus.NO_CONTENT 반환
   * @throws CustomException
   * @throws Exception
   * @see ArticleService#selectArticleCount(SearchDTO)
   * @see ArticleService#selectArticleList(SearchDTO)
   */
  @GetMapping
  public ResponseEntity getArticleList(@ModelAttribute SearchDTO searchDTO)
      throws CustomException, Exception {

    int totalCount = articleService.selectArticleCount(searchDTO);
    List<ArticleDTO> articleList = articleService.selectArticleList(searchDTO);
    if (articleList == null) {
      return status(HttpStatus.NO_CONTENT).body(null);
    }
    HttpHeaders headers = new HttpHeaders();
    // TODO: 헤더는 항상 특정 데이터(멀티파트 제외), 인증, 시크릿 등의 정보 즉 늘 요청에서 가지고 가야할 데이터가 들어가는 용도
    // 메타성 데이터가 들어가기 위한 용도
    // 요청에 대한 응답 데이터는 바디로 통치기
    /*
    APIResult apiResult = new APIResult();
    apiResult.set("list", list);
    apiResult.set("total", toa);
     */
    headers.set("X-Total-Count", String.valueOf(totalCount));
    return new ResponseEntity(articleList, headers, HttpStatus.OK);
  }

  /**
   * 게시글을 가져오는 RequestMapping /v1/articles/{articleId} GET 요청을 처리함
   *
   * @param articleId 게시글 번호
   * @return ResponseEntity 성공 시 HttpStatus.OK, 게시물이 없으면 HttpStatus.NO_CONTENT 반환
   * @throws CustomException (유효성 검사 실패시 발생)
   * @throws Exception
   * @see ArticleService#selectArticle(long)
   * @see ArticleService#updateArticleHit(long)
   */
  @GetMapping("/{articleId}")
  public ResponseEntity getArticle(@ArticleIdValidation @PathVariable long articleId)
      throws CustomException, Exception {

    ArticleDTO articleDTO = articleService.selectArticle(articleId);
    if (articleDTO.getContent() == null) {
      return status(HttpStatus.NO_CONTENT).body(null);
    }

    articleService.updateArticleHit(articleId);

    return ResponseEntity.ok(articleDTO);
  }

  /**
   * 게시글을 수정하는 RequestMapping /v1/articles/{articleId} PUT 요청을 처리함 File 처리를 위해 @RequestPart를 사용함
   *
   * @param articleDTO ArticleDTO 형태의 게시글 정보
   * @param deleteFiles    삭제할 파일 리스트
   * @param files          MultipartFile[] 등록할 파일 리스트
   * @param articleId      게시글 번호
   * @return ResponseEntity 성공 시 HttpStatus.NO_CONTENT 반환
   * @throws CustomException
   * @throws Exception
   * @see ArticleService#isPasswordCorrect(ArticleDTO)
   * @see ArticleService#updateArticleAndFiles(ArticleDTO, String[], MultipartFile[])
   */
  @PutMapping("/{articleId}")
  public ResponseEntity updateArticle(@ArticleIdValidation @PathVariable long articleId,
      @Validated(ArticleValidationGroups.UpdateArticleGroup.class) @RequestPart("articleDTO") ArticleDTO articleDTO,
      @RequestParam(value = "deleteFiles", required = false) String[] deleteFiles,
      @RequestPart(value = "files", required = false) MultipartFile[] files)
      throws CustomException, Exception {

    articleDTO.setPassword(Security.sha256Encrypt(articleDTO.getPassword()));

    if (articleService.isPasswordCorrect(articleDTO) == false) {
      throw new CustomException(ErrorCode.ARTICLE_PASSWORD_NOT_VALID);
    }

    int result = articleService.updateArticleAndFiles(articleDTO, deleteFiles, files);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  /**
   * 게시글을 삭제하는 RequestMapping /v1/articles/{articleId} DELETE 요청을 처리함
   *
   * @param articleId  게시글 번호
   * @param articleDTO 비밀번호를 담은 DTO
   * @return ResponseEntity 성공 시 HttpStatus.NO_CONTENT 반환
   * @throws CustomException
   * @throws Exception
   * @see ArticleService#isArticleExist(long)
   * @see ArticleService#isPasswordCorrect(ArticleDTO)
   * @see ArticleService#deleteArticle(ArticleDTO)
   */
  @DeleteMapping("/{articleId}")
  public ResponseEntity deleteArticle(@ArticleIdValidation @PathVariable long articleId,
      @Validated(ArticleValidationGroups.DeleteArticleGroup.class) @RequestBody ArticleDTO articleDTO)
      throws CustomException, Exception {

    articleDTO.setPassword(Security.sha256Encrypt(articleDTO.getPassword()));

    if (articleService.isPasswordCorrect(articleDTO) == false) {
      throw new CustomException(ErrorCode.ARTICLE_PASSWORD_NOT_VALID);
    }

    int deleteResult = articleService.deleteArticle(articleDTO);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
}
