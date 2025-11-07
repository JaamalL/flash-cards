package com.flashcards.server.common.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Authorize
{
    String[] roles() default {};
    boolean isVerified() default true;
}
