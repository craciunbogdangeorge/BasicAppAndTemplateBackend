package apecs.psihapp.backend.domain;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import java.util.Date;
import java.util.List;

import apecs.psihapp.backend.form.QuestionForm;

import static apecs.psihapp.backend.service.OfyService.ofy;

/**
 * Created by Bogdan-George Craciun on 15.08.2015.
 * Copyright (c) 2015 Bogdan-George Craciun. All rights reserved.
 * -----------------------------------------------------------------
 * The question send / received by the doctor / patient.
 */
@Entity
@Cache
public class Question {

    private static final List<String> DEFAULT_DRUGS = ImmutableList.of("Default", "Drug");

    /**
     * The id for the datastore key.
     *
     * We use automatic id assignment for entities of Question class.
     */
    @Id
    private long id;

    /**
     * The title of the question.
     */
    @Index
    private String title;

    /**
     * The description of the question.
     */
    private String description;

    /**
     * Drugs that a patient takes.
     */
    @Index
    private List<String> listOfDrugs;

    /**
     * The date of this question.
     */
    private Date date;

    /**
     * Holds Profile key as the parent.
     */
    @Parent
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private Key<Profile> profileKey;

    /**
     * The userId of the doctor who created the question.
     */
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private String creatorUserId;

    /**
     * Just making the default constructor private.
     */
    private Question() {}

    public Question(final long id, final String creatorUserId, final QuestionForm questionForm) {
        Preconditions.checkNotNull(questionForm.getTitle(), "The title is required");
        this.id = id;
        this.profileKey = Key.create(Profile.class, creatorUserId);
        this.creatorUserId = creatorUserId;
        updateWithQuestionForm(questionForm);
    }

    /**
     * Updates the Question with QuestionForm.
     * This method is used upon object creation as well as updating existing Question.
     *
     * @param questionForm contains form data sent from the client.
     */
    private void updateWithQuestionForm(QuestionForm questionForm) {
        this.title = questionForm.getTitle();
        this.description = questionForm.getDescription();

        List<String> listOfDrugs = questionForm.getListOfDrugs();
        this.listOfDrugs = listOfDrugs == null || listOfDrugs.isEmpty() ? DEFAULT_DRUGS : listOfDrugs;

        Date date = questionForm.getDate();
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

    /**
     * Returns a defensive copy of list of drugs if not null.
     *
     * @return a defensive copy of list of drugs if not null.
     */
    public List<String> getListOfDrugs() {
        return listOfDrugs == null ? null : ImmutableList.copyOf(listOfDrugs);
    }

    /**
     * Returns a defensive copy of date if not null.
     *
     * @return a defensive copy of date if not null.
     */
    public Date getDate() {
        return date == null ? null : new Date(date.getTime());
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public Key<Profile> getProfileKey() {
        return profileKey;
    }

    // Get a String version of the key
    public String getWebsafeKey() {
        return Key.create(profileKey, Question.class, id).getString();
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public String getCreatorUserId() {
        return creatorUserId;
    }

    /**
     * Returns creator's display name.
     *
     * @return creator's display name. If there is no Profile, return his/her userId.
     */
    public String getCreatorDisplayName() {
        // Profile organizer = ofy().load().key(Key.create(Profile.class, creatorUserId)).now();
        Profile creator = ofy().load().key(getProfileKey()).now();
        if (creator == null) {
            return creatorUserId;
        } else {
            return creator.getDisplayName();
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Id: " + id + "\n")
                .append("Title: ").append(title).append("\n");
        if (description != null) {
            stringBuilder.append("Description: ").append(description).append("\n");
        }
        if (listOfDrugs != null && listOfDrugs.size() > 0) {
            stringBuilder.append("Drugs:\n");
            for (String drug : listOfDrugs) {
                stringBuilder.append("\t").append(drug).append("\n");
            }
        }
        if (date != null) {
            stringBuilder.append("Date: ").append(date.toString()).append("\n");
        }

        return stringBuilder.toString();
    }

}
