/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.config;

import de.communicode.communikey.security.AuthoritiesConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

import static de.communicode.communikey.config.SecurityConfig.APP_ID;
import static de.communicode.communikey.controller.RequestMappings.API;
import static de.communicode.communikey.controller.RequestMappings.USERS;
import static de.communicode.communikey.controller.RequestMappings.USERS_ACTIVATE;
import static de.communicode.communikey.controller.RequestMappings.USERS_PASSWORD_RESET;
import static de.communicode.communikey.controller.RequestMappings.USERS_REGISTER;
import static java.util.Objects.requireNonNull;

/**
 * Configures the OAuth2 authorization- and resource server.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Configuration
public class OAuth2Config {

    private final DataSource dataSource;

    @Bean
    public JdbcTokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Autowired
    public OAuth2Config(DataSource dataSource) {
        this.dataSource =requireNonNull(dataSource, "dataSource must not be null!");
    }

    /**
     * Configures the OAuth2 resource server.
     *
     * @author sgreb@communicode.de
     * @since 0.2.0
     */
    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        private final TokenStore tokenStore;

        @Autowired
        public ResourceServerConfiguration(TokenStore tokenStore) {
            this.tokenStore = requireNonNull(tokenStore, "tokenStore must not be null!");
        }

        @Bean
        public DefaultAccessTokenConverter defaultAccessTokenConverter() {
            DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
            defaultAccessTokenConverter.setIncludeGrantType(true);
            return defaultAccessTokenConverter;
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources
                .resourceId(APP_ID)
                .tokenStore(tokenStore);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .antMatchers(USERS + USERS_REGISTER).permitAll()
                .antMatchers(USERS + USERS_ACTIVATE).permitAll()
                .antMatchers(USERS + USERS_PASSWORD_RESET).permitAll()
                .antMatchers("/oauth/authorize").permitAll()
                .antMatchers(API + "/**").authenticated()
            .and()
                .csrf().disable();
        }
    }

    /**
     * Configures the OAuth2 authorization server.
     *
     * @author sgreb@communicode.de
     * @since 0.2.0
     */
    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Qualifier("authenticationManagerBean")
        private final AuthenticationManager authenticationManager;

        private final TokenStore tokenStore;

        private final CommunikeyProperties communikeyProperties;

        @Autowired
        public AuthorizationServerConfiguration(AuthenticationManager authenticationManager, TokenStore tokenStore, CommunikeyProperties communikeyProperties) {
            this.authenticationManager = requireNonNull(authenticationManager, "authenticationManager must not be null!");
            this.tokenStore = requireNonNull(tokenStore, "tokenStore must not be null!");
            this.communikeyProperties = requireNonNull(communikeyProperties, "communikeyProperties must not be null!");
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                .withClient(APP_ID)
                .secret(communikeyProperties.getSecurity().getoAuth2().getSecret())
                .resourceIds(APP_ID)
                .authorizedGrantTypes("implicit")
                .authorities(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER)
                .scopes("read", "write")
                .autoApprove(true)
                .accessTokenValiditySeconds(communikeyProperties.getSecurity().getoAuth2().getAccessTokenValidity());
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer.realm(APP_ID);
        }
    }
}
