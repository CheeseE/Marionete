package com.marionete.service;

import io.grpc.StatusRuntimeException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import services.LoginRequest;
import services.LoginServiceGrpc;

import java.util.Optional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Setter
    @GrpcClient("loginService")
    private LoginServiceGrpc.LoginServiceBlockingStub loginServiceStub;

    @Override
    public Optional<String> getAuthToken(String user, String password) {
        LoginRequest request = LoginRequest.newBuilder()
                .setUsername(user)
                .setPassword(password)
                .build();
        try {
            var loginResponse = loginServiceStub.login(request);
            log.info("Auth token successfully received: {}", loginResponse.getToken());
            return Optional.of(loginResponse.getToken());
        } catch (StatusRuntimeException e) {
            log.warn("Error during gRPC invocation: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
