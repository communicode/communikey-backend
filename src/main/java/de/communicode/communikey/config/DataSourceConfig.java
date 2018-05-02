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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;

/**
 * Initializes the OAuth2 database schema and enables the JPA repository auditing.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Configuration
@EnableJpaRepositories("de.communicode.communikey.repository")
@EnableJpaAuditing(auditorAwareRef = "securityAuditorAware")
public class DataSourceConfig {

  private DataSource dataSource;

  @Value("classpath:schema.sql")
  private Resource schemaScript;

  @Autowired
  DataSourceConfig(DataSource dataSource) {
    requireNonNull(dataSource, "dataSource must not be null!");
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
   * @return the database populator object
   */
  private DatabasePopulator databasePopulator() {
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(schemaScript);
    return populator;
  }
}