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
 * 설명
 *
 * @author YK
 * @version 1.0
 * @fileName ArticleService
 * @since 2023-02-28
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

  public int insertArticle(ArticleDTO articleDTO) {
    int result = articleMapper.insertArticle(articleDTO);
    if (result < 1) {
      throw new CustomException(ErrorCode.ARTICLE_INSERT_FAIL);
    }
    return result;
  }

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

  public int selectArticleCount(SearchDTO searchDTO) {
    int result = articleMapper.selectArticleCount(searchDTO);
    return result;
  }

  public List<ArticleDTO> selectArticleList(SearchDTO searchDTO) {
    List<ArticleDTO> articleList = articleMapper.selectArticleList(searchDTO);
    return articleList;
  }

  public ArticleDTO selectArticle(long articleId) {
    ArticleDTO articleDTO = articleMapper.selectArticle(articleId);
    if (articleDTO == null) {
      throw new CustomException(ErrorCode.ARTICLE_NOT_FOUND);
    }
    return articleDTO;
  }

  public boolean isArticleExist(long articleId) {
    boolean result = articleMapper.isArticleExist(articleId);
    return result;
  }

  public boolean isPasswordCorrect(ArticleDTO articleDTO) {
    if (!articleMapper.isPasswordCorrect(articleDTO)) {
      throw new CustomException(ErrorCode.ARTICLE_PASSWORD_NOT_VALID);
    }
    return true;
  }
  @Transactional
  public int updateArticleAndFiles(ArticleDTO articleDTO, String[]  deleteFiles, MultipartFile[] files)
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

  public int updateArticle(ArticleDTO articleDTO) {
    int result = articleMapper.updateArticle(articleDTO);
    if (result < 1) {
      throw new CustomException(ErrorCode.ARTICLE_UPDATE_FAIL);
    }
    return result;
  }

  public int updateArticleHit(long articleId) {
    int result = articleMapper.updateArticleHit(articleId);
    return result;
  }

  public int deleteArticle(ArticleDTO articleDTO) {
    int result = articleMapper.deleteArticle(articleDTO);
    if (result < 1) {
      throw new CustomException(ErrorCode.ARTICLE_DELETE_FAIL);
    }
    return result;
  }
}
