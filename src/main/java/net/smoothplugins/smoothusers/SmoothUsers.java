package net.smoothplugins.smoothusers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.smoothplugins.smoothbase.configuration.Configuration;
import net.smoothplugins.smoothbase.connection.MongoConnection;
import net.smoothplugins.smoothbase.connection.RedisConnection;
import net.smoothplugins.smoothusers.api.DefaultSmoothUsersAPI;
import net.smoothplugins.smoothusers.loader.MainLoader;
import net.smoothplugins.smoothusers.module.*;
import net.smoothplugins.smoothusersapi.SmoothUsersAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmoothUsers extends JavaPlugin {

    private static Injector injector;

    @Override
    public void onLoad() {
        // Plugin startup logic
        Configuration config = new Configuration(this, "config");

        String uri = config.getString("mongo.uri");
        String databaseName = config.getString("mongo.database");
        MongoConnection mongoConnection = new MongoConnection(uri, databaseName);

        String redisHost = config.getString("redis.host");
        int redisPort = config.getInt("redis.port");
        String redisPassword = config.getString("redis.password");
        String redisPrefix = config.getString("redis.cluster");
        RedisConnection redisConnection = new RedisConnection(redisHost, redisPort, redisPassword, redisPrefix);

        injector = Guice.createInjector(
                new SmoothUsersModule(this),
                new ConfigurationModule(config),
                new ConnectionModule(mongoConnection, redisConnection),
                new StorageModule(),
                new UserModule(),
                new SerializerModule()
        );

        getServer().getServicesManager().register(
                SmoothUsersAPI.class,
                injector.getInstance(DefaultSmoothUsersAPI.class),
                this,
                org.bukkit.plugin.ServicePriority.Normal
        );
    }

    @Override
    public void onEnable() {
        injector.getInstance(MainLoader.class).load();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        injector.getInstance(MainLoader.class).unload();
    }

    public static Injector getInjector() {
        return injector;
    }
}
