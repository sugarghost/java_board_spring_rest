package com.board.spring.rest.yoony.validation.article;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * article content 검증
 *
 * @author yoony
 * @since 2023. 03. 12.
 * @version 1.0
 * @see ContentValidator
 */
@Documented
@Constraint(validatedBy = ContentValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentValidation {

  String message() default "Invalid Content format";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
