package net.smoothplugins.smoothusers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.smoothplugins.smoothbase.common.connection.RedisConnection;
import net.smoothplugins.smoothbase.common.database.nosql.MongoDBDatabase;
import net.smoothplugins.smoothbase.common.database.nosql.RedisDatabase;
import net.smoothplugins.smoothbase.common.file.YAMLFile;
import net.smoothplugins.smoothbase.paper.file.PaperYAMLFile;
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
        YAMLFile config = new PaperYAMLFile(this, "config");
        System.out.println("Test: " + config.getClass());

        String redisHost = config.getString("redis", "host");
        int redisPort = config.getInt("redis", "port");
        String redisPassword = config.getString("redis", "password");
        RedisConnection redisConnection = new RedisConnection(redisHost, redisPassword, redisPort, getLogger());
        redisConnection.connect();

        String uri = config.getString("mongo", "uri");
        String databaseName = config.getString("mongo", "database");
        MongoDBDatabase mongoDatabase = new MongoDBDatabase(uri, databaseName, "users");
        mongoDatabase.connect();

        String redisCluster = config.getString("redis", "cluster");
        RedisDatabase redisDatabase = new RedisDatabase(redisConnection, redisCluster + ":users:");

        injector = Guice.createInjector(
                new SmoothUsersModule(this),
                new ConfigurationModule(config),
                new ConnectionModule(redisConnection),
                new StorageModule(mongoDatabase, redisDatabase),
                new UserModule(),
                new SerializerModule()
        );

        System.out.println("Test2: " + injector.getClass());

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
