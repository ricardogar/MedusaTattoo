package com.medusa.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_SECRETARIA";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String CLIENTE = "ROLE_CLIENTE";

    private AuthoritiesConstants() {
    }
}
