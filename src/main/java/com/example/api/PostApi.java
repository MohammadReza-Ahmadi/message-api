package com.example.api;

import com.example.model.Post;
import com.example.repositoy.PostRepository;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.cloud.datastore.Key;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Api(
        name = "posts",
        version = "v1",
        namespace =
        @ApiNamespace(
                ownerDomain = "posts.example.com",
                ownerName = "posts.example.com",
                packagePath = ""
        )
)

@Slf4j
public class PostApi {

    private PostRepository repository;

    public PostApi() {
        this.repository = new PostRepository();
    }

    @ApiMethod(name = "create", httpMethod = ApiMethod.HttpMethod.POST)
    public PostResponse create(PostRequest postReq, HttpServletRequest req) {
        String username = getUsername(req);
        Key key = repository.insert(Post.builder()
                .author(username)
                .subject(postReq.getSubject())
                .body(postReq.getBody())
                .build());

        return PostResponse.builder()
                .key(key.getName())
                .message("Post is created successfully")
                .build();
    }

    @ApiMethod(name = "modify", httpMethod = ApiMethod.HttpMethod.PUT)
    public PostResponse modify(PostRequest postReq, @Named("key") String key, HttpServletRequest req) {
        String username = getUsername(req);
        repository.update(Post.builder()
                .author(username)
                .subject(postReq.getSubject())
                .body(postReq.getBody())
                .build(), username, key);

        return PostResponse.builder()
                .message("Post is updated successfully")
                .build();
    }

    private String getUsername(HttpServletRequest req) {
        return req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "TEST_USER";
    }
}