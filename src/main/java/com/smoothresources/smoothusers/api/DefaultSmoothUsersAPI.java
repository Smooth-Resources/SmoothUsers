package com.smoothresources.smoothusers.api;

import com.google.inject.Inject;
import com.smoothresources.smoothusersapi.SmoothUsersAPI;
import com.smoothresources.smoothusersapi.user.UserService;

public class DefaultSmoothUsersAPI implements SmoothUsersAPI {

    @Inject
    private UserService userService;

    @Override
    public UserService getUserService() {
        return userService;
    }
}
