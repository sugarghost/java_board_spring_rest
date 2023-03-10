package com.board.spring.rest.yoony.file;

import com.board.spring.rest.yoony.article.ArticleService;
import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.CustomExceptionView;
import com.board.spring.rest.yoony.error.ErrorCode;
import com.board.spring.rest.yoony.validation.article.ArticleIdValidation;
import com.board.spring.rest.yoony.validation.file.FileIdValidation;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 파일 도메인을 처리하는 컨트롤러
 * <p> /v1/articles/{articleId}/files 요청을 처리함
 *
 * @author YK
 * @version 1.0
 * @fileName CommentController
 * @since 2023-03-04
 */
@RestController
@RequestMapping("/v1/articles/{articleId}/files")
public class FileController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  FileService fileService;

  @Autowired
  ArticleService articleService;

  /**
   * 특정 게시물의 파일을 리스트를 조회하는 RequestMapping
   * <p> /v1/articles/{articleId}/files GET 요청을 처리함
   * @param articleId
   * @return ResponseEntity 성공시 HttpStatus.OK와 파일 리스트 반환
   * @throws CustomException
   * @throws Exception
   * @see FileService#selectFileList(long)
   * @see ArticleService#isArticleExist(long)
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   */
  @GetMapping
  public ResponseEntity getFileList(@PathVariable long articleId)
      throws CustomException, Exception {

    if (articleId == 0) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_ID_NOT_VALID);
    }
    if (articleService.isArticleExist(articleId) == false) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_NOT_FOUND);
    }

    List<FileDTO> fileList = fileService.selectFileList(articleId);
    return ResponseEntity.ok(fileList);
  }

  /**
   * 특정 게시물의 특정 파일을 다운로드 하기 위한 RequestMapping
   * <p> /v1/articles/{articleId}/files/{fileId} GET 요청을 처리함
   * <p> 파일 다운로드시 Range 처리를 위해 요청 헤더를 받아서 처리함(사용은 안함)
   *
   * @param fileId 파일 아이디
   * @param articleId 게시물 아이디
   * @param headers 다운로드 처리를 위한 요청 헤더
   * @return
   * @throws CustomException
   * @throws Exception
   * @see FileService#downloadFile(FileDTO, List)
   * @see ArticleService#isArticleExist(long)
   * @see FileService#selectFile(FileDTO)
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   */
  @GetMapping("/{fileId}")
  public ResponseEntity getFile(@FileIdValidation @PathVariable long fileId, @ArticleIdValidation @PathVariable long articleId,
      @RequestHeader HttpHeaders headers)
      throws CustomException, Exception {
    FileDTO fileDTO = new FileDTO();
    fileDTO.setFileId(fileId);
    fileDTO.setArticleId(articleId);
    FileDTO targetFileDTO = fileService.selectFile(fileDTO);

    return fileService.downloadFile(targetFileDTO, headers.get(HttpHeaders.RANGE));
  }

}
