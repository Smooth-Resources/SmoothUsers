package com.smoothresources.smoothusers.module;

import com.google.inject.AbstractModule;
import com.smoothresources.smoothusers.user.DefaultUserService;
import com.smoothresources.smoothusersapi.user.UserService;

public class UserModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserService.class).to(DefaultUserService.class).asEagerSingleton();
    }
}
