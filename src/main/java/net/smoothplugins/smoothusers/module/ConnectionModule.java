package net.smoothplugins.smoothusers.module;

import com.google.inject.AbstractModule;
import net.smoothplugins.common.connection.RedisConnection;

public class ConnectionModule extends AbstractModule {

    private final RedisConnection redisConnection;

    public ConnectionModule(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
    }

    @Override
    protected void configure() {
        bind(RedisConnection.class).toInstance(redisConnection);
    }
}
