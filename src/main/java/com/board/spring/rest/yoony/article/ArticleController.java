package com.board.spring.rest.yoony.article;

import static org.springframework.http.ResponseEntity.status;

import com.board.spring.rest.yoony.article.search.SearchDTO;
import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.CustomExceptionView;
import com.board.spring.rest.yoony.error.ErrorCode;
import com.board.spring.rest.yoony.util.Security;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * article 도메인을 처리하는 컨트롤러
 *
 * @author YK
 * @version 1.0
 * @fileName ArticleController
 * @since 2023-03-04
 * @see ArticleService
 */
@RestController
@RequestMapping("/v1/articles")
public class ArticleController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  ArticleService articleService;

  /**
   * 게시글을 생성하는 RequestMapping
   * /v1/articles POST 요청을 처리함
   * File 처리를 위해 @RequestPart를 사용함
   * @param articleDTOJson ArticleDTO를 json으로 변환한 String
   * @param files MultipartFile[] 형태의 파일 리스트
   * @return ResponseEntity 형태의 응답(성공시 HttpStatus.CREATED) 반환
   * @throws CustomException (유효성 검사 실패시 발생)
   * @throws Exception
   * @see ArticleService#insertArticleAndFiles(ArticleDTO, MultipartFile[])
   */
  @PostMapping
  public ResponseEntity createArticle(@RequestPart("articleDTO") String articleDTOJson,
      @RequestPart(value = "files", required = false) MultipartFile[] files)
      throws CustomException, Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    ArticleDTO articleDTO = objectMapper.readValue(articleDTOJson, ArticleDTO.class);
  // TODO: RequestPart나 Json parse없이 바로 던지는 법 찾아보기
    if (articleDTO.isInsertArticleValid() == false) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_INSERT_NOT_VALID);
    }
    articleDTO.setPassword(Security.sha256Encrypt(articleDTO.getPassword()));

    articleService.insertArticleAndFiles(articleDTO, files);

    return new ResponseEntity(HttpStatus.CREATED);
  }


  /**
   * 게시글목록을 가져오는 RequestMapping
   * /v1/articles GET 요청을 처리함
   * 전체 게시물 수를 반환하기 위해 X-Total-Count 헤더에 값을 담아서 반환함
   * @param searchDTO 검색조건을 담은 DTO
   * @return ResponseEntity 성공 시 HttpStatus.OK, 게시물이 없으면 HttpStatus.NO_CONTENT 반환
   * @throws CustomException
   * @throws Exception
   * @see ArticleService#selectArticleCount(SearchDTO)
   * @see ArticleService#selectArticleList(SearchDTO)
   *
   */
  @GetMapping
  public ResponseEntity getArticleList(@ModelAttribute SearchDTO searchDTO)
      throws CustomException, Exception {

    int totalCount = articleService.selectArticleCount(searchDTO);
    // TODO: DTO를 반환하면 내부에 모든 메소드 또한(Getter) 계산이 되서 반환이 되기 때문에 Validation은 따로 다 빼는게 좋음
    // DTO는 가급적 하나로 통치기(안되면 어쩔 수 없지만)
    // 문서 만드는 라이브러리들은 기본적으로 DTO 기반으로 문서를 뽑아줌
    // 조회용으로 DTO로 뽑으면 쓰지도 않는 컬럼이 너무 많아서 문서가 복잡해지는 경우가 있어 따로 빼는 경우가 있음
    // 쓰는것만 만들고자 하면 다 만들어야하고, 복잡해질수도 있고 정답은 없음
    // 지금은 일단 통일성 있게 하기
    List<ArticleDTO> articleList = articleService.selectArticleList(searchDTO);
    if (articleList == null) {
      return status(HttpStatus.NO_CONTENT).body(null);
    }
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-Total-Count", String.valueOf(totalCount));
    return new ResponseEntity(articleList, headers, HttpStatus.OK);
  }

  /**
   * 게시글을 가져오는 RequestMapping
   * /v1/articles/{articleId} GET 요청을 처리함
   * @param articleId 게시글 번호
   * @return ResponseEntity 성공 시 HttpStatus.OK, 게시물이 없으면 HttpStatus.NO_CONTENT 반환
   * @throws CustomException (유효성 검사 실패시 발생)
   * @throws Exception
   * @see ArticleService#selectArticle(long)
   * @see ArticleService#updateArticleHit(long)
   */
  // TODO: 현재 프로젝트 구성에는 적합하지만, 규모가 커지면 분리가 필요할 것 같음
  @GetMapping("/{articleId}")
  public ResponseEntity getArticle(@PathVariable long articleId)
      throws CustomException, Exception {
    if (articleId == 0) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_ID_NOT_VALID);
    }
    // TODO: 일반적인 예외 상황은 넘어가거나, 전체적으로 전역처리 고려
    ArticleDTO articleDTO = articleService.selectArticle(articleId);
    if (articleDTO.getContent() == null) {
      return status(HttpStatus.NO_CONTENT).body(null);
    }

    articleService.updateArticleHit(articleId);

    return ResponseEntity.ok(articleDTO);
  }

  /**
   * 게시글을 수정하는 RequestMapping
   * /v1/articles/{articleId} PUT 요청을 처리함
   * File 처리를 위해 @RequestPart를 사용함
   * @param articleDTOJson ArticleDTO를 json으로 변환한 String
    * @param deleteFiles 삭제할 파일 리스트
    * @param files MultipartFile[] 등록할 파일 리스트
   * @param articleId 게시글 번호
   * @return ResponseEntity 성공 시 HttpStatus.NO_CONTENT 반환
   * @throws CustomException
   * @throws Exception
   * @see ArticleService#isArticleExist(long)
   * @see ArticleService#isPasswordCorrect(ArticleDTO)
   * @see ArticleService#updateArticleAndFiles(ArticleDTO, String[], MultipartFile[])
   *
   */
  @PutMapping("/{articleId}")
  public ResponseEntity updateArticle(@PathVariable long articleId,
      @RequestPart("articleDTO") String articleDTOJson,
      @RequestParam(value = "deleteFiles",required = false) String[] deleteFiles,
      @RequestPart(value = "files",required = false) MultipartFile[] files)
      throws CustomException, Exception {

    ObjectMapper objectMapper = new ObjectMapper();
    ArticleDTO articleDTO = objectMapper.readValue(articleDTOJson, ArticleDTO.class);
    // TODO: 중요 예외만 잡고 처리하는게 가독성 측면에서 좋아보임
    // TODO: 많은 고민을 했음을 보여주고 싶다면 Validator를 따로 빼서 만들기

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

    int result = articleService.updateArticleAndFiles(articleDTO, deleteFiles, files);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  /**
   * 게시글을 삭제하는 RequestMapping
   * /v1/articles/{articleId} DELETE 요청을 처리함
   * @param articleId 게시글 번호
   * @param articleDTO 비밀번호를 담은 DTO
   * @return ResponseEntity 성공 시 HttpStatus.NO_CONTENT 반환
   * @throws CustomException
   * @throws Exception
   * @see ArticleService#isArticleExist(long)
   * @see ArticleService#isPasswordCorrect(ArticleDTO)
   * @see ArticleService#deleteArticle(ArticleDTO)
   */
  @DeleteMapping("/{articleId}")
  public ResponseEntity deleteArticle(@PathVariable long articleId, @RequestBody ArticleDTO articleDTO)
      throws CustomException, Exception {

    if (articleId == 0) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_ID_NOT_VALID);
    }
    if (articleService.isArticleExist(articleId) == false) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_NOT_FOUND);
    }
    articleDTO.setArticleId(articleId);
    articleDTO.setPassword(Security.sha256Encrypt(articleDTO.getPassword()));

    if (articleService.isPasswordCorrect(articleDTO) == false) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_PASSWORD_NOT_VALID);
    }

    int deleteResult = articleService.deleteArticle(articleDTO);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
}
