package com.rean.shopspring.exception;

import java.lang.annotation.*;
import java.sql.SQLException;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HandleSqlException {
    Class<? extends SQLException> value();
}

