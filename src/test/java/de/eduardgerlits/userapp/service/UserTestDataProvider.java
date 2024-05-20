package de.eduardgerlits.userapp.service;

import de.eduardgerlits.userapp.model.*;

import java.util.List;

public class UserTestDataProvider {

    public static User createDefaultTestUser() {
        return de.eduardgerlits.userapp.model.User.builder()
                .id(1)
                .name("Marc Brown")
                .username("M4rcBr0wn")
                .email("marc.brown@gmail.com")
                .address(Address.builder()
                        .street("Kulas Light")
                        .suite("Apt. 556")
                        .city("Gwenborough")
                        .zipcode("92998-3874")
                        .geo(Geo.builder()
                                .lat("-37.3159")
                                .lng("81.1496")
                                .build())
                        .build())
                .phone("1-771-706-8231 x56292")
                .website("www.marc-brown.com")
                .company(Company.builder()
                        .name("My Comp")
                        .catchPhrase("Single-layered neural-net")
                        .bs("e-commerce")
                        .build())
                .build();
    }

    public static User createDefaultTestUser2() {
        return de.eduardgerlits.userapp.model.User.builder()
                .id(2)
                .name("Kurtis Weissnat")
                .username("Elwyn.Skiles")
                .email("Telly.Hoeger@billy.biz")
                .address(Address.builder()
                        .street("Rex Trail")
                        .suite("Suite 280")
                        .city("Howemouth")
                        .zipcode("58804-1099")
                        .geo(Geo.builder()
                                .lat("24.891")
                                .lng("21.8984")
                                .build())
                        .build())
                .phone("1-770-736-8031 x56442")
                .website("hildegard.org")
                .company(Company.builder()
                        .name("Romaguera-Crona")
                        .catchPhrase("Multi-layered client-server neural-net")
                        .bs("harness real-time e-markets")
                        .build())
                .build();
    }

    public static List<UserPost> createDefaultTestUserPostList() {
        return List.of(
                UserPost.builder().userId(1).id(1).title("first").body("Lorem ipsum dolor sit amet").build(),
                UserPost.builder().userId(1).id(2).title("second").body("Consetetur sadipscing elitr").build(),
                UserPost.builder().userId(1).id(3).title("third").body("Sed diam nonumy eirmod tempor invidunt").build(),
                UserPost.builder().userId(1).id(4).title("fourth").body("Ut labore et dolore magna aliquyam erat").build()
        );
    }

}
