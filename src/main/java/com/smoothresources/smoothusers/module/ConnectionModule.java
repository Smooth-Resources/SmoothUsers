package com.smoothresources.smoothusers.module;

import com.google.inject.AbstractModule;
import com.smoothresources.smoothbase.common.connection.RedisConnection;

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
