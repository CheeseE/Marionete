package com.marionete;

import com.marionete.backends.AccountInfoMock;
import com.marionete.backends.UserInfoMock;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "UserAccount API", version = "1.0"))
public class Application {

    public static void main(String[] args) {
        UserInfoMock.start();
        AccountInfoMock.start();
        SpringApplication.run(Application.class, args);
    }
}
