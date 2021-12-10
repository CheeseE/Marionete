package com.marionete.service;

import com.marionete.grpc.LoginServiceImpl;
import com.marionete.util.JwtTokenUtil;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "grpc.server.inProcessName=test",
        "grpc.server.port=-1",
        "grpc.client.loginService.address=in-process:test"
})
@SpringJUnitConfig(classes = { AuthServiceImplTest.LoginServiceTestConfiguration.class })
@DirtiesContext
class AuthServiceImplTest {

    @Configuration
    @ImportAutoConfiguration({
            GrpcClientAutoConfiguration.class,
            GrpcServerAutoConfiguration.class,
            GrpcServerFactoryAutoConfiguration.class
    })
    static class LoginServiceTestConfiguration {
        JwtTokenUtil jwtUtil = new JwtTokenUtil("dummy_secret");
        @Bean
        AuthService authService() {
            return new AuthServiceImpl();
        }
        @Bean
        LoginServiceImpl loginService() {
            return new LoginServiceImpl(jwtUtil);
        }
    }

    @Autowired
    private AuthService serviceToTest;

    @Test
    @DirtiesContext
    void Authentication_Returns_Token() {
        assertTrue(serviceToTest.getAuthToken("admin", "admin").isPresent());
        assertFalse(serviceToTest.getAuthToken("admin", "admin").get().isEmpty());
    }

    @Test
    @DirtiesContext
    void BadAuthentication_Returns_Empty() {
        assertFalse(serviceToTest.getAuthToken("user", "admin").isPresent());
    }
}