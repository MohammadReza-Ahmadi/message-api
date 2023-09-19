package com.example.repositoy;

import com.google.cloud.datastore.*;

import java.lang.reflect.ParameterizedType;


public class EntityPersistence<T> {

    private static final String PROJECT_ID = "project-2-id2-399020";
    private static final Datastore datastore;

    static {
        datastore = DatastoreOptions.newBuilder().setProjectId(PROJECT_ID).build().getService();
    }

    private final KeyFactory keyFactory;

    public EntityPersistence() {
        this.keyFactory = datastore.newKeyFactory().setKind(getParameterClass().getSimpleName());
    }

    protected final KeyFactory getKeyFactory() {
        return keyFactory;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getParameterClass() {
        return (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Key save(FullEntity<Key> entity) {
        Transaction transaction = datastore.newTransaction();
        try {
            Entity add = datastore.put(entity);
            if (add == null) {
                throw new RuntimeException("Error happens in saving entity: '" + getParameterClass().getName() + "'");
            }
            transaction.commit();
            return add.getKey();

        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }


    protected boolean update(Entity entity) {
        Transaction transaction = datastore.newTransaction();
        try {
            Entity fetched = get(entity.getKey());

            if (fetched == null) {
                throw new RuntimeException("Entity of type: '" + getParameterClass().getName() + "' with key: '" + entity.getKey().getName() + "' not found");
            }

            if (!fetched.getString("author").equals(entity.getString("author"))){
                throw new RuntimeException("Only author of the post is allowed to edite");
            }

            datastore.update(entity);
            transaction.commit();
            return true;

        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }


    protected Entity get(Key key) {
        return datastore.get(key);
    }

    protected Entity get(String key) {
        return datastore.get(keyFactory.newKey(key));
    }
    protected boolean delete(Key... keys) {
        Transaction transaction = datastore.newTransaction();
        try {
            datastore.delete(keys);
            transaction.commit();
            return true;

        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }
}
