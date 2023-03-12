package com.board.spring.rest.yoony.validation.article;

import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.ErrorCode;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * article content 검증
 *
 * @author yoony
 * @version 1.0
 * @see ContentValidation
 * @since 2023. 03. 12.
 */
public class ContentValidator implements ConstraintValidator<ContentValidation, String> {

  /**
   * article content 검증
   * <p> 4자 이상 2000자 미만
   *
   * @param content 검증 대상 content
   * @param context
   * @return 검증 성공시 true
   * @throws CustomException
   * @see ContentValidation
   * @since 2023. 03. 12.
   */
  @Override
  public boolean isValid(String content, ConstraintValidatorContext context) {
    if (content != null && !content.isEmpty() && content.matches("^.{4,1999}$")) {
      return true;
    }
    throw new CustomException(ErrorCode.ARTICLE_CONTENT_NOT_VALID);
  }
}
