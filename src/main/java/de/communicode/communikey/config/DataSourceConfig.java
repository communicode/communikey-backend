/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * Configures Spring Security for the {@link de.communicode.communikey.domain.User} authentication.
 *
 * <p>
 *     Provides the JDBC {@link DriverManagerDataSource} bean.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Configuration
public class AuthenticationProviderConfig {

    @Value("classpath:schema.sql")
    private Resource schemaScript;

    @Value("${spring.datasource.driverClassName}")
    private String databaseDriverClassName;
    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    @Value("${spring.datasource.username}")
    private String databaseUsername;
    @Value("${spring.datasource.password}")
    private String databasePassword;

    /**
     * Sets the JDBC values for the database datasource.
     *
     * @return the configured JDBC {@link DriverManagerDataSource}
     */
    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(databaseDriverClassName);
        driverManagerDataSource.setUrl(datasourceUrl);
        driverManagerDataSource.setUsername(databaseUsername);
        driverManagerDataSource.setPassword(databasePassword);
        return driverManagerDataSource;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }

    private DatabasePopulator databasePopulator() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        return populator;
    }
}