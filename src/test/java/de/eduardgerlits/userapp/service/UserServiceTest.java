package de.eduardgerlits.userapp.service;

import de.eduardgerlits.userapp.model.User;
import de.eduardgerlits.userapp.model.UserPost;
import de.eduardgerlits.userapp.model.UserWithPosts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserClientService mockedUserClientService;

    @InjectMocks
    private UserService serviceUnderTest;

    @Test
    public void fetchUserData_ShouldReturnUserWithEmptyPostList_IfOnlyUserDataAndNoPostsExist() {

        // Arrange
        final int TEST_USER_ID = 1;
        final User testUser = UserTestDataProvider.createDefaultTestUser();
        final List<UserPost> testUserPosts = UserTestDataProvider.createDefaultTestUserPostList();

        Mockito.when(mockedUserClientService.fetchUser(TEST_USER_ID)).thenReturn(Mono.just(testUser));
        Mockito.when(mockedUserClientService.fetchUserPosts(TEST_USER_ID)).thenReturn(Mono.just(testUserPosts));

        // Act
        UserWithPosts userWithPosts = serviceUnderTest.fetchUserData(TEST_USER_ID)
                .block(); // Subscribe to Publisher for test case

        // Assert
        Assertions.assertThat(userWithPosts).isNotNull();
        Assertions.assertThat(userWithPosts.getUser()).isEqualTo(testUser);
        Assertions.assertThat(userWithPosts.getUserPosts()).isEqualTo(testUserPosts);
    }


    @Test
    public void fetchUserData_ShouldReturnUserWithContainingPosts_IfUserDataAndPostsExist() {

        // Arrange
        final int TEST_USER_ID = 2;
        final User testUser = UserTestDataProvider.createDefaultTestUser();
        final List<UserPost> testUserPosts = List.of();

        Mockito.when(mockedUserClientService.fetchUser(TEST_USER_ID)).thenReturn(Mono.just(testUser));
        Mockito.when(mockedUserClientService.fetchUserPosts(TEST_USER_ID)).thenReturn(Mono.just(testUserPosts));

        // Act
        UserWithPosts userWithPosts = serviceUnderTest.fetchUserData(TEST_USER_ID)
                .block(); // Subscribe to Publisher for test case

        // Assert
        Assertions.assertThat(userWithPosts).isNotNull();
        Assertions.assertThat(userWithPosts.getUser()).isEqualTo(testUser);
        Assertions.assertThat(userWithPosts.getUserPosts()).isEqualTo(testUserPosts);
    }



}
