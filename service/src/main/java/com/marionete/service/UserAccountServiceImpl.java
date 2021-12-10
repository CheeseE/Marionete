package com.marionete.service;

import com.marionete.model.AccountInfo;
import com.marionete.model.UserAccount;
import com.marionete.model.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    @Qualifier("user-client")
    private final WebClient userWebClient;
    @Value("${user.service.url}")
    private final String userServiceUrl;

    @Qualifier("account-client")
    private final WebClient accountWebClient;
    @Value("${account.service.url}")
    private final String accountServiceUrl;

    @Override
    public Mono<UserAccount> getUserAccount(String token) {
        var user = userWebClient.get()
                .uri(userServiceUrl)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(UserInfo.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .filter(this::isServerError));
        var account = accountWebClient.get()
                .uri(accountServiceUrl)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(AccountInfo.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .filter(this::isServerError));
        return Mono.zip(account, user, UserAccount::new).log();
    }

    private boolean isServerError(Throwable throwable) {
        log.error("Error during webclient request: {}", throwable.getMessage());
        return throwable instanceof TimeoutException ||
                (throwable instanceof WebClientResponseException &&
                ((WebClientResponseException) throwable).getStatusCode().is5xxServerError());
    }
}
