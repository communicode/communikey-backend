/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.config;

import static de.communicode.communikey.CommunikeyConstants.COOKIE_LOGIN_SESSION_ID;
import static de.communicode.communikey.CommunikeyConstants.ENDPOINT_HTTP_STATUS_CODE_403;
import static de.communicode.communikey.CommunikeyConstants.ENDPOINT_LOGIN;
import static de.communicode.communikey.CommunikeyConstants.ENDPOINT_LOGOUT;
import static de.communicode.communikey.CommunikeyConstants.ENDPOINT_PASSWORDS;
import static de.communicode.communikey.CommunikeyConstants.ENDPOINT_ROOT;
import static de.communicode.communikey.CommunikeyConstants.REQUEST_PARAM_LOGIN_LOGOUT;
import static de.communicode.communikey.CommunikeyConstants.withParameters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configures Spring Security for the {@link de.communicode.communikey.domain.User} authentication.
 *
 * <p>
 *     Defines matcher to allow a per {@link de.communicode.communikey.type.UserRoleType} endpoint access filtering.
 *     Handles the login- and logout sequence and invalidates expired HTTP user sessions, cookies and CSRF tokens.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DriverManagerDataSource dataSource;

    @Autowired
    public WebSecurityConfig(DriverManagerDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder())
            .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username=?")
            .authoritiesByUsernameQuery("SELECT username, role FROM users WHERE username=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(ENDPOINT_LOGIN).permitAll()
            .antMatchers(
                ENDPOINT_ROOT,
                ENDPOINT_PASSWORDS + "/**"
            ).access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
            .and()
            .exceptionHandling().accessDeniedPage(ENDPOINT_HTTP_STATUS_CODE_403)
            .and()
        .formLogin()
            .loginPage(ENDPOINT_LOGIN)
            .usernameParameter("username").passwordParameter("password")
            .and()
        .logout()
            .logoutUrl(ENDPOINT_LOGOUT)
            .logoutSuccessUrl(withParameters(ENDPOINT_LOGIN, REQUEST_PARAM_LOGIN_LOGOUT))
            .invalidateHttpSession(true)
            .deleteCookies(COOKIE_LOGIN_SESSION_ID)
            .and()
        .csrf();
    }

    /**
     * Returns a {@link PasswordEncoder} for the password encryption.
     *
     * @return a {@link BCryptPasswordEncoder} to encrypt passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
