package net.smoothplugins.smoothusers.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import net.smoothplugins.smoothbase.serializer.Serializer;

public class SerializerModule extends AbstractModule {

    @Provides
    public Serializer getSerializer() {
        return new Serializer(null);
    }
}
