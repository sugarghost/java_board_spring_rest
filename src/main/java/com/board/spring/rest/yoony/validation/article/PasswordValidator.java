package com.board.spring.rest.yoony.validation.article;
import com.board.spring.rest.yoony.error.CustomExceptionView;
import com.board.spring.rest.yoony.error.ErrorCode;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {
  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    if (password != null && !password.isEmpty() && password.matches(
        "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{4,15}$")) {
      return true;
    }
    throw new CustomExceptionView(ErrorCode.ARTICLE_PASSWORD_NOT_VALID);
  }
}
