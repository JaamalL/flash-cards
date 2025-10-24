package com.techpassport.server.auth.infrastructure.services;

import com.google.protobuf.StringValue;
import com.techpassport.grpc.profile.Request;
import com.techpassport.grpc.profile.Response;
import com.techpassport.server.auth.core.dtos.CreateProfileDto;
import com.techpassport.server.auth.core.ports.services.ICreateProfileClient;
import com.techpassport.grpc.profile.ProfileServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class CreateProfileClient implements ICreateProfileClient {
    private static final Logger logger = LoggerFactory.getLogger(CreateProfileClient.class);

    private final ProfileServiceGrpc.ProfileServiceStub stub;

    public CreateProfileClient(
            @Value("${grpc.profile.host}") String host,
            @Value("${grpc.profile.port}") int port
    ) {
        var channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();

        this.stub = ProfileServiceGrpc.newStub(channel);
    }

    @Override
    public UUID createProfile(CreateProfileDto dto) {
        var requestBuilder = Request.newBuilder()
                .setUserId(dto.userId().toString());

        if (dto.firstName() != null)
            requestBuilder.setFirstName(StringValue.of(dto.firstName()));

        if (dto.lastName() != null)
            requestBuilder.setLastName(StringValue.of(dto.lastName()));

        if (dto.avatar() != null)
            requestBuilder.setAvatar(StringValue.of(dto.avatar()));

        if (dto.phone() != null)
            requestBuilder.setPhone(StringValue.of(dto.phone()));

        if (dto.bio() != null)
            requestBuilder.setBio(StringValue.of(dto.bio()));

        var request = requestBuilder.build();

        CompletableFuture<UUID> future = new CompletableFuture<>();

        stub.createProfile(request, new StreamObserver<>() {
            @Override
            public void onNext(Response response) {
                try {
                    future.complete(UUID.fromString(response.getProfileId()));
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onError(Throwable t) {
                logger.error("Помилка при створенні профілю", t);
                future.completeExceptionally(t);
            }

            @Override
            public void onCompleted() {
            }
        });

        try {
            return future.join();
        } catch (Exception e) {
            throw new RuntimeException("Не вдалося створити профіль", e);
        }
    }
}
