package com.techpassport.server.common.data.redis.infrastructure.services;

import com.techpassport.server.common.data.redis.core.ports.services.IRedisCache;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class RedisCache implements IRedisCache {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisMessageListenerContainer listenerContainer;

    public RedisCache(
            RedisTemplate<String, String> redisTemplate,
            RedisMessageListenerContainer listenerContainer
    ) {
        this.redisTemplate = redisTemplate;
        this.listenerContainer = listenerContainer;
    }

    @Override
    public void setString(String key, String value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, ttl);
    }

    @Override
    public String getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void addToList(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public String[] getList(String key, long start, long end) {
        List<String> list = redisTemplate.opsForList().range(key, start, end);
        return list == null ? new String[0] : list.toArray(new String[0]);
    }

    @Override
    public boolean listContains(String key, String value) {
        List<String> list = redisTemplate.opsForList().range(key, 0, -1);
        return list != null && list.contains(value);
    }

    @Override
    public long getListLength(String key) {
        Long size = redisTemplate.opsForList().size(key);
        return size != null ? size : 0;
    }

    @Override
    public long removeFromList(String key, String value, long count) {
        Long removed = redisTemplate.opsForList().remove(key, count, value);
        return removed != null ? removed : 0;
    }

    @Override
    public void subscribe(String channel, RedisCallback callback) {
        MessageListener listener = (message, pattern) -> {
            String body = new String(message.getBody());
            callback.onMessage(body);
        };

        listenerContainer.addMessageListener(listener, new ChannelTopic(channel));
    }

    @Override
    public Duration getLifetime(String key) {
        Long seconds = redisTemplate.getExpire(key);
        return Duration.ofSeconds(seconds);
    }
}