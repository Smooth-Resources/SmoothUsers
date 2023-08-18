package net.smoothplugins.smoothusers.loader;

import com.google.inject.Inject;
import net.smoothplugins.smoothusers.SmoothUsers;
import net.smoothplugins.smoothusers.listener.PlayerJoinListener;
import org.bukkit.Bukkit;

public class ListenerLoader {

    @Inject
    private SmoothUsers smoothUsers;
    @Inject
    private PlayerJoinListener playerJoinListener;

    public void load() {
        Bukkit.getPluginManager().registerEvents(playerJoinListener, smoothUsers);
    }
}
