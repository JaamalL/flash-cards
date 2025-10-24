package com.techpassport.server.auth.infrastructure.services;

import com.techpassport.server.auth.core.ports.services.IVerificationMailerClient;
import com.techpassport.grpc.mail.MailSenderGrpc;
import com.techpassport.grpc.mail.Request;
import com.google.protobuf.Empty;

import com.techpassport.server.auth.core.values.VerificationMailDto;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class VerificationMailClient implements IVerificationMailerClient {

    private static final Logger log = LoggerFactory.getLogger(VerificationMailClient.class);

    @Value("${spring.application.url}")
    private String baseUrl;

    private final MailSenderGrpc.MailSenderStub stub;

    public VerificationMailClient(
            @Value("${grpc.mail.host}") String host,
            @Value("${grpc.mail.port}") int port
    ) {
        var channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();

        this.stub = MailSenderGrpc.newStub(channel);
    }

    @Override
    public void sendVerificationMail(VerificationMailDto dto) {
        var subject = String.format("Your verification code is %s", dto.code());
        var html = loadVerifyTemplate(dto);

        var request = Request.newBuilder()
                .setEmail(dto.email())
                .setSubject(subject)
                .setContent(html)
                .build();

        stub.sendMail(request, new StreamObserver<>() {
            @Override
            public void onNext(Empty empty) {
            }

            @Override
            public void onError(Throwable t) {
                log.error("Error sending email: {}", t.getMessage(), t);
            }

            @Override
            public void onCompleted() {
                log.info("The letter to {} was sent successfully.", "");
            }
        });
    }

    private String loadVerifyTemplate(VerificationMailDto dto) {
        try (var in = new ClassPathResource("templates/verify.html").getInputStream()) {
            var html = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            var link = String.format("%s/auth/verify/token?verifyToken=%s", baseUrl, dto.token());
            return html
                    .replace("{CODE}", dto.code())
                    .replace("{LINK}", link)
                    .replace("{LIFETIME}", String.format("%d minutes", dto.lifetimeMinutes()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load verify.html template", e);
        }
    }
}
