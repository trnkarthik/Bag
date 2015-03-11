package com.getmebag.bag.annotations;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by karthiktangirala on 3/10/15.
 */
@Qualifier
@Retention(RUNTIME)
public @interface IsThisFirstTimeUse {
}
