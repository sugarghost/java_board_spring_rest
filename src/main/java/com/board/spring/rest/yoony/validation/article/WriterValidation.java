package com.board.spring.rest.yoony.validation.article;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Constraint(validatedBy = WriterValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WriterValidation {
  String message() default "Invalid Writer format";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}


