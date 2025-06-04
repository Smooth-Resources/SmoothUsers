package com.smoothresources.smoothusers.module;

import com.google.inject.AbstractModule;
import com.smoothresources.smoothusers.SmoothUsers;

public class SmoothUsersModule extends AbstractModule {

    private final SmoothUsers plugin;

    public SmoothUsersModule(SmoothUsers plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(SmoothUsers.class).toInstance(plugin);
    }
}
