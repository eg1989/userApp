package de.eduardgerlits.userapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.eduardgerlits.userapp.exception.ApiUnreachableException;
import de.eduardgerlits.userapp.exception.UserNotFoundException;
import de.eduardgerlits.userapp.exception.UserPostsNotFoundException;
import de.eduardgerlits.userapp.model.User;
import de.eduardgerlits.userapp.model.UserPost;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserClientServiceTest {

    private static final int MOCK_WEB_SERVER_PORT = 8081;
    private static final String MOCK_WEB_SERVER_URI = "http://localhost:" + MOCK_WEB_SERVER_PORT;

    private static MockWebServer mockWebServer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UserClientService serviceUnderTest = new UserClientService(MOCK_WEB_SERVER_URI);


    @BeforeEach
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(MOCK_WEB_SERVER_PORT);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void fetchUser_ShouldThrowUserNotFoundException_IfHttpResponseIsNotFound() {

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.NOT_FOUND.value())
                        .setHeader("Content-Type", "application/json")
        );

        Assertions
                .assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> serviceUnderTest.fetchUser(-1).block())
                .withMessage("User with id -1 not found");
    }

    @Test
    public void fetchUser_ShouldThrowApiUnreachableException_IfHttpResponseIs5XX() {

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .setHeader("Content-Type", "application/json")
        );

        Assertions
                .assertThatExceptionOfType(ApiUnreachableException.class)
                .isThrownBy(() -> serviceUnderTest.fetchUser(-1).block())
                .withMessage("User API to path http://localhost:8081/users/-1 seems to be unreachable");
    }

    @Test
    public void fetchUser_ShouldReturnUser_IfHttpResponseIsOkWithUser() throws Exception {

        // Arrange
        User providedTestUser = UserTestDataProvider.createDefaultTestUser();
        final int testUserId = providedTestUser.getId();

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader("Content-Type", "application/json")
                        .setBody(objectMapper.writeValueAsString(providedTestUser))
        );

        // Act
        User responseUser = serviceUnderTest.fetchUser(testUserId).block();

        // Assert
        assertThat(responseUser).isNotNull();
        assertThat(responseUser).isEqualTo(providedTestUser);
    }

    @Test
    public void fetchUserPosts_ShouldThrowUserNotFoundException_IfHttpResponseIsNotFound() {

        // Arrange
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.NOT_FOUND.value())
                        .setHeader("Content-Type", "application/json")
        );

        // Act & Assert
        Assertions
                .assertThatExceptionOfType(UserPostsNotFoundException.class)
                .isThrownBy(() -> serviceUnderTest.fetchUserPosts(-1).block())
                .withMessage("No posts found for User with id -1");
    }

    @Test
    public void fetchUserPosts_ShouldThrowApiUnreachableException_IfHttpResponseIs5XX() {

        // Arrange
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .setHeader("Content-Type", "application/json")
        );

        // Act & Assert
        Assertions
                .assertThatExceptionOfType(ApiUnreachableException.class)
                .isThrownBy(() -> serviceUnderTest.fetchUserPosts(-1).block())
                .withMessage("User API to path http://localhost:8081/posts?userId=-1 seems to be unreachable");
    }

    @Test
    public void fetchUserPosts_ShouldReturnPostsForGivenUser_IfHttpResponseIsOkWithUserPost() throws Exception {

        // Arrange
        List<UserPost> userPosts = UserTestDataProvider.createDefaultTestUserPostList();
        final int testUserId = 1;

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader("Content-Type", "application/json")
                        .setBody(objectMapper.writeValueAsString(userPosts))
        );

        // Act
        List<UserPost> responseUserPosts = serviceUnderTest.fetchUserPosts(testUserId).block();

        // Assert
        assertThat(responseUserPosts).isNotNull();
        assertThat(userPosts.containsAll(responseUserPosts)).isTrue();
        assertThat(userPosts.size()).isEqualTo(responseUserPosts.size());
    }

    @Test
    public void fetchUserPosts_ShouldReturnEmptyPostList_IfHttpResponseIsOkWithUser() throws Exception {

        // Arrange
        final int testUserIdWithNoPosts = 2;

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader("Content-Type", "application/json")
                        .setBody(objectMapper.writeValueAsString(List.of()))
        );

        // Act
        List<UserPost> responseUserPosts = serviceUnderTest.fetchUserPosts(testUserIdWithNoPosts).block();

        // Assert
        assertThat(responseUserPosts).isNotNull();
        assertThat(responseUserPosts.size()).isEqualTo(0);
    }


}
