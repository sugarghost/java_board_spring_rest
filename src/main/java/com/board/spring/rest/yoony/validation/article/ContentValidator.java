package com.board.spring.rest.yoony.validation.article;
import com.board.spring.rest.yoony.error.CustomExceptionView;
import com.board.spring.rest.yoony.error.ErrorCode;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContentValidator implements ConstraintValidator<ContentValidation, String> {
  @Override
  public boolean isValid(String content, ConstraintValidatorContext context) {
    if (content != null && !content.isEmpty() && content.matches("^.{4,1999}$")) {
      return true;
    }
    throw new CustomExceptionView(ErrorCode.ARTICLE_CONTENT_NOT_VALID);
  }
}
