package com.marionete.handler;

import com.marionete.model.UserAccount;
import com.marionete.model.UserAccountRequest;
import com.marionete.service.AuthServiceImpl;
import com.marionete.service.UserAccountServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

@Component
@AllArgsConstructor
public class UserAccountHandler {

    private UserAccountServiceImpl userAccountService;
    private AuthServiceImpl authService;

    /**
     * Getting authenticated user and account details.
     * @param request
     * @return
     */
    public Mono<ServerResponse> getUserAccount(@Nonnull ServerRequest request) {
        return request.bodyToMono(UserAccountRequest.class)
                .map(r -> authService.getAuthToken(r.getUsername(), r.getPassword()))
                .flatMap(optional ->
                        optional.map(token ->
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(userAccountService.getUserAccount(token), UserAccount.class)
                        ).orElse(ServerResponse.status(HttpStatus.UNAUTHORIZED).build()));
    }
}
