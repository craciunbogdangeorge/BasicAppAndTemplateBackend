package apecs.psihapp.backend.domain;

/**
 * Created by Bogdan-George Craciun on 15.08.2015.
 * Copyright (c) 2015 Bogdan-George Craciun. All rights reserved.
 * -----------------------------------------------------------------
 * Wrapper class for an announcement message.
 */
public class Announcement {

    private String message;

    public Announcement() {}

    public Announcement(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}