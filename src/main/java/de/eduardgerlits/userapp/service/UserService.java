package de.eduardgerlits.userapp.service;

import de.eduardgerlits.userapp.model.User;
import de.eduardgerlits.userapp.model.UserWithPosts;
import de.eduardgerlits.userapp.model.UserPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserClientService userClientService;

    public Mono<UserWithPosts> fetchUserData(final int id) {
        Mono<User> userMono = userClientService.fetchUser(id);
        Mono<List<UserPost>> userCommentListMono = userClientService.fetchUserPosts(id);

        return userMono.zipWith(userCommentListMono)
                .map(tuple -> new UserWithPosts(tuple.getT1(), tuple.getT2()));
    }

}
