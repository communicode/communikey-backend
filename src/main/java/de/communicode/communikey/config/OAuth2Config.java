/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.config;

import de.communicode.communikey.service.UserDetailsRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import static de.communicode.communikey.CommunikeyApplication.APP_ID;
import static de.communicode.communikey.type.PrivilegeType.READ;
import static de.communicode.communikey.type.PrivilegeType.WRITE;
import static de.communicode.communikey.type.RoleType.ROLE_ADMIN;
import static de.communicode.communikey.type.RoleType.ROLE_USER;
import static java.util.Objects.requireNonNull;

/**
 * Holds both configuration classes for the OAuth2 authorization- and resource server.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
public class OAuth2Config {

    /**
     * Configures the OAuth2 resource server.
     */
    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId(APP_ID);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().authenticated()
                .and()
                .csrf().disable();
        }
    }

    /**
     * Configures the OAuth2 authorization server.
     */
    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Qualifier("authenticationManagerBean")
        private final AuthenticationManager authenticationManager;

        private UserDetailsRestService userDetailsRestService;
        private final DataSourceConfig dataSourceConfig;

        @Value("${cckey.security.oauth2.access_token_validity}")
        private int access_token_validity;

        @Value("${cckey.security.oauth2.refresh_token_validity}")
        private int refresh_token_validity;

        @Autowired
        public AuthorizationServerConfiguration(AuthenticationManager authenticationManager, DataSourceConfig dataSourceConfig,
                                                UserDetailsRestService userDetailsRestService) {
            this.authenticationManager = requireNonNull(authenticationManager, "authenticationManager must not be null!");
            this.dataSourceConfig = requireNonNull(dataSourceConfig, "dataSourceConfig must not be null!");
            this.userDetailsRestService = requireNonNull(userDetailsRestService, "userDetailsRestService must not be null!");
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsRestService)
                .tokenStore(tokenStore());
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.jdbc(dataSourceConfig.dataSource())
                .withClient(APP_ID)
                .authorizedGrantTypes("password", "refresh_token")
                .authorities(ROLE_USER.name(), ROLE_ADMIN.name())
                .autoApprove(true)
                .resourceIds(APP_ID)
                .scopes(READ.name().toLowerCase(), WRITE.name().toLowerCase())
                .accessTokenValiditySeconds(access_token_validity)
                .refreshTokenValiditySeconds(refresh_token_validity);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer
                .realm(APP_ID);
        }

        @Bean
        public TokenStore tokenStore() {
            return new JdbcTokenStore(dataSourceConfig.dataSource());
        }

    }
}
