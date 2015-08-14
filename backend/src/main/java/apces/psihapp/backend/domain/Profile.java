package apces.psihapp.backend.domain;

import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bogdan-George Craciun on 15.08.2015.
 * Copyright (c) 2015 Bogdan-George Craciun. All rights reserved.
 * -----------------------------------------------------------------
 */

@Entity
@Cache
public class Profile {
    String displayName;
    String mainEmail;

    TypeOfUser typeOfUser;

    private List<String> conferenceKeysToAttend = new ArrayList<>(0);

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

    public List<String> getConferenceKeysToAttend() {
        return ImmutableList.copyOf(conferenceKeysToAttend);
    }

    public void addToConferenceKeysToAttend(String conferenceKey) {
        conferenceKeysToAttend.add(conferenceKey);
    }

    /**
     * Remove the conferenceKey from conferenceKeysToAttend.
     *
     * @param conferenceKey a websafe String representation of the Conference Key
     */
    public void unregisterFromConference(String conferenceKey) {
        if (conferenceKeysToAttend.contains(conferenceKey)) {
            conferenceKeysToAttend.remove(conferenceKey);
        } else {
            throw new IllegalArgumentException("Invalid conferenceKey: " + conferenceKey);
        }
    }

    public static enum TypeOfUser {
        Doctor,
        Patient,
    }
}


