package com.sds_guesthouse.util.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Documented
@Constraint(validatedBy = {})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "아이디는 필수 입력 값입니다.")
@Size(min = 4, max = 20, message = "아이디는 4~20자 사이여야 합니다.")
@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 영문과 숫자만 사용할 수 있습니다.")
public @interface ValidUserId {
    String message() default "아이디 형식이 올바르지 않습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}