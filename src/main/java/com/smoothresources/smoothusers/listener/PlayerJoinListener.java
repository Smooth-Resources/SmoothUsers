package com.smoothresources.smoothusers.listener;

import com.google.inject.Inject;
import com.smoothresources.smoothusers.SmoothUsers;
import com.smoothresources.smoothusersapi.service.Destination;
import com.smoothresources.smoothusersapi.user.User;
import com.smoothresources.smoothusersapi.user.UserService;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @Inject
    private UserService userService;
    @Inject
    private SmoothUsers plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            User user = userService.getUserByUUID(event.getPlayer().getUniqueId()).orElseGet(() -> {
                User newUser = new User(event.getPlayer().getUniqueId(), event.getPlayer().getName());
                userService.create(newUser);
                return newUser;
            });

            if (!user.getUsername().equals(event.getPlayer().getName())) {
                // User has changed his name.
                if (userService.containsByUsername(event.getPlayer().getName())) {
                    // The new username is already taken. This is because the unknown user has changed his name,
                    // but he didn't connect to the server again.
                    User unknownUser = userService.getUserByUsername(event.getPlayer().getName()).orElse(null);
                    if (unknownUser == null) return;

                    unknownUser.updateUsername("");
                    userService.update(unknownUser, Destination.STORAGE, Destination.CACHE_IF_PRESENT);
                }

                userService.deleteByUUID(user.getUuid(), Destination.CACHE_IF_PRESENT);
                userService.deleteByUsername(user.getUsername(), Destination.CACHE_IF_PRESENT);
                user.updateUsername(event.getPlayer().getName());
                userService.update(user, Destination.STORAGE, Destination.CACHE_IF_PRESENT);
            }

            if (userService.cacheContainsByUUID(user.getUuid())) {
                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                    if (!event.getPlayer().isOnline()) return;

                    if (!userService.removeTTLFromCacheByUUID(user.getUuid()) | !userService.removeTTLFromCacheByUsername(user.getLowerCaseUsername())) {
                        userService.loadToCache(user);
                    }
                }, 20L * 5);
            } else {
                userService.loadToCache(user);
            }
        });
    }
}
