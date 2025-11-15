package com.flashcards.server.profile.infrastructure.configurations;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.flashcards.server.profile.infrastructure.repository",
        entityManagerFactoryRef = "profileManagerFactory",
        transactionManagerRef = "profileTransactionManager"
)
public class ProfileDatabaseConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.profile")
    public DataSourceProperties profileDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource profileDataSource() {
        return profileDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean profileManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(profileDataSource());
        factory.setPackagesToScan("com.flashcards.server.profile.core.entities");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPersistenceUnitName("profile");
        return factory;
    }

    @Bean
    public JpaTransactionManager profileTransactionManager() {
        return new JpaTransactionManager(profileManagerFactory().getObject());
    }
}
