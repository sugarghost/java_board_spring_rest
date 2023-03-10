package com.board.spring.rest.yoony.validation.article;
import com.board.spring.rest.yoony.error.CustomExceptionView;
import com.board.spring.rest.yoony.error.ErrorCode;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ArticleIdValidator implements ConstraintValidator<ArticleIdValidation, Long> {
  @Override
  public boolean isValid(Long articleId, ConstraintValidatorContext context) {
    if (articleId < 1) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_ID_NOT_VALID);
    }
    return true;
  }
}
