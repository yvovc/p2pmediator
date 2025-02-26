package com.ubisoft.streaming.testtask.p2pmediator.config;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.io.File;

/**
 * Class that configures data source components and JOOQ objects.
 *
 * @author yvovc
 * @since 2020/26/09
 */
@Configuration
@ComponentScan
public class DataSourceConfiguration {
    @Value("${db.embedded.postgres.directory}")
    public String embeddedPostgresDataDirectory;

    @Value("${db.embedded.postgres.port}")
    public Integer embeddedPostgresPort;

    @Bean
    @Primary
    public DataSource inMemoryDS() throws Exception {
        final EmbeddedPostgres postgresManager =
                EmbeddedPostgres.builder()
                        .setCleanDataDirectory(true)
                        .setDataDirectory(embeddedPostgresDataDirectory + "/pg_data_dir")
                        .setOverrideWorkingDirectory(new File(embeddedPostgresDataDirectory))
                        .setPort(embeddedPostgresPort)
                        .start();
        return postgresManager.getPostgresDatabase();
    }

    @Bean
    public org.jooq.Configuration dslConfiguration(final DataSource pgDataSource) throws Exception {
        return new DefaultConfiguration()
                .set(new DataSourceConnectionProvider(
                        new TransactionAwareDataSourceProxy(pgDataSource)))
                .set(SQLDialect.POSTGRES);
    }

    @Primary
    public DefaultDSLContext jooqDsl(final org.jooq.Configuration configuration) throws Exception {
        return new DefaultDSLContext(configuration);
    }

}





