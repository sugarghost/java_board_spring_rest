package com.board.spring.rest.yoony.validation.article;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * ArticleIdValidation
 *
 * @author yoony
 * @since 2023. 03. 12.
 * @version 1.0
 * @see ArticleIdValidator
 */
@Documented
@Constraint(validatedBy = ArticleIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ArticleIdValidation {
  String message() default "Invalid Article ID";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}