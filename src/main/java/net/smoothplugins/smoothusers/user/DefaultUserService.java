package net.smoothplugins.smoothusers.user;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.smoothplugins.smoothbase.configuration.Configuration;
import net.smoothplugins.smoothbase.serializer.Serializer;
import net.smoothplugins.smoothbase.storage.MongoStorage;
import net.smoothplugins.smoothbase.storage.RedisStorage;
import net.smoothplugins.smoothusersapi.service.Destination;
import net.smoothplugins.smoothusersapi.user.User;
import net.smoothplugins.smoothusersapi.user.UserService;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public class DefaultUserService implements UserService {

    @Inject @Named("user")
    private MongoStorage mongoStorage;
    @Inject @Named("user")
    private RedisStorage redisStorage;
    @Inject @Named("config")
    private Configuration config;
    @Inject
    private Serializer serializer;

    @Override
    public void create(User user) {
        mongoStorage.create(serializer.serialize(user));
    }

    @Override
    public void update(User user, Destination... destinations) {
        for (Destination destination : destinations) {
            switch (destination) {
                case STORAGE -> {
                    mongoStorage.update("_id", user.getUuid().toString(), serializer.serialize(user));
                }

                case CACHE -> {
                    redisStorage.update(user.getUuid().toString(), serializer.serialize(user));
                    redisStorage.update(user.getLowerCaseUsername(), serializer.serialize(user));
                }

                case CACHE_IF_PRESENT -> {
                    if (redisStorage.contains(user.getUuid().toString())) {
                        redisStorage.update(user.getUuid().toString(), serializer.serialize(user));
                        redisStorage.update(user.getLowerCaseUsername(), serializer.serialize(user));
                    }
                }
            }
        }
    }

    @Override
    public boolean containsByUUID(UUID uuid) {
        return redisStorage.contains(uuid.toString()) || mongoStorage.contains("_id", uuid.toString());
    }

    @Override
    public boolean containsByUsername(String username) {
        return redisStorage.contains(username.toLowerCase()) || mongoStorage.contains("lowerCaseUsername", username.toLowerCase(Locale.ROOT));
    }

    @Override
    public Optional<User> getUserByUUID(UUID uuid) {
        User user = serializer.deserialize(redisStorage.get(uuid.toString()), User.class);
        if (user != null) return Optional.of(user);

        user = serializer.deserialize(mongoStorage.get("_id", uuid.toString()), User.class);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        User user = serializer.deserialize(redisStorage.get(username.toLowerCase(Locale.ROOT)), User.class);
        if (user != null) return Optional.of(user);

        user = serializer.deserialize(mongoStorage.get("lowerCaseUsername", username.toLowerCase(Locale.ROOT)), User.class);
        return Optional.ofNullable(user);
    }

    @Override
    public void deleteByUUID(UUID uuid, Destination... destinations) {
        for (Destination destination : destinations) {
            switch (destination) {
                case STORAGE -> {
                    mongoStorage.delete("_id", uuid.toString());
                }

                case CACHE -> {
                    User user = getUserByUUID(uuid).orElse(null);
                    if (user == null) continue;

                    redisStorage.delete(uuid.toString());
                    redisStorage.delete(user.getLowerCaseUsername());
                }

                case CACHE_IF_PRESENT -> {
                    if (redisStorage.contains(uuid.toString())) {
                        User user = getUserByUUID(uuid).orElse(null);
                        if (user == null) continue;

                        redisStorage.delete(uuid.toString());
                        redisStorage.delete(user.getLowerCaseUsername());
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
                    mongoStorage.delete("lowerCaseUsername", username.toLowerCase(Locale.ROOT));
                }

                case CACHE -> {
                    User user = getUserByUsername(username).orElse(null);
                    if (user == null) continue;

                    redisStorage.delete(user.getUuid().toString());
                    redisStorage.delete(user.getLowerCaseUsername());
                }

                case CACHE_IF_PRESENT -> {
                    if (redisStorage.contains(username.toLowerCase(Locale.ROOT))) {
                        User user = getUserByUsername(username).orElse(null);
                        if (user == null) continue;

                        redisStorage.delete(user.getUuid().toString());
                        redisStorage.delete(user.getLowerCaseUsername());
                    }
                }
            }
        }
    }

    @Override
    public boolean cacheContainsByUUID(UUID uuid) {
        return redisStorage.contains(uuid.toString());
    }

    @Override
    public boolean cacheContainsByUsername(String username) {
        return redisStorage.contains(username.toLowerCase(Locale.ROOT));
    }

    @Override
    public void loadToCache(User user) {
        redisStorage.update(user.getUuid().toString(), serializer.serialize(user));
        redisStorage.update(user.getLowerCaseUsername(), serializer.serialize(user));
    }

    @Override
    public boolean removeTTLFromCacheByUUID(UUID uuid) {
        return redisStorage.removeTTL(uuid.toString());
    }

    @Override
    public boolean removeTTLFromCacheByUsername(String username) {
        return redisStorage.removeTTL(username.toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean setTTLOfCacheByUUID(UUID uuid, int seconds) {
        return redisStorage.setTTL(uuid.toString(), seconds);
    }

    @Override
    public boolean setTTLOfCacheByUsername(String username, int seconds) {
        return redisStorage.setTTL(username.toLowerCase(Locale.ROOT), seconds);
    }
}
