package com.flp.ddd.dddlibrary;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE) // it's on the class level
@Retention(RetentionPolicy.RUNTIME) // build at runtime
@Service // it is used by a service
@Validated // in order to use validations
public @interface UseCase {
}
