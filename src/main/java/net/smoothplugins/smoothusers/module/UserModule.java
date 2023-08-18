package net.smoothplugins.smoothusers.module;

import com.google.inject.AbstractModule;
import net.smoothplugins.smoothusers.user.DefaultUserService;
import net.smoothplugins.smoothusersapi.user.UserService;

public class UserModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserService.class).to(DefaultUserService.class);
    }
}
