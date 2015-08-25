package apecs.psihapp.backend.domain;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.common.base.Preconditions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import java.util.Date;

import apecs.psihapp.backend.form.ResponseForm;

/**
 * Created by Bogdan-George Craciun on 15.08.2015.
 * Copyright (c) 2015 Bogdan-George Craciun. All rights reserved.
 * -----------------------------------------------------------------
 * The response send / received by the patient / doctor.
 */
@Entity
@Cache
public class Response {
    /**
     * The id for the datastore key.
     *
     * We use automatic id assignment for entities of Response class.
     */
    @Id
    private long id;

    /**
     * The title of the response.
     */
    @Index
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
     * Holds Profile key as the parent.
     */
    @Parent
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private Key<Profile> profileKey;

    /**
     * The userId of the patient who created the response.
     */
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private String creatorUserId;

    /**
     * Just making the default constructor private.
     */
    private Response() {}

    public Response(final long id, final String creatorUserId, final ResponseForm responseForm) {
        Preconditions.checkNotNull(responseForm.getTitle(), "The title is required");
        this.id = id;
        this.profileKey = Key.create(Profile.class, creatorUserId);
        this.creatorUserId = creatorUserId;
        updateWithResponseForm(responseForm);
    }

    /**
     * TODO - document
     * @param responseForm
     */
    private void updateWithResponseForm(ResponseForm responseForm) {
        this.title = responseForm.getTitle();
        this.description = responseForm.getDescription();
        Date date = responseForm.getDate();
        this.date = date == null ? null : new Date(date.getTime());

    }

    public long getId() {
        return id;
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
