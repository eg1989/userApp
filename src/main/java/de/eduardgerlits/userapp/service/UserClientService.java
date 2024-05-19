package de.eduardgerlits.userapp.service;

import de.eduardgerlits.userapp.model.User;
import de.eduardgerlits.userapp.model.UserComment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class UserClientService {

    private WebClient client;

    public UserClientService() {
        this.client = WebClient.create("https://jsonplaceholder.typicode.com");
    }

    public Mono<User> fetchUser(final int id) {
        return client.get()
                .uri("/users/{id}", id)
                .retrieve().bodyToMono(User.class)
                .log("Request User for user id " + id)
                .onErrorResume(ex -> Mono.empty());
    }

    public Mono<List<UserComment>> fetchUserComments(final int id) {
        return client.get()
                .uri("/posts?userId={id}", id)
                .retrieve().bodyToFlux(UserComment.class)
                .collectList().log("Request UserComments for user id " + id);
    }

}
