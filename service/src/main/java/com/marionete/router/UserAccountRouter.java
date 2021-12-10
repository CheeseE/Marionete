package com.marionete.router;

import com.marionete.handler.UserAccountHandler;
import com.marionete.model.UserAccount;
import com.marionete.model.UserAccountRequest;
import com.marionete.service.UserAccountServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class UserAccountRouter {

    public static final String USER_ACCOUNT_ENDPOINT = "/marionete/useraccount";

    @Bean
    @RouterOperation(path = USER_ACCOUNT_ENDPOINT,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.POST,
            beanClass = UserAccountServiceImpl.class,
            beanMethod = "getUserAccount",
            operation = @Operation(
                    operationId = "getUserAccount",
                    responses = {
                        @ApiResponse(responseCode = "200", description = "successful operation",
                                content = @Content(schema = @Schema(implementation = UserAccount.class))),
                        @ApiResponse(responseCode = "401", description = "unAuthorized operation")
                    },
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UserAccountRequest.class))))
    )
    public RouterFunction<ServerResponse> route(UserAccountHandler handler) {
        return RouterFunctions
                .route(POST(USER_ACCOUNT_ENDPOINT)
                        .and(accept(MediaType.APPLICATION_JSON)), handler::getUserAccount);
    }
}
