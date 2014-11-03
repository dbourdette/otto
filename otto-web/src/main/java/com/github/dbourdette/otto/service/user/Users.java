package com.github.dbourdette.otto.service.user;

import com.github.dbourdette.otto.SpringConfig;
import com.google.code.morphia.Datastore;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

@Service("users")
public class Users {
    @Inject
    private Datastore datastore;

    @Inject
    private SpringConfig springConfig;

    @PostConstruct
    public void bootstrap() {
        datastore.ensureIndex(User.class, "username_idx", "username", true, false);
    }

    public List<User> findUsers() {
        return datastore.find(User.class).order("username").asList();
    }

    public User findUser(String id) {
        return datastore.get(User.class, new ObjectId(id));
    }

    public void save(User user) {
        datastore.save(user);
    }

    public boolean available(String username) {
        return datastore.find(User.class).field("username").equal(username).countAll() == 0;
    }

    public void delete(User user) {
        datastore.delete(user);
    }

    public User findUserByUsername(String username) {
        return datastore.find(User.class).filter("username =", username).get();
    }
}
