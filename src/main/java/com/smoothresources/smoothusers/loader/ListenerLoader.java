package com.smoothresources.smoothusers.loader;

import com.google.inject.Inject;
import com.smoothresources.smoothusers.SmoothUsers;
import com.smoothresources.smoothusers.listener.PlayerJoinListener;
import com.smoothresources.smoothusers.listener.PlayerQuitListener;
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
