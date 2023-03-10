package com.board.spring.rest.yoony.validation.article;
import com.board.spring.rest.yoony.error.CustomExceptionView;
import com.board.spring.rest.yoony.error.ErrorCode;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WriterValidator implements ConstraintValidator<WriterValidation, String> {
  @Override
  public boolean isValid(String writer, ConstraintValidatorContext context) {
    if (writer != null && !writer.isEmpty() && writer.matches("^.{3,4}$")) {
      return true;
    }
    throw new CustomExceptionView(ErrorCode.ARTICLE_WRITER_NOT_VALID);
  }
}
