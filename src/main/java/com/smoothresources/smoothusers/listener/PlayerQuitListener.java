package com.smoothresources.smoothusers.listener;

import com.google.inject.Inject;
import com.smoothresources.smoothusers.SmoothUsers;
import com.smoothresources.smoothusersapi.user.UserService;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @Inject
    private UserService userService;
    @Inject
    private SmoothUsers plugin;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            userService.setTTLOfCacheByUUID(event.getPlayer().getUniqueId(), 600); // 10 minutes
            userService.setTTLOfCacheByUsername(event.getPlayer().getName(), 600); // 10 minutes
        });
    }
}
