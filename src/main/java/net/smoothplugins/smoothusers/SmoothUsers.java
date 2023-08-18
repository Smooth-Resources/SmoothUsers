package net.smoothplugins.smoothusers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.smoothplugins.smoothusers.module.SmoothUsersModule;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmoothUsers extends JavaPlugin {

    private static Injector injector;

    @Override
    public void onEnable() {
        // Plugin startup logic
        injector = Guice.createInjector(
                new SmoothUsersModule(this)
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
