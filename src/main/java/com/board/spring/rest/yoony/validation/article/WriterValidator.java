package com.board.spring.rest.yoony.validation.article;

import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.ErrorCode;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * article writer 검증
 *
 * @author yoony
 * @version 1.0
 * @see WriterValidation
 * @since 2023. 03. 12.
 */
public class WriterValidator implements ConstraintValidator<WriterValidation, String> {

  /**
   * article writer 검증
   * <p> 3자 이상 4자 미만
   *
   * @param writer  검증 대상 writer
   * @param context
   * @return 검증 성공시 true
   * @throws CustomException
   * @see WriterValidation
   * @since 2023. 03. 12.
   */
  @Override
  public boolean isValid(String writer, ConstraintValidatorContext context) {
    if (writer != null && !writer.isEmpty() && writer.matches("^.{3,4}$")) {
      return true;
    }
    throw new CustomException(ErrorCode.ARTICLE_WRITER_NOT_VALID);
  }
}
