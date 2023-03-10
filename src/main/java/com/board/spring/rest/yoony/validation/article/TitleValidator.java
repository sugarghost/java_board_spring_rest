package com.board.spring.rest.yoony.validation.article;
import com.board.spring.rest.yoony.error.CustomExceptionView;
import com.board.spring.rest.yoony.error.ErrorCode;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TitleValidator implements ConstraintValidator<TitleValidation, String> {
  @Override
  public boolean isValid(String title, ConstraintValidatorContext context) {
    if (title != null && !title.isEmpty() && title.matches("^.{4,99}$")) {
      return true;
    }
    throw new CustomExceptionView(ErrorCode.ARTICLE_TITLE_NOT_VALID);
  }
}
