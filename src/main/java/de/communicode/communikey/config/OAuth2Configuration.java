/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.config;

import static de.communicode.communikey.type.PrivilegeType.READ;
import static de.communicode.communikey.type.PrivilegeType.WRITE;
import static de.communicode.communikey.type.RoleType.ROLE_ADMIN;
import static de.communicode.communikey.type.RoleType.ROLE_USER;
import static java.util.Objects.requireNonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@EnableAuthorizationServer
@EnableResourceServer
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {

    private final String CLIENT_ID = "cckey-ui";

    @Value("${cckey.security.oauth2.access_token_validity}")
    private int access_token_validity;

    @Value("${cckey.security.oauth2.refresh_token_validity}")
    private int refresh_token_validity;

    private final AuthenticationManager authenticationManager;

    private final DataSourceConfig dataSourceConfig;

    @Autowired
    public OAuth2Configuration(AuthenticationManager authenticationManager, DataSourceConfig dataSourceConfig) {
        this.authenticationManager = requireNonNull(authenticationManager, "authenticationManager must not be null!");
        this.dataSourceConfig = requireNonNull(dataSourceConfig, "dataSourceConfig must not be null!");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
            .authenticationManager(authenticationManager)
            .tokenStore(tokenStore());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSourceConfig.dataSource())
            .withClient(CLIENT_ID)
            .authorizedGrantTypes("password", "refresh_token")
            .authorities(ROLE_USER.name(), ROLE_ADMIN.name())
            .autoApprove(true)
            .resourceIds(CLIENT_ID)
            .scopes(READ.name().toLowerCase(), WRITE.name().toLowerCase())
            .accessTokenValiditySeconds(access_token_validity)
            .refreshTokenValiditySeconds(refresh_token_validity)
            .resourceIds(CLIENT_ID);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSourceConfig.dataSource());
    }
}
