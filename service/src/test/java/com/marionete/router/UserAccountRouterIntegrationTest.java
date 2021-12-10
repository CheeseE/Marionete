package com.marionete.router;

import com.marionete.model.UserAccount;
import com.marionete.model.UserAccountRequest;
import com.marionete.backends.AccountInfoMock;
import com.marionete.backends.UserInfoMock;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static com.marionete.router.UserAccountRouter.USER_ACCOUNT_ENDPOINT;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserAccountRouterIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeClass
    public static void setUp() {
        UserInfoMock.start();
        AccountInfoMock.start();
    }

    @Test
    public void testGetUserAccount_ReturnsData_WhenUserAndPasswordAreCorrect() {
        webTestClient
                .post().uri(USER_ACCOUNT_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new UserAccountRequest("user", "user")))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserAccount.class).value(userAccount -> {
                    assertThat(userAccount.getUserInfo().getName()).isEqualTo("John");
                    assertThat(userAccount.getUserInfo().getSurname()).isEqualTo("Doe");
                    assertThat(userAccount.getUserInfo().getAge()).isEqualTo(32);
                    assertThat(userAccount.getUserInfo().getSex()).isEqualTo("male");
                    assertThat(userAccount.getAccountInfo().getAccountNumber()).isEqualTo("12345-3346-3335-4456");
                });
    }

    @Test
    public void testGetUserAccount_ReturnsUnauthorised_WhenUserAndPasswordAreInCorrect() {
        webTestClient
                .post().uri(USER_ACCOUNT_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new UserAccountRequest("user", "wrong")))
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }
}