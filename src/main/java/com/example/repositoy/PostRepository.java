package com.example.repositoy;

import com.example.model.Post;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.Key;

import java.util.Date;
import java.util.UUID;


public class PostRepository extends EntityPersistence<Post> {

    public Key insert(Post post) {
        String id = UUID.randomUUID().toString();
        Key key = getKeyFactory().newKey(id);
        FullEntity.Builder<Key> builder = FullEntity.newBuilder(key);

        if (post.getAuthor() != null) {
            builder.set("author", post.getAuthor());
        }

        if (post.getSubject() != null) {
            builder.set("subject", post.getSubject());
        }

        if (post.getBody() != null) {
            builder.set("body", post.getBody());
        }

        builder.set("createDate", new Date().getTime());

        return save(builder.build());
    }

    public boolean update(Post post, String username, String key) {

        Entity.Builder builder = Entity.newBuilder(getKeyFactory().newKey(key));

        builder.set("author", username);

        if (post.getSubject() != null) {
            builder.set("subject", post.getSubject());
        }

        if (post.getBody() != null) {
            builder.set("body", post.getBody());
        }

        builder.set("changeDate", new Date().getTime());

        return super.update(builder.build());
    }

}
