package com.board.spring.rest.yoony.validation.article;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * article title 검증
 *
 * @author yoony
 * @version 1.0
 * @see TitleValidator
 * @since 2023. 03. 12.
 */
@Documented
@Constraint(validatedBy = TitleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TitleValidation {

  String message() default "Invalid Title format";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
