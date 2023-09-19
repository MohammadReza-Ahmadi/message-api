package com.example.api;

import com.example.model.Comment;
import com.example.repositoy.CommentRepository;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.cloud.datastore.Key;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Api(
        name = "comments",
        version = "v1",
        namespace =
        @ApiNamespace(
                ownerDomain = "comments.example.com",
                ownerName = "comments.example.com",
                packagePath = ""
        )
)

@Slf4j
public class CommentApi {

    private CommentRepository repository;

    public CommentApi() {
        this.repository = new CommentRepository();
    }

    @ApiMethod(name = "create", httpMethod = ApiMethod.HttpMethod.POST)
    public PostResponse create(CommentRequest commReq, HttpServletRequest req) {
        String username = getUsername(req);
        Key key = repository.insert(Comment.builder()
                .postKey(commReq.getPostKey())
                .author(username)
                .body(commReq.getBody())
                .postKey(commReq.getPostKey())
                .build());

        return PostResponse.builder()
                .key(key.getName())
                .message("Comment is added successfully")
                .build();
    }


    private String getUsername(HttpServletRequest req) {
        return req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "TEST_USER";
    }
}
