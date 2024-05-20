package de.eduardgerlits.userapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.eduardgerlits.userapp.model.User;
import de.eduardgerlits.userapp.model.UserPost;
import de.eduardgerlits.userapp.model.UserWithPosts;
import de.eduardgerlits.userapp.service.UserTestDataProvider;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class UserControllerIntegrationTest {

    private static final int MOCK_WEB_SERVER_PORT = 8081;

    private static MockWebServer mockWebServer;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebTestClient client;

    @BeforeAll
    public static void setUp() throws Exception {

        User userResponseId1 = UserTestDataProvider.createDefaultTestUser();
        User userResponseId2 = UserTestDataProvider.createDefaultTestUser2();
        List<UserPost> postListResponseId1 = UserTestDataProvider.createDefaultTestUserPostList();

        String userResponseId1AsStr = objectMapper.writeValueAsString(userResponseId1);
        String userResponseId2AsStr = objectMapper.writeValueAsString(userResponseId2);
        String postListResponseId1AsStr = objectMapper.writeValueAsString(postListResponseId1);

        mockWebServer = new MockWebServer();
        mockWebServer.setDispatcher(new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest request) throws InterruptedException {
                ObjectMapper om = new ObjectMapper();
                switch (request.getPath()) {

                    case "/users/1" ->
                        new MockResponse()
                                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .setResponseCode(200)
                                .setBody(userResponseId1AsStr);

                    case "/users/2" ->
                        new MockResponse()
                                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .setResponseCode(200)
                                .setBody(userResponseId2AsStr);

                    case "/users/-1" ->
                        new MockResponse()
                                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .setResponseCode(404);

                    case "/users/100" ->
                        new MockResponse()
                                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .setResponseCode(500);

                    case "/posts?userId=1" ->
                        new MockResponse()
                                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .setResponseCode(200)
                                .setBody(postListResponseId1AsStr);

                    case "/posts?userId=2" ->
                        new MockResponse()
                                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .setResponseCode(200)
                                .setBody("[]");

                }
                return new MockResponse();
            }
        });

        mockWebServer.start(MOCK_WEB_SERVER_PORT);

    }

    @AfterAll
    public static void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void getUserWithPosts_ShouldReturn200AndUserWithPosts_IfUserAndPostExist() {

        User userResponseId1 = UserTestDataProvider.createDefaultTestUser();
        List<UserPost> postListResponseId1 = UserTestDataProvider.createDefaultTestUserPostList();

        client.get().uri("/users/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserWithPosts.class)
                .isEqualTo(new UserWithPosts(userResponseId1, postListResponseId1));
    }

    @Test
    void getUserWithPosts_ShouldReturn200AndUserWithEmptyPostList_IfUserExistsWithNoPosts() {

        User userResponseId2 = UserTestDataProvider.createDefaultTestUser2();

        client.get().uri("/users/2")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserWithPosts.class)
                .isEqualTo(new UserWithPosts(userResponseId2, List.of()));
    }

    @Test
    void getUserWithPosts_ShouldReturn404AndApiErrorObject_IfUserForGivenIdDoesNotExist() {

        User userResponseId1 = UserTestDataProvider.createDefaultTestUser();
        List<UserPost> postListResponseId1 = UserTestDataProvider.createDefaultTestUserPostList();

        client.get().uri("/users/-1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("User with id -1 not found")
                .jsonPath("$.errors").exists();
    }

    @Test
    void getUserWithPosts_ShouldReturn500AndApiErrorObject_IfApiIsNotReachable() {

        User userResponseId1 = UserTestDataProvider.createDefaultTestUser();
        List<UserPost> postListResponseId1 = UserTestDataProvider.createDefaultTestUserPostList();

        client.get().uri("/users/100")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.status").isEqualTo("INTERNAL_SERVER_ERROR")
                .jsonPath("$.message").isEqualTo("User API to path http://localhost:8081/users/100 seems to be unreachable")
                .jsonPath("$.errors").exists();
    }

}
