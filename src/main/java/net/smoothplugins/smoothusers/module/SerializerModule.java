package net.smoothplugins.smoothusers.module;

import com.google.inject.AbstractModule;
import net.smoothplugins.common.serializer.Serializer;

public class SerializerModule extends AbstractModule {

    @Override
    protected void configure() {
        Serializer serializer = new Serializer.Builder()
                .registerDefaultAdapters()
                .build();

        bind(Serializer.class).toInstance(serializer);
    }
}
