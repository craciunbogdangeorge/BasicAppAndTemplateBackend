package apces.psihapp.backend.form;

import com.google.common.collect.ImmutableList;

import java.util.Date;
import java.util.List;

/**
 * Created by Bogdan-George Craciun on 15.08.2015.
 * Copyright (c) 2015 Bogdan-George Craciun. All rights reserved.
 * -----------------------------------------------------------------
 * A simple Java object (POJO) representing a Question form sent from the client.
 */
public class QuestionForm {
    /**
     * The title of the question.
     */
    private String title;

    /**
     * The description of the question.
     */
    private String description;

    /**
     * Drugs that a patient takes.
     */
    private List<String> listOfDrugs;

    /**
     * The date of the question.
     */
    private Date date;

    private QuestionForm() {}

    public QuestionForm(String title, String description, List<String> listOfDrugs, Date date) {
        this.title = title;
        this.description = description;
        this.listOfDrugs = listOfDrugs == null ? null : ImmutableList.copyOf(listOfDrugs);
        this.date = date == null ? null : new Date(date.getTime());

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getListOfDrugs() {
        return listOfDrugs;
    }

    public Date getDate() {
        return date;
    }
}
