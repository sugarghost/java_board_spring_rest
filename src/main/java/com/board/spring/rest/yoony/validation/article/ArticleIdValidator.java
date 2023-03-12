package com.board.spring.rest.yoony.validation.article;

import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.ErrorCode;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author Yoony
 * @version 1.0
 * @see ArticleIdValidation
 * @since 2023. 03. 12.
 */
public class ArticleIdValidator implements ConstraintValidator<ArticleIdValidation, Long> {

  /**
   * articleId 검증(1 이상)
   * @param articleId 검증 대상 articleId
   * @param context
   * @return
   * @throws CustomException
   *
   * @author Yoony
   * @version 1.0
   * @see ArticleIdValidation
   * @since 2023. 03. 12.
   */
  @Override
  public boolean isValid(Long articleId, ConstraintValidatorContext context) {
    if (articleId < 1) {
      throw new CustomException(ErrorCode.ARTICLE_ID_NOT_VALID);
    }
    return true;
  }
}
