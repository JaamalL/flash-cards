package com.techpassport.server.profile.infrastructure.services;

import com.techpassport.grpc.profile.ProfileServiceGrpc;
import com.techpassport.server.profile.core.dtos.CreateProfileDto;
import com.techpassport.server.profile.core.ports.services.ICreateProfile;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import com.techpassport.grpc.profile.Request;
import com.techpassport.grpc.profile.Response;

import java.util.UUID;

@GrpcService
public class CreateProfileServer extends ProfileServiceGrpc.ProfileServiceImplBase
{
    private final ICreateProfile createProfile;

    public CreateProfileServer(
            ICreateProfile createProfile
    ) {
        this.createProfile = createProfile;
    }

    @Override
    public void createProfile(Request request, StreamObserver<Response> responseObserver) {
        try {
            var dto = new CreateProfileDto(
                    UUID.fromString(request.getUserId()),
                    request.hasFirstName() ? request.getFirstName().getValue() : null,
                    request.hasLastName() ? request.getLastName().getValue() : null,
                    request.hasAvatar() ? request.getAvatar().getValue() : null,
                    request.hasPhone() ? request.getPhone().getValue() : null,
                    request.hasBio() ? request.getBio().getValue() : null
            );

            var profile = createProfile.createProfile(dto);

            Response response = Response.newBuilder()
                    .setProfileId(profile.getId().toString())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
