package apecs.psihapp.backend.spi;

/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Work;

import java.util.logging.Logger;

import javax.inject.Named;

import apecs.psihapp.backend.Constants;
import apecs.psihapp.backend.MyBean;
import apecs.psihapp.backend.domain.AppEngineUser;
import apecs.psihapp.backend.domain.Profile;
import apecs.psihapp.backend.domain.Question;
import apecs.psihapp.backend.domain.Response;
import apecs.psihapp.backend.form.ProfileForm;
import apecs.psihapp.backend.form.ProfileForm.TypeOfUser;
import apecs.psihapp.backend.form.QuestionForm;
import apecs.psihapp.backend.form.ResponseForm;

import static apecs.psihapp.backend.service.OfyService.factory;
import static apecs.psihapp.backend.service.OfyService.ofy;

/**
 * Created by Bogdan-George Craciun on 15.08.2015.
 * Copyright (c) 2015 Bogdan-George Craciun. All rights reserved.
 * -----------------------------------------------------------------
 * Main endpoint api class exposed.
 */

/**
 * An endpoint class we are exposing
 */
@Api(name = "psihEndpointApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.psihapp.apecs", ownerName = "backend.psihapp.apecs", packagePath = ""),
        scopes = {Constants.EMAIL_SCOPE},
        clientIds = {
                Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID,
                Constants.API_EXPLORER_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE},
        description = "API for the PsihApp Backend application.")
public class PsihEndpointApi {

    private static final Logger LOG = Logger.getLogger(PsihEndpointApi.class.getName());

    /**
     * Creates or updates a Profile object associated with the given user
     * object.
     *
     * @param user        A User object injected by the cloud endpoints.
     * @param profileForm A ProfileForm object sent from the client form.
     * @return Profile object just created.
     * @throws UnauthorizedException when the User object is null.
     */

    // Declare this method as a method available externally through Endpoints
    @ApiMethod(name = "saveProfile", path = "profile", httpMethod = ApiMethod.HttpMethod.POST)
    // The request that invokes this method should provide data that
    // conforms to the fields defined in ProfileForm
    public Profile saveProfile(final User user, ProfileForm profileForm) throws UnauthorizedException {

        // If the user is not logged in, throw an UnauthorizedException
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

        // Get the userId and mainEmail
        String mainEmail = user.getEmail();
        String userId = getUserId(user);

        // Get the displayName and typeOfUser sent by the request
        String displayName = profileForm.getDisplayName();
        TypeOfUser typeOfUser = profileForm.getTypeOfUser();

        // Get the Profile entity from the datastore if exists
        // otherwise create a new one
        Profile profile = ofy().load().key(Key.create(Profile.class, userId)).now();

        if (profile == null) {
            // Set the displayName and typeOfUser with default values
            // if not sent in the request
            if (displayName == null) {
                displayName = extractDefaultDisplayNameFromEmail(user.getEmail());
            }
            if (typeOfUser == null) {
                typeOfUser = TypeOfUser.NOT_SPECIFIED;
            }
            // Create a new Profile entity
            profile = new Profile(userId, displayName, mainEmail, typeOfUser);
        } else {
            // The Profile entity already exists
            // Update the Profile entity
            profile.update(displayName, typeOfUser);
        }

        // Save the Profile entity in the datastore
        ofy().save().entity(profile).now();

        // Return the profile
        return profile;
    }

    /**
     * Returns a Profile object associated with the given user object. The cloud
     * endpoints system automatically inject the User object.
     *
     * @param user A User object injected by the cloud endpoints.
     * @return Profile object.
     * @throws UnauthorizedException when the User object is null.
     */
    @ApiMethod(name = "getProfile", path = "profile", httpMethod = ApiMethod.HttpMethod.GET)
    public Profile getProfile(final User user) throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

        // load the Profile Entity
        String userId = getUserId(user);
        Key key = Key.create(Profile.class, userId);

        return (Profile) ofy().load().key(key).now();
    }

    @ApiMethod(name = "createResponse", path = "response", httpMethod = ApiMethod.HttpMethod.POST)
    public Response createResponse(final User user, final ResponseForm responseForm) throws UnauthorizedException {

        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

        // Get the userId of the logged in User
        final String userId = getUserId(user);

        // Get the key for the User's Profile
        Key<Profile> profileKey = Key.create(Profile.class, getUserId(user));

        // Allocate a key for the response -- let App Engine allocate the ID
        // Don't forget to include the parent Profile in the allocated ID
        final Key<Response> responseKey = factory().allocateId(profileKey, Response.class);

        // Get the Response Id from the Key
        final long responseKeyId = responseKey.getId();

        // Get the default queue
        final Queue queue = QueueFactory.getDefaultQueue();

        // Start a transaction
        Response response;
        response = ofy().transact(new Work<Response>() {
            @Override
            public Response run() {

                // Get the existing Profile entity for the current user if there is one
                // Otherwise create a new Profile entity with default values
                Profile profile = getProfileFromUser(user);

                // Create a new Response Entity, specifying the user's Profile entity
                // as the parent of the response
                Response response = new Response(responseKeyId, userId, responseForm);

                // Save Response and Profile Entities
                ofy().save().entities(response, profile).now();

                queue.add(ofy().getTransaction(),
                        TaskOptions.Builder.withUrl("/tasks/send_confirmation_email")
                                .param("email", profile.getMainEmail())
                                .param("topicInfo", response.toString()));

                return response;
            }
        });

        return response;
    }

    @ApiMethod(name = "createQuestion", path = "question", httpMethod = ApiMethod.HttpMethod.POST)
    public Question createQuestion(final User user, final QuestionForm questionForm) throws UnauthorizedException {

        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

        // Get the userId of the logged in User
        final String userId = getUserId(user);

        // Get the key for the User's Profile
        Key<Profile> profileKey = Key.create(Profile.class, getUserId(user));

        // Allocate a key for the question -- let App Engine allocate the ID
        // Don't forget to include the parent Profile in the allocated ID
        final Key<Question> questionKey = factory().allocateId(profileKey, Question.class);

        // Get the Question Id from the Key
        final long questionKeyId = questionKey.getId();

        // Get the default queue
        final Queue queue = QueueFactory.getDefaultQueue();

        // Start a transaction
        Question question;
        question = ofy().transact(new Work<Question>() {
            @Override
            public Question run() {

                // Get the existing Profile entity for the current user if there is one
                // Otherwise create a new Profile entity with default values
                Profile profile = getProfileFromUser(user);

                // Create a new Question Entity, specifying the user's Profile entity
                // as the parent of the question
                Question question = new Question(questionKeyId, userId, questionForm);

                // Save Question and Profile Entities
                ofy().save().entities(question, profile).now();

                queue.add(ofy().getTransaction(),
                        TaskOptions.Builder.withUrl("/tasks/send_confirmation_email")
                                .param("email", profile.getMainEmail())
                                .param("topicInfo", question.toString()));

                return question;
            }
        });

        return question;
    }

    /*
     * Get the display name from the user's email. For example, if the email is
     * lemoncake@example.com, then the display name becomes "lemoncake."
     */
    private static String extractDefaultDisplayNameFromEmail(String email) {
        return email == null ? null : email.substring(0, email.indexOf("@"));
    }

    /**
     * Gets the Profile entity for the current user
     * or creates it if it doesn't exist
     *
     * @param user The user object for which to get the profile.
     * @return The user's Profile
     */
    private static Profile getProfileFromUser(User user) {
        // First fetch the user's Profile from the datastore.
        Profile profile = ofy().load().key(
                Key.create(Profile.class, getUserId(user))).now();
        if (profile == null) {
            // Create a new Profile if it doesn't exist.
            // Use default displayName and teeShirtSize
            String email = user.getEmail();
            profile = new Profile(getUserId(user),
                    extractDefaultDisplayNameFromEmail(email), email, TypeOfUser.NOT_SPECIFIED);
        }
        return profile;
    }

    /**
     * This is an ugly workaround for null userId for Android clients.
     *
     * @param user A User object injected by the cloud endpoints.
     * @return the App Engine userId for the user.
     */
    private static String getUserId(User user) {
        String userId = user.getUserId();
        if (userId == null) {
            LOG.info("userId is null, so trying to obtain it from the datastore.");

            AppEngineUser appEngineUser = new AppEngineUser(user);
            ofy().save().entity(appEngineUser).now();

            // Begin new session for not using session cache.
            Objectify objectify = ofy().factory().begin();
            AppEngineUser savedUser = objectify.load().key(appEngineUser.getKey()).now();

            userId = savedUser.getUser().getUserId();

            LOG.info("Obtained the userId: " + userId);
        }
        return userId;
    }

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }

}
