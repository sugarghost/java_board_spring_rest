package com.board.spring.rest.yoony.validation.file;
import com.board.spring.rest.yoony.error.CustomExceptionView;
import com.board.spring.rest.yoony.error.ErrorCode;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileIdValidator implements ConstraintValidator<FileIdValidation, Long> {
  @Override
  public boolean isValid(Long articleId, ConstraintValidatorContext context) {
    if (articleId < 1) {
      throw new CustomExceptionView(ErrorCode.File_ID_NOT_VALID);
    }
    return true;
  }
}
