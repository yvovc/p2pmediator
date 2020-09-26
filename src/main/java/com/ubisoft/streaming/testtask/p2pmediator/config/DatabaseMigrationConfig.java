package com.ubisoft.streaming.testtask.p2pmediator.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class DatabaseMigrationConfig {
    @Autowired
    DataSource ds;

    @PostConstruct
    public void migrateWithFlyway() {
        Flyway flyway = Flyway.configure()
                .dataSource(ds)
                .schemas("p2pmediatordb")
                .defaultSchema("p2pmediatordb")
                .locations("db/migration/**")
                .load();

        flyway.migrate();
    }
}