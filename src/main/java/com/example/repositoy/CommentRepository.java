package com.example.repositoy;

import com.example.model.Comment;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.Key;

import java.util.Date;
import java.util.UUID;


public class CommentRepository extends EntityPersistence<Comment> {

    private final PostRepository postRepository;

    public CommentRepository() {
        this.postRepository = new PostRepository();
    }

    public Key insert(Comment comment) {

        if (postRepository.get(comment.getPostKey()) == null) {
            throw new RuntimeException("There is no post with id: '" + comment.getPostKey() + "'");
        }

        String id = UUID.randomUUID().toString();
        Key key = getKeyFactory().newKey(id);
        FullEntity.Builder<Key> builder = FullEntity.newBuilder(key);

        if (comment.getAuthor() != null) {
            builder.set("author", comment.getAuthor());
        }

        if (comment.getBody() != null) {
            builder.set("body", comment.getBody());
        }

        builder.set("postKey", comment.getPostKey());
        builder.set("createDate", new Date().getTime());

        return super.save(builder.build());
    }

}
