package net.smoothplugins.smoothusers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.smoothplugins.smoothbase.configuration.Configuration;
import net.smoothplugins.smoothusers.module.ConfigurationModule;
import net.smoothplugins.smoothusers.module.SmoothUsersModule;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmoothUsers extends JavaPlugin {

    private static Injector injector;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Configuration config = new Configuration(this, "config");

        injector = Guice.createInjector(
                new SmoothUsersModule(this),
                new ConfigurationModule(config)
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Injector getInjector() {
        return injector;
    }
}
