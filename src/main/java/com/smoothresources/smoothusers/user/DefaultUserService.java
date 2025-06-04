package com.smoothresources.smoothusers.user;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.smoothresources.smoothbase.common.database.nosql.MongoDBDatabase;
import com.smoothresources.smoothbase.common.database.nosql.RedisDatabase;
import com.smoothresources.smoothbase.common.file.YAMLFile;
import com.smoothresources.smoothbase.common.serializer.Serializer;
import com.smoothresources.smoothusersapi.service.Destination;
import com.smoothresources.smoothusersapi.user.User;
import com.smoothresources.smoothusersapi.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public class DefaultUserService implements UserService {

    @Inject @Named("user")
    private MongoDBDatabase storage;
    @Inject @Named("user")
    private RedisDatabase cache;
    @Inject @Named("config")
    private YAMLFile config;
    @Inject
    private Serializer serializer;

    private List<User> usersInCache;
    private LocalDateTime lastCacheUpdate;

    @Override
    public void create(User user) {
        storage.insert(user.getUuid().toString(), serializer.serialize(user));
    }

    @Override
    public void update(User user, Destination... destinations) {
        for (Destination destination : destinations) {
            switch (destination) {
                case STORAGE -> {
                    storage.update(user.getUuid().toString(), serializer.serialize(user));
                }

                case CACHE -> {
                    cache.update(user.getUuid().toString(), serializer.serialize(user));
                    cache.update(user.getLowerCaseUsername(), serializer.serialize(user));
                }

                case CACHE_IF_PRESENT -> {
                    if (cache.exists(user.getUuid().toString())) {
                        cache.update(user.getUuid().toString(), serializer.serialize(user));
                        cache.update(user.getLowerCaseUsername(), serializer.serialize(user));
                    }
                }
            }
        }
    }

    @Override
    public boolean containsByUUID(UUID uuid) {
        return cache.exists(uuid.toString()) || storage.exists(uuid.toString());
    }

    @Override
    public boolean containsByUsername(String username) {
        return cache.exists(username.toLowerCase()) || storage.exists("lowerCaseUsername", username.toLowerCase());
    }

    @Override
    public Optional<User> getUserByUUID(UUID uuid) {
        User user = serializer.deserialize(cache.get(uuid.toString()), User.class);
        if (user != null) return Optional.of(user);

        user = serializer.deserialize(storage.get(uuid.toString()), User.class);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        User user = serializer.deserialize(cache.get(username.toLowerCase(Locale.ROOT)), User.class);
        if (user != null) return Optional.of(user);

        user = serializer.deserialize(storage.get("lowerCaseUsername", username.toLowerCase(Locale.ROOT)), User.class);
        return Optional.ofNullable(user);
    }

    @Override
    public void deleteByUUID(UUID uuid, Destination... destinations) {
        for (Destination destination : destinations) {
            switch (destination) {
                case STORAGE -> {
                    storage.delete(uuid.toString());
                }

                case CACHE -> {
                    User user = getUserByUUID(uuid).orElse(null);
                    if (user == null) continue;

                    cache.delete(uuid.toString());
                    cache.delete(user.getLowerCaseUsername());
                }

                case CACHE_IF_PRESENT -> {
                    if (cache.exists(uuid.toString())) {
                        User user = getUserByUUID(uuid).orElse(null);
                        if (user == null) continue;

                        cache.delete(uuid.toString());
                        cache.delete(user.getLowerCaseUsername());
                    }
                }
            }
        }
    }

    @Override
    public void deleteByUsername(String username, Destination... destinations) {
        for (Destination destination : destinations) {
            switch (destination) {
                case STORAGE -> {
                    storage.delete("lowerCaseUsername", username.toLowerCase(Locale.ROOT));
                }

                case CACHE -> {
                    User user = getUserByUsername(username).orElse(null);
                    if (user == null) continue;

                    cache.delete(user.getUuid().toString());
                    cache.delete(user.getLowerCaseUsername());
                }

                case CACHE_IF_PRESENT -> {
                    if (cache.exists(username.toLowerCase(Locale.ROOT))) {
                        User user = getUserByUsername(username).orElse(null);
                        if (user == null) continue;

                        cache.delete(user.getUuid().toString());
                        cache.delete(user.getLowerCaseUsername());
                    }
                }
            }
        }
    }

    @Override
    public boolean cacheContainsByUUID(UUID uuid) {
        return cache.exists(uuid.toString());
    }

    @Override
    public boolean cacheContainsByUsername(String username) {
        return cache.exists(username.toLowerCase(Locale.ROOT));
    }

    @Override
    public void loadToCache(User user) {
        cache.update(user.getUuid().toString(), serializer.serialize(user));
        cache.update(user.getLowerCaseUsername(), serializer.serialize(user));
    }

    @Override
    public boolean removeTTLFromCacheByUUID(UUID uuid) {
        return cache.removeTTL(uuid.toString());
    }

    @Override
    public boolean removeTTLFromCacheByUsername(String username) {
        return cache.removeTTL(username.toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean setTTLOfCacheByUUID(UUID uuid, int seconds) {
        return cache.setTTL(uuid.toString(), seconds);
    }

    @Override
    public boolean setTTLOfCacheByUsername(String username, int seconds) {
        return cache.setTTL(username.toLowerCase(Locale.ROOT), seconds);
    }

    @Override
    public List<User> getAllFromCache(boolean forceUpdate) {
        if (forceUpdate) {
            usersInCache = cache.getValues("").stream().map(userJSON -> serializer.deserialize(userJSON, User.class)).toList();
            lastCacheUpdate = LocalDateTime.now();
            return usersInCache;
        }

        if (usersInCache == null || lastCacheUpdate.plusSeconds(config.getInt("update-get-all-every")).isBefore(LocalDateTime.now())) {
            return getAllFromCache(true);
        }

        return usersInCache;
    }
}
