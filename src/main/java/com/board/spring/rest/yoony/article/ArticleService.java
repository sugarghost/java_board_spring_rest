package com.board.spring.rest.yoony.article;

import com.board.spring.rest.yoony.article.search.SearchDTO;
import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.ErrorCode;
import com.board.spring.rest.yoony.file.FileDTO;
import com.board.spring.rest.yoony.file.FileMapper;
import com.board.spring.rest.yoony.file.FileProperty;
import com.board.spring.rest.yoony.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * article 연관된 데이터 처리를 위한 서비스
 *
 * @author YK
 * @version 1.0
 * @fileName ArticleService
 * @see ArticleController
 * @see ArticleMapper
 * @since 2023-03-04
 */
@Service
public class ArticleService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ArticleMapper articleMapper;

  @Autowired
  private FileMapper fileMapper;
  @Autowired
  private FileProperty fileProperty;

  /**
   * 게시글을 생성하는 메소드(비활성화) 현재 사용 로직은 File을 같이 생성하는 로직만 사용하기 때문에 비활성화
   *
   * @param articleDTO 게시글 정보를 담은 DTO
   * @return int 형태의 생성된 게시글의 수
   * @throws CustomException (게시글 생성 실패시 발생)
   * @author YK
   * @version 1.0
   * @see ArticleMapper#insertArticle(ArticleDTO)
   * @since 2023-03-04
   */
  public int insertArticle(ArticleDTO articleDTO) {
    int result = articleMapper.insertArticle(articleDTO);
    if (result < 1) {
      throw new CustomException(ErrorCode.ARTICLE_INSERT_FAIL);
    }
    return result;
  }

  /**
   * 게시글을 생성하는 메소드
   *
   * @param articleDTO 게시글 정보를 담은 DTO
   * @param files      게시글에 첨부된 파일들
   * @return int 형태의 생성된 게시글의 수
   * @throws CustomException (게시글 생성 실패시 발생)
   * @author YK
   * @version 1.0
   * @see ArticleMapper#insertArticle(ArticleDTO)
   * @see FileUtil#uploadFile(MultipartFile, String)
   * @see FileMapper#insertFile(FileDTO)
   * @since 2023-03-04
   */
  @Transactional
  public int insertArticleAndFiles(ArticleDTO articleDTO, MultipartFile[] files)
      throws Exception {

    int result = articleMapper.insertArticle(articleDTO);
    if (result < 1) {
      throw new CustomException(ErrorCode.ARTICLE_INSERT_FAIL);
    }

    if (files != null) {
      for (MultipartFile file : files) {
        if (file.isEmpty()) {
          continue;
        }

        FileDTO fileDTO = FileUtil.uploadFile(file, fileProperty.getUploadPath());
        fileDTO.setArticleId(articleDTO.getArticleId());

        int fileInsertResult = fileMapper.insertFile(fileDTO);
        if (fileInsertResult < 1) {
          throw new CustomException(ErrorCode.FILE_INSERT_FAIL);
        }
      }
    }

    return result;
  }

  /**
   * 게시글 수를 조회하는 메소드
   *
   * @param searchDTO 검색 조건을 담은 DTO
   * @return int 형태의 게시글 수
   * @author YK
   * @version 1.0
   * @see ArticleMapper#selectArticleCount(SearchDTO)
   * @since 2023-03-04
   */
  public int selectArticleCount(SearchDTO searchDTO) {
    int result = articleMapper.selectArticleCount(searchDTO);
    return result;
  }

  /**
   * 게시글 리스트를 조회하는 메소드
   *
   * @param searchDTO 검색 조건을 담은 DTO
   * @return List 형태의 게시글 리스트
   * @author YK
   * @version 1.0
   * @see ArticleMapper#selectArticleList(SearchDTO)
   * @since 2023-03-04
   */
  public List<ArticleDTO> selectArticleList(SearchDTO searchDTO) {
    List<ArticleDTO> articleList = articleMapper.selectArticleList(searchDTO);
    return articleList;
  }

  /**
   * 게시글을 조회하는 메소드
   *
   * @param articleId 조회할 게시글의 ID
   * @return ArticleDTO 형태의 게시글 정보
   * @throws CustomException (게시글이 존재하지 않을 경우 발생)
   * @author YK
   * @version 1.0
   * @see ArticleMapper#selectArticle(long)
   * @since 2023-03-04
   */
  public ArticleDTO selectArticle(long articleId) {
    ArticleDTO articleDTO = articleMapper.selectArticle(articleId);
    if (articleDTO == null) {
      throw new CustomException(ErrorCode.ARTICLE_NOT_FOUND);
    }
    return articleDTO;
  }

  /**
   * 게시글 존재 여부 확인 메소드
   *
   * @param articleId 조회할 게시글의 ID
   * @return boolean 게시글 존재 여부
   * @author YK
   * @version 1.0
   * @see ArticleMapper#isArticleExist(long)
   * @since 2023-03-04
   */
  public boolean isArticleExist(long articleId) {
    boolean result = articleMapper.isArticleExist(articleId);
    return result;
  }

  /**
   * 패스워드의 일치 여부를 확인하는 메소드
   *
   * @param articleDTO 일치 여부를 확인할 articleId와 password를 담은 DTO
   * @return boolean 패스워드 일치 여부
   * @throws CustomException (패스워드가 일치하지 않을 경우 발생)
   * @author YK
   * @version 1.0
   * @see ArticleMapper#isPasswordCorrect(ArticleDTO)
   * @since 2023-03-04
   */
  public boolean isPasswordCorrect(ArticleDTO articleDTO) {
    if (!articleMapper.isPasswordCorrect(articleDTO)) {
      throw new CustomException(ErrorCode.ARTICLE_PASSWORD_NOT_VALID);
    }
    return true;
  }

  /**
   * 게시글 수정을 위한 메소드 트랜잭션을 위해 파일에 대한 삭제와 추가를 같이 수행함
   *
   * @param articleDTO  수정할 게시글 정보를 담은 DTO
   * @param deleteFiles 삭제할 파일의 ID를 담은 배열
   * @param files       수정할 게시글에 첨부할 파일들
   * @return int 수정된 게시글의 수
   * @throws Exception
   * @author YK
   * @version 1.0
   * @see ArticleMapper#updateArticle(ArticleDTO)
   * @see FileMapper#deleteFile(FileDTO)
   * @see FileUtil#uploadFile(MultipartFile, String)
   * @see FileMapper#insertFile(FileDTO)
   * @see CustomException (게시글 수정 실패, 파일 삭제 실패, 파일 추가 실패시 발생)
   * @since 2023-03-04
   */
  @Transactional
  public int updateArticleAndFiles(ArticleDTO articleDTO, String[] deleteFiles,
      MultipartFile[] files)
      throws Exception {
    int result = articleMapper.updateArticle(articleDTO);
    if (result < 1) {
      throw new CustomException(ErrorCode.ARTICLE_UPDATE_FAIL);
    }
    if (deleteFiles != null) {
      for (String deleteFileId : deleteFiles) {
        FileDTO deleteFileDTO = new FileDTO();
        deleteFileDTO.setFileId(Integer.parseInt(deleteFileId));
        deleteFileDTO.setArticleId(articleDTO.getArticleId());

        int fileDeleteResult = fileMapper.deleteFile(deleteFileDTO);
        if (fileDeleteResult < 1) {
          throw new CustomException(ErrorCode.FILE_DELETE_FAIL);
        }
      }
    }

    if (files != null) {
      for (MultipartFile file : files) {
        if (file.isEmpty()) {
          continue;
        }

        FileDTO fileDTO = FileUtil.uploadFile(file, fileProperty.getUploadPath());
        fileDTO.setArticleId(articleDTO.getArticleId());

        int fileInsertResult = fileMapper.insertFile(fileDTO);
        if (fileInsertResult < 1) {
          throw new CustomException(ErrorCode.FILE_INSERT_FAIL);
        }
      }
    }

    return result;
  }

  /**
   * 게시글 수정을 위한 메소드(비활성화) 파일 삭제 및 수정을 진행하는 메소드를 사용하기에 사용하지 않음
   *
   * @param articleDTO 수정할 게시글 정보를 담은 DTO
   * @return int 수정된 게시글의 수
   * @throws CustomException (게시글 수정 실패시 발생)
   * @author YK
   * @version 1.0
   * @see ArticleMapper#updateArticle(ArticleDTO)
   * @since 2023-03-04
   */
  public int updateArticle(ArticleDTO articleDTO) {
    int result = articleMapper.updateArticle(articleDTO);
    if (result < 1) {
      throw new CustomException(ErrorCode.ARTICLE_UPDATE_FAIL);
    }
    return result;
  }

  /**
   * 게시물 조회시 조회수를 증가시키는 메소드
   *
   * @param articleId 조회수를 증가시킬 게시물의 ID
   * @return int 증가된 게시물의 수
   * @author YK
   * @version 1.0
   * @since 2023-03-04
   */
  public int updateArticleHit(long articleId) {
    int result = articleMapper.updateArticleHit(articleId);
    return result;
  }

  /**
   * 게시글 삭제를 위한 메소드
   *
   * @param articleDTO 삭제할 게시글의 ID와 비밀번호를 담은 DTO
   * @return int 삭제된 게시글의 수
   * @throws CustomException (게시글 삭제 실패시 발생)
   * @author YK
   * @version 1.0
   * @see ArticleMapper#deleteArticle(ArticleDTO)
   * @since 2023-03-04
   */
  public int deleteArticle(ArticleDTO articleDTO) {
    int result = articleMapper.deleteArticle(articleDTO);
    if (result < 1) {
      throw new CustomException(ErrorCode.ARTICLE_DELETE_FAIL);
    }
    return result;
  }
}
