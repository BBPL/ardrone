package com.google.api.client.extensions.appengine.auth;

import com.google.api.client.util.Preconditions;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

class AppEngineServletUtils {
    private AppEngineServletUtils() {
    }

    static final String getUserId() {
        User currentUser = UserServiceFactory.getUserService().getCurrentUser();
        Preconditions.checkState(currentUser != null, "This servlet requires the user to be logged in.");
        return currentUser.getUserId();
    }
}
