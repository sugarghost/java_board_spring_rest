package com.board.spring.rest.yoony.validation.article;

import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.ErrorCode;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * article title 검증
 *
 * @author yoony
 * @version 1.0
 * @see TitleValidation
 * @since 2023. 03. 12.
 */
public class TitleValidator implements ConstraintValidator<TitleValidation, String> {

  /**
   * article title 검증
   * <p> 4자 이상 100자 미만
   *
   * @param title   검증 대상 title
   * @param context
   * @return 검증 성공시 true
   * @throws CustomException
   * @author Yoony
   * @version 1.0
   * @see TitleValidation
   * @since 2023. 03. 12.
   */
  @Override
  public boolean isValid(String title, ConstraintValidatorContext context) {
    if (title != null && !title.isEmpty() && title.matches("^.{4,99}$")) {
      return true;
    }
    throw new CustomException(ErrorCode.ARTICLE_TITLE_NOT_VALID);
  }
}
