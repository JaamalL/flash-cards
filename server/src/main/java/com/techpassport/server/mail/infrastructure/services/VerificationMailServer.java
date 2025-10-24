package com.techpassport.server.mail.infrastructure.services;

import com.google.protobuf.Empty;
import com.techpassport.grpc.mail.MailSenderGrpc;
import com.techpassport.grpc.mail.Request;
import com.techpassport.server.mail.core.ports.services.IMailSender;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class VerificationMailServer extends MailSenderGrpc.MailSenderImplBase
{
    private final IMailSender mailSender;

    public VerificationMailServer(
            IMailSender mailSender
    )
    {
        this.mailSender = mailSender;
    }

    @Override
    public void sendMail(Request request, StreamObserver<Empty> responseObserver) {
        try {
            mailSender.sendMail(request.getEmail(), request.getSubject(), request.getContent());
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}