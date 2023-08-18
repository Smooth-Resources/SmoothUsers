package net.smoothplugins.smoothusers.loader;

import com.google.inject.Inject;
import net.smoothplugins.smoothusers.SmoothUsers;
import net.smoothplugins.smoothusers.listener.PlayerJoinListener;
import net.smoothplugins.smoothusers.listener.PlayerQuitListener;
import org.bukkit.Bukkit;

public class ListenerLoader {

    @Inject
    private SmoothUsers smoothUsers;
    @Inject
    private PlayerJoinListener playerJoinListener;
    @Inject
    private PlayerQuitListener playerQuitListener;

    public void load() {
        Bukkit.getPluginManager().registerEvents(playerJoinListener, smoothUsers);
        Bukkit.getPluginManager().registerEvents(playerQuitListener, smoothUsers);
    }
}
