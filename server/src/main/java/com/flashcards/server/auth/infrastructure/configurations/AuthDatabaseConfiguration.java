package com.flashcards.server.auth.infrastructure.configurations;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
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
        basePackages = "com.flashcards.server.auth.infrastructure.repository",
        entityManagerFactoryRef = "authManagerFactory",
        transactionManagerRef = "authTransactionManager"
)
public class AuthDatabaseConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.auth")
    public DataSourceProperties authDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource authDataSource() {
        return authDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean authManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(authDataSource());
        factory.setPackagesToScan("com.flashcards.server.auth.core.entities");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPersistenceUnitName("auth");
        return factory;
    }

    @Bean
    public JpaTransactionManager authTransactionManager() {
        return new JpaTransactionManager(authManagerFactory().getObject());
    }

    @Bean
    public EntityManager authEntityManager(@Qualifier("authManagerFactory") LocalContainerEntityManagerFactoryBean factory) {
        return factory.getObject().createEntityManager();
    }
}
