package com.whooo.barscanner.injectors;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by thuongle on 12/26/15.
 */
@Scope @Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}
