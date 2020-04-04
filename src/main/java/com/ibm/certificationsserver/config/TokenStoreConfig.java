package com.ibm.certificationsserver.config;

import com.ibm.certificationsserver.util.UniqueAuthenticationKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
public class TokenStoreConfig {
    @Autowired
    private DataSource dataSource;

    @Bean
    public TokenStore tokenStore() {

        JdbcTokenStore jdbcTokenStore = new JdbcTokenStore(dataSource);
        jdbcTokenStore.setAuthenticationKeyGenerator(new UniqueAuthenticationKeyGenerator());

        return jdbcTokenStore;
    }
}
