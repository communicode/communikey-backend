/*
 * This file is part of communikey.
 * Copyright (C) 2016-2018  communicode AG <communicode.de>
 *
 * communikey is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.communicode.communikey.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.hibernate.HikariConfigurationUtil;
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

    @Value("${spring.datasource.hikari.jdbc-url}")
    private String datasourceUrl;

    @Value("${spring.datasource.hikari.username}")
    private String databaseUsername;

    @Value("${spring.datasource.hikari.password}")
    private String databasePassword;
    /**
     * Configures the JDBC driver manager data source.
     *
     * @return the configured JDBC driver manager data source
     */
    @Bean
    public HikariDataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(datasourceUrl);
        hikariConfig.setUsername(databaseUsername);
        hikariConfig.setPassword(databasePassword);
        hikariConfig.setMaximumPoolSize(10);
        return new HikariDataSource(hikariConfig);
    }

    /**
     * The bean to initialize the database.
     *
     * @param dataSource the datasource bean
     * @return the data source initializer bean
     */
    @Bean
    DataSourceInitializer dataSourceInitializer(HikariDataSource dataSource) {
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
     * @return the database populator object
     */
    private DatabasePopulator databasePopulator() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        return populator;
    }
}
