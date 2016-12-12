/*
 * Copyright (C) communicode AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 2016
 */
package de.communicode.communikey.config;

import de.communicode.communikey.domain.Key;
import de.communicode.communikey.domain.KeyCategory;
import de.communicode.communikey.domain.Privilege;
import de.communicode.communikey.domain.Role;
import de.communicode.communikey.domain.User;
import de.communicode.communikey.domain.UserGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
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
 * <p>
 *     Provides the JDBC {@link DriverManagerDataSource} bean.
 *
 * @author sgreb@communicode.de
 * @since 0.2.0
 */
@Configuration
public class DataSourceConfig {

    /**
     * The name of the table for {@link KeyCategory} entities.
     */
    public static final String CATEGORIES = "categories";

    /**
     * The name of the column for creator {@link User} entity IDs.
     */
    public static final String CREATOR_USER_ID = "creator_user_id";

    /**
     * The name of the column for {@link KeyCategory} entity IDs.
     */
    public static final String KEY_CATEGORY_ID = "key_category_id";

    /**
     * The name of the column for {@link Key} entity IDs.
     */
    public static final String KEY_ID = "key_id";

    /**
     * The name of the table for {@link Key} entities.
     */
    public static final String KEYS = "\"keys\"";

    /**
     * The name of the column for {@link Role} entity IDs.
     */
    public static final String PRIVILEGE_ID = "privilege_id";

    /**
     * The name of the column for responsible {@link User} entity IDs.
     */
    public static final String RESPONSIBLE_USER_ID = "responsible_user_id";

    /**
     * The name of the column for {@link Role} entity IDs.
     */
    public static final String ROLE_ID = "role_id";

    /**
     * The name of the table for the relation of {@link Role}- to {@link Privilege} entities.
     */
    public static final String ROLES_PRIVILEGES = "roles_privileges";

    /**
     * The name of the table for {@link User} entities.
     */
    public static final String USERS = "users";

    /**
     * The name of the column for {@link UserGroup} entity IDs.
     */
    public static final String USER_GROUP_ID = "user_group_id";

    /**
     * The name of the column for {@link UserGroup} entity names.
     */
    public static final String USER_GROUP_NAME = "user_group_name";

    /**
     * The name of the table for {@link UserGroup} entities.
     */
    public static final String USER_GROUPS = "user_groups";

    /**
     * The name of the table for the relation of {@link User}- to {@link UserGroup} entities.
     */
    public static final String USERS_GROUPS = "users_groups";

    /**
     * The name of the table for the relation of {@link UserGroup}- to {@link KeyCategory} entities.
     */
    public static final String USER_GROUPS_KEY_CATEGORIES = "user_groups_key_categories";

    /**
     * The name of the column for {@link User} entity IDs.
     */
    public static final String USER_ID = "user_id";

    /**
     * The name of the table for the relation of {@link User}- to {@link Role} entities.
     */
    public static final String USERS_ROLES = "users_roles";

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
     * Sets the JDBC values for the database data source.
     *
     * @return the configured JDBC driver manager data source
     */
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .driverClassName(databaseDriverClassName)
            .url(datasourceUrl)
            .username(databaseUsername)
            .password(databasePassword)
            .build();
    }

    /**
     * The bean to initialize the database.
     *
     * @param dataSource the {@link #dataSource()} bean
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
     * @return a database populartor object
     */
    private DatabasePopulator databasePopulator() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        return populator;
    }
}