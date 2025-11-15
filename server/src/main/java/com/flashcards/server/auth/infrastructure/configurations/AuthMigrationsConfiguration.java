package com.flashcards.server.auth.infrastructure.configurations;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AuthMigrationsConfiguration
{
    @Bean
    public SpringLiquibase authLiquibase(
            @Qualifier("authDataSource") DataSource dataSource
    ) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/auth/changelog-master.yaml");
        liquibase.setShouldRun(true);
        return liquibase;
    }
}
