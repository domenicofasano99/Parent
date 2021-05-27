package com.bok.parent.be.security.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('USER') or hasRole('INTERNAL')")
public @interface AllowLogged {
}
