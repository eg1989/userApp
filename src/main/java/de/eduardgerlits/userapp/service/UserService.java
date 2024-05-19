package de.eduardgerlits.userapp.service;

import de.eduardgerlits.userapp.model.UserWithComments;
import de.eduardgerlits.userapp.model.User;
import de.eduardgerlits.userapp.model.UserComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserClientService userClientService;

    public Mono<UserWithComments> buildFullUser(final int id) {
        Mono<User> userMono = userClientService.fetchUser(id);
        Mono<List<UserComment>> userCommentListMono = userClientService.fetchUserComments(id);

        return userMono.zipWith(userCommentListMono)
                .map(tuple -> buildMergedUser(tuple.getT1(), tuple.getT2()));
    }

    private UserWithComments buildMergedUser(User user, List<UserComment> userComments) {
        return new UserWithComments(user, userComments);
    }

}
