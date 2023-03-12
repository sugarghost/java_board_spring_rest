package com.board.spring.rest.yoony.validation.article;

import com.board.spring.rest.yoony.error.CustomException;
import com.board.spring.rest.yoony.error.ErrorCode;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * article password 검증
 *
 * @author yoony
 * @version 1.0
 * @see PasswordValidation
 * @since 2023. 03. 12.
 */
public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {

  /**
   * article password 검증
   * <p> 4자 이상 15자 이하
   * <p> 영문, 숫자, 특수문자 조합
   *
   * @param password 검증 대상 password
   * @param context
   * @return 검증 성공시 true
   * @throws CustomException
   * @see PasswordValidation
   * @since 2023. 03. 12. author Yoony version 1.0
   */
  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    if (password != null && !password.isEmpty() && password.matches(
        "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{4,15}$")) {
      return true;
    }
    throw new CustomException(ErrorCode.ARTICLE_PASSWORD_NOT_VALID);
  }
}
