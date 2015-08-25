package apecs.psihapp.backend.service;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import apecs.psihapp.backend.domain.Profile;
import apecs.psihapp.backend.domain.Question;
import apecs.psihapp.backend.domain.Response;

/**
 * Created by Bogdan-George Craciun on 15.08.2015.
 * Copyright (c) 2015 Bogdan-George Craciun. All rights reserved.
 * -----------------------------------------------------------------
 * Custom Objectify Service that this application should use.
 */
public class OfyService {
    /**
     * This static block ensure the entity registration.
     */
    static {
        factory().register(Profile.class);
        factory().register(Question.class);
        factory().register(Response.class);
    }

    /**
     * Use this static method for getting the Objectify service object in order to make sure the
     * above static block is executed before using Objectify.
     *
     * @return Objectify service object.
     */
    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    /**
     * Use this static method for getting the Objectify service factory.
     *
     * @return ObjectifyFactory.
     */
    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
