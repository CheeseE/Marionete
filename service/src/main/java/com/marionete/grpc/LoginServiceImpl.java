package com.marionete.grpc;

import com.marionete.model.UserAccountRequest;
import com.marionete.util.JwtTokenUtil;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import services.LoginResponse;
import services.LoginServiceGrpc.LoginServiceImplBase;

@GrpcService
@AllArgsConstructor
public class LoginServiceImpl extends LoginServiceImplBase {

    private JwtTokenUtil tokenUtil;

    /**
     * gRPC call to the login service to get a jwt token
     * @param request login request with username and password
     * @param responseObserver
     */
    @Override
    public void login(services.LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        //If username matches password then we return a token.
        if (request.getUsername().equals(request.getPassword())) {
            responseObserver.onNext(
                    LoginResponse.newBuilder()
                            .setToken(tokenUtil.generateToken(request.getUsername()))
                            .build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(
                    Status.UNAUTHENTICATED
                            .withDescription("Incorrect username or password!")
                            .asRuntimeException());
        }
    }

}
