package com.smoothresources.smoothusers.module;

import com.google.inject.AbstractModule;
import com.smoothresources.smoothbase.common.serializer.Serializer;

import java.util.List;

public class SerializerModule extends AbstractModule {

    @Override
    protected void configure() {
        Serializer serializer = new Serializer.Builder()
                .registerDefaultAdapters(List.of())
                .build();

        bind(Serializer.class).toInstance(serializer);
    }
}
