package apces.psihapp.backend.domain;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.Date;

/**
 * Created by Bogdan-George Craciun on 15.08.2015.
 * Copyright (c) 2015 Bogdan-George Craciun. All rights reserved.
 * -----------------------------------------------------------------
 * The question send / received by the doctor / patient.
 */
@Entity
@Cache
public class Question {
    /**
     * The id for the datastore key.
     *
     * We use automatic id assignment for entities of Question class.
     */
    @Id
    private long id;

    /**
     * The date of this question.
     */
    private Date date;

    /**
     * Just making the default constructor private.
     */
    private Question() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
