package com.ibm.certificationsserver.util;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;

import java.util.UUID;

public class UniqueAuthenticationKeyGenerator implements AuthenticationKeyGenerator {
    public String extractKey(OAuth2Authentication authentication) {
        return UUID.randomUUID().toString();
    }
}
