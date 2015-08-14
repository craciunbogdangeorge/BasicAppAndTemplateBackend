package apces.psihapp.backend.domain;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Bogdan-George Craciun on 15.08.2015.
 * Copyright (c) 2015 Bogdan-George Craciun. All rights reserved.
 * -----------------------------------------------------------------
 */
@Entity
public class AppEngineUser {
    @Id
    private String email;
    private User user;

    private AppEngineUser() {}

    public AppEngineUser(User user) {
        this.user = user;
        this.email = user.getEmail();
    }
    public User getUser() {
        return user;
    }
    public Key<AppEngineUser> getKey() {
        return Key.create(AppEngineUser.class, email);
    }
}
