package com.marionete.service;

import com.marionete.model.UserAccount;
import reactor.core.publisher.Mono;

public interface UserAccountService {
    /**
     * Invoking the user and account service and merge the responses.
     * @param token authentication token
     * @return
     */
    Mono<UserAccount> getUserAccount(String token);
}
