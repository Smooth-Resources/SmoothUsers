package com.smoothresources.smoothusers.loader;

import com.google.inject.Inject;
import com.smoothresources.smoothusersapi.user.UserService;
import org.bukkit.Bukkit;

public class MainLoader {

    @Inject
    private ListenerLoader listenerLoader;
    @Inject
    private UserService userService;

    public void load() {
        listenerLoader.load();
    }

    public void unload() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            userService.setTTLOfCacheByUUID(player.getUniqueId(), 600); // 10 minutes
            userService.setTTLOfCacheByUsername(player.getName(), 600); // 10 minutes
        });
    }
}
