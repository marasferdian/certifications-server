package com.ibm.certificationsserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Value("certifications")
    private String resourceId;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(resourceId);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/register/").permitAll()
                .antMatchers(HttpMethod.GET, "**").permitAll()
                .antMatchers(HttpMethod.POST,"/certifications/filters").permitAll()
                .antMatchers(HttpMethod.POST,"/certifications/custom").permitAll()
                .antMatchers(HttpMethod.POST, "/certifications/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/certifications").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/certifications/").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/certifications/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/requests/{\\d+}/{\\d+}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.OPTIONS, "**").permitAll()
                .anyRequest().authenticated();
    }
}
