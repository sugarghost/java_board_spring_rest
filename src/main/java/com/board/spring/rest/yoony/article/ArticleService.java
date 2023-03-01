package com.board.spring.rest.yoony.article;

import com.board.spring.rest.yoony.article.search.SearchDTO;
import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.ErrorCode;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  @Autowired
  private ArticleMapper articleMapper;

  public int insertArticle(ArticleDTO articleDTO) {
    int result = articleMapper.insertArticle(articleDTO);
    if (result < 1) {
      throw new CustomException(ErrorCode.ARTICLE_INSERT_FAIL);
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
