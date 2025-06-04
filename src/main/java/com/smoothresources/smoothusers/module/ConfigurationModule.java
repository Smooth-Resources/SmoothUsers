package com.smoothresources.smoothusers.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.smoothresources.smoothbase.common.file.YAMLFile;

public class ConfigurationModule extends AbstractModule {

    private final YAMLFile config;

    public ConfigurationModule(YAMLFile config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        bind(YAMLFile.class).annotatedWith(Names.named("config")).toInstance(config);
    }
}
