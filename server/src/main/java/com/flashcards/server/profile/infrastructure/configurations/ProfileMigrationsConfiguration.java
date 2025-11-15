package com.flashcards.server.profile.infrastructure.configurations;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ProfileMigrationsConfiguration
{
    @Bean
    public SpringLiquibase profileLiquibase(
            @Qualifier("profileDataSource") DataSource dataSource
    ) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/profile/changelog-master.yaml");
        liquibase.setShouldRun(true);
        return liquibase;
    }
}
