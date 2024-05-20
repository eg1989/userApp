package de.eduardgerlits.userapp.service;

import de.eduardgerlits.userapp.exception.ApiUnreachableException;
import de.eduardgerlits.userapp.exception.UserNotFoundException;
import de.eduardgerlits.userapp.exception.UserPostsNotFoundException;
import de.eduardgerlits.userapp.model.User;
import de.eduardgerlits.userapp.model.UserPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class UserClientService {

    private String baseUrl;

    private WebClient client;

    public UserClientService(@Value("${application.api.baseUrl}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = WebClient.create(baseUrl);
    }

    public Mono<User> fetchUser(final int id) {

        final String usersPath = "/users/";
        final String usersPathTemplate = usersPath + "{id}";
        return client.get()
                .uri(usersPathTemplate, id)
                .retrieve()
                .onStatus(httpStatusCode -> HttpStatus.NOT_FOUND == httpStatusCode, clientResponse -> Mono.error(new UserNotFoundException(id)))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new ApiUnreachableException(baseUrl + usersPath + id)))
                .bodyToMono(de.eduardgerlits.userapp.model.User.class)
                .log("Request User for user id " + id);
    }

    public Mono<List<UserPost>> fetchUserPosts(final int id) {
        final String userPostsPath = "/posts?userId=";
        final String userPostsPathTemplate = userPostsPath + "{id}";
        return client.get()
                .uri(userPostsPathTemplate, id)
                .retrieve()
                .onStatus(httpStatusCode -> HttpStatus.NOT_FOUND == httpStatusCode, clientResponse -> Mono.error(new UserPostsNotFoundException(id)))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new ApiUnreachableException(baseUrl + userPostsPath + id)))
                .bodyToFlux(UserPost.class)
                .collectList()
                .log("Request UserPosts for user id " + id);
    }

}
