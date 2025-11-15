package com.flashcards.server.common.redis.core.ports.services;

import java.time.Duration;

public interface IRedisCache {

    void setString(String key, String value, Duration ttl);

    String getString(String key);

    boolean delete(String key);

    boolean exists(String key);

    void addToList(String key, String value);

    String[] getList(String key, long start, long end);

    boolean listContains(String key, String value);

    long getListLength(String key);

    long removeFromList(String key, String value, long count);

    void subscribe(String channel, RedisCallback callback);

    Duration getLifetime(String key);

    @FunctionalInterface
    interface RedisCallback {
        void onMessage(String message);
    }
}
