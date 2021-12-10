package com.marionete.grpc;

import com.google.rpc.Code;
import com.google.rpc.Status;
import com.marionete.util.JwtTokenUtil;
import io.grpc.internal.testing.StreamRecorder;
import io.grpc.protobuf.StatusProto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import services.LoginRequest;
import services.LoginResponse;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SpringJUnitConfig(classes = { LoginServiceImplTest.LoginServiceTestConfiguration.class })
class LoginServiceImplTest {

    @Configuration
    static class LoginServiceTestConfiguration {
        @Bean
        LoginServiceImpl loginService() {
            return new LoginServiceImpl(new JwtTokenUtil("dummy_secret"));
        }
    }

    @Autowired
    private LoginServiceImpl serviceToTest;

    @Test
    void loginWithBadCredentials_Returns_Unauthenticated() throws Exception{
        LoginRequest request = LoginRequest.newBuilder()
                .setUsername("user")
                .setPassword("password")
                .build();
        StreamRecorder<LoginResponse> responseObserver = StreamRecorder.create();
        //StatusRuntimeException thrown =
        serviceToTest.login(request, responseObserver);
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time");
        }
        assertNotNull(responseObserver.getError());
        Status status = StatusProto.fromThrowable(responseObserver.getError());
        assertNotNull(status);
        assertEquals("UNAUTHENTICATED", Code.forNumber(status.getCode()).toString());
        assertEquals("Incorrect username or password!", status.getMessage());
    }

    @Test
    void loginWithGoodCredentials_Returns_Token() throws Exception{
        LoginRequest request = LoginRequest.newBuilder()
                .setUsername("admin")
                .setPassword("admin")
                .build();
        StreamRecorder<LoginResponse> responseObserver = StreamRecorder.create();
        serviceToTest.login(request, responseObserver);
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time");
        }
        assertNull(responseObserver.getError());
        List<LoginResponse> results = responseObserver.getValues();
        assertEquals(1, results.size());
        LoginResponse response = results.get(0);
        assertNotNull(response.getToken());
    }
}