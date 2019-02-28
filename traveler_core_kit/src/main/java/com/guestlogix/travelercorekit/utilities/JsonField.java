package com.guestlogix.travelercorekit.utilities;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JsonField {
    String name() default "";
}
