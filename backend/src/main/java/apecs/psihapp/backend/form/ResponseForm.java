package apecs.psihapp.backend.form;

import java.util.Date;

/**
 * Created by Bogdan-George Craciun on 18.08.2015.
 * Copyright (c) 2015 Bogdan-George Craciun. All rights reserved.
 * -----------------------------------------------------------------
 */
public class ResponseForm {

    /**
     * The title of the response.
     */
    private String title;

    /**
     * The description of the response.
     */
    private String description;

    /**
     * The date of this response.
     */
    private Date date;

    /**
     * Just making the default constructor private.
     */
    private ResponseForm() {}

    private ResponseForm(String title, String description) {
        this.title = title;
        this.description = description;
        this.date = date == null ? null : new Date(date.getTime());

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }
}
