package com.ubisoft.streaming.testtask.p2pmediator.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Database migration config class.
 *
 * @author yvovc
 * @since 2020/26/09
 */
@Configuration
public class DatabaseMigrationConfig {
    private final static String P2P_MEDIATOR_DB_SCHEMA_NAME = "p2pmediatordb";

    @Autowired
    DataSource ds;

    @PostConstruct
    public void migrateWithFlyway() {
        Flyway flyway = Flyway.configure()
                .dataSource(ds)
                .schemas(P2P_MEDIATOR_DB_SCHEMA_NAME)
                .defaultSchema(P2P_MEDIATOR_DB_SCHEMA_NAME)
                .locations("db/migration", "db/migration/data")
                .load();

        flyway.migrate();
    }
}