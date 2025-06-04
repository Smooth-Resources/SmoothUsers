package com.smoothresources.smoothusers.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.smoothresources.smoothbase.common.database.nosql.MongoDBDatabase;
import com.smoothresources.smoothbase.common.database.nosql.RedisDatabase;

public class StorageModule extends AbstractModule {

    private final MongoDBDatabase userMongoDBDatabase;
    private final RedisDatabase userRedisDatabase;

    public StorageModule(MongoDBDatabase userMongoDBDatabase, RedisDatabase userRedisDatabase) {
        this.userMongoDBDatabase = userMongoDBDatabase;
        this.userRedisDatabase = userRedisDatabase;
    }

    @Override
    protected void configure() {
        bind(MongoDBDatabase.class).annotatedWith(Names.named("user")).toInstance(userMongoDBDatabase);
        bind(RedisDatabase.class).annotatedWith(Names.named("user")).toInstance(userRedisDatabase);
    }
}
