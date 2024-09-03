package net.smoothplugins.smoothusers.module;

import com.google.inject.AbstractModule;
import net.smoothplugins.smoothbase.common.serializer.Serializer;

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
