package apecs.psihapp.backend.domain;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import apecs.psihapp.backend.form.ProfileForm.TypeOfUser;

/**
 * Created by Bogdan-George Craciun on 15.08.2015.
 * Copyright (c) 2015 Bogdan-George Craciun. All rights reserved.
 * -----------------------------------------------------------------
 * The Profile which defines a user of the application.
 */
@Entity
@Cache
public class Profile {
    String displayName;
    String mainEmail;

    TypeOfUser typeOfUser;

    @Id
    String userId;

    /**
     * Public constructor for Profile.
     *
     * @param userId       The user id, obtained from the email
     * @param displayName  Any string user wants us to display him/her on this system.
     * @param mainEmail    User's main e-mail address.
     * @param typeOfUser The User's tee shirt size
     */
    public Profile(String userId, String displayName, String mainEmail, TypeOfUser typeOfUser) {
        this.userId = userId;
        this.displayName = displayName;
        this.mainEmail = mainEmail;
        this.typeOfUser = typeOfUser;
    }

    /**
     * Just making the default constructor private.
     */
    private Profile() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getMainEmail() {
        return mainEmail;
    }

    public TypeOfUser getTypeOfUser() {
        return typeOfUser;
    }

    public String getUserId() {
        return userId;
    }

    public void update(String displayName, TypeOfUser typeOfUser) {
        if (displayName != null) {
            this.displayName = displayName;
        }

        if (typeOfUser != null) {
            this.typeOfUser = typeOfUser;
        }
    }
}


