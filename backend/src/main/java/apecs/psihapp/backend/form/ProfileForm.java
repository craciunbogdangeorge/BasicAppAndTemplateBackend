package apecs.psihapp.backend.form;

/**
 * Created by Bogdan-George Craciun on 15.08.2015.
 * Copyright (c) 2015 Bogdan-George Craciun. All rights reserved.
 * -----------------------------------------------------------------
 * Pojo representing a profile form on the client side.
 */
public class ProfileForm {
    /**
     * Any string user wants us to display him/her on this system.
     */
    private String displayName;

    /**
     * Type of user.
     */
    private TypeOfUser typeOfUser;

    private ProfileForm() {
    }

    /**
     * Constructor for ProfileForm, solely for unit test.
     *
     * @param displayName       A String for displaying the user on this system.
     * @param typeOfUser The type of the user.
     */
    public ProfileForm(String displayName, TypeOfUser typeOfUser) {
        this.displayName = displayName;
        this.typeOfUser = typeOfUser;
    }

    public String getDisplayName() {
        return displayName;
    }

    public TypeOfUser getTypeOfUser() {
        return typeOfUser;
    }

    public static enum TypeOfUser {
        Doctor,
        Patient,
        NOT_SPECIFIED
    }
}
