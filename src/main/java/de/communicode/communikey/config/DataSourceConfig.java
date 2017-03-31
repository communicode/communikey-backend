/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2017
 */
package de.communicode.communikey.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Configures Spring Security for the {@link de.communicode.communikey.domain.User} authentication.
 *
 * <p>Provides the JDBC {@link DriverManagerDataSource} bean.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Configuration
@EnableJpaRepositories("de.communicode.communikey.repository")
@EnableJpaAuditing(auditorAwareRef = "securityAuditorAware")
@EnableTransactionManagement
public class DataSourceConfig {

    @Value("classpath:schema.sql")
    private Resource schemaScript;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    /**
     * Configures the JDBC driver manager data source.
     *
     * @return the configured JDBC driver manager data source
     */
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .url(datasourceUrl)
            .username(databaseUsername)
            .password(databasePassword)
            .build();
    }

    /**
     * The bean to initialize the database.
     *
     * @param dataSource the datasource bean
     * @return the data source initializer bean
     */
    @Bean
    DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }

    /**
     * Populates the database with schema scripts.
     *
     * <p>Currently runs the SQL script for
     * <ul>
     *   <li>OAuth2</li>
     * </ul>
     *
     * @return the database populartor object
     */
    private DatabasePopulator databasePopulator() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        return populator;
    }
}