package com.marionete.service;

import java.util.Optional;

public interface AuthService {
    /**
     * Authenticating the user with userName and password.
     * @param user userName
     * @param password password
     * @return
     */
    Optional<String> getAuthToken(String user, String password);
}
