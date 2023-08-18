package net.smoothplugins.smoothusers.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import net.smoothplugins.smoothbase.configuration.Configuration;

public class ConfigurationModule extends AbstractModule {

    private final Configuration config;

    public ConfigurationModule(Configuration config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        bind(Configuration.class).annotatedWith(Names.named("config")).toInstance(config);
    }
}
