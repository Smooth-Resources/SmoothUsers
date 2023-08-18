package net.smoothplugins.smoothusers.api;

import com.google.inject.Inject;
import net.smoothplugins.smoothusersapi.SmoothUsersAPI;
import net.smoothplugins.smoothusersapi.user.UserService;

public class DefaultSmoothUsersAPI implements SmoothUsersAPI {

    @Inject
    private UserService userService;

    @Override
    public UserService getUserService() {
        return userService;
    }
}
