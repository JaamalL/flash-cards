package com.flashcards.server.mail.infrastructure.configurations;

import io.vertx.core.Vertx;
import io.vertx.ext.mail.LoginOption;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.StartTLSOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfiguration {

    @Bean
    public Vertx vertx() {
        return Vertx.vertx();
    }

    @Bean
    public MailClient mailClient(
            Vertx vertx,
            @Value("${spring.mail.host}") String host,
            @Value("${spring.mail.port}") int port,
            @Value("${spring.mail.username}") String username,
            @Value("${spring.mail.password}") String password,
            @Value("${spring.mail.properties.mail.smtp.starttls.enable:true}") boolean starttls,
            @Value("${spring.mail.properties.mail.smtp.auth:true}") boolean auth
    ) {
        MailConfig config = new MailConfig();
        config.setHostname(host);
        config.setPort(port);
        config.setUsername(username);
        config.setPassword(password);
        config.setStarttls(starttls ? StartTLSOptions.REQUIRED : StartTLSOptions.DISABLED);
        config.setLogin(auth ? LoginOption.REQUIRED : LoginOption.NONE);
        config.setKeepAlive(true);
        config.setMaxPoolSize(10);

        return MailClient.createShared(vertx, config, "flashcards-mail-client");
    }
}
