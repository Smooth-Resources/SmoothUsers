package net.smoothplugins.smoothusers.module;

import com.google.inject.AbstractModule;
import net.smoothplugins.smoothusers.SmoothUsers;

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
