package com.ubisoft.streaming.testtask.p2pmediator.config;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
@ComponentScan
public class DataSourceConfiguration { //TODO check throwing exc
    @Bean
    @Primary
    public DataSource inMemoryDS() throws Exception {
        return EmbeddedPostgres.builder()
                .start().getPostgresDatabase();
    }

    @Bean
    public org.jooq.Configuration dslConfiguration() throws Exception {
        return new DefaultConfiguration()
                .set(new DataSourceConnectionProvider(
                        new TransactionAwareDataSourceProxy(inMemoryDS())))
                .set(SQLDialect.POSTGRES);
    }

    @Primary
    public DefaultDSLContext jooqDsl() throws Exception {
        return new DefaultDSLContext(dslConfiguration());
    }

}





