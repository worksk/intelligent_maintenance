package com.message.user_message.service.impl;

import com.message.user_message.service.ICacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @Auther: wyx
 * @Date: 2019-07-26 11:04
 * @Description:
 */
public class RedisHelper implements ICacheManager {

    /**
     * 过期时间
     */
    private final Integer EXPIRE = 1000 * 600;
    private RedisSerializer<String> keySerializer;
    private RedisSerializer<Object> valueSerializer;
    private RedisTemplate<Serializable, Serializable> redisTemplate;

    public RedisHelper(RedisTemplate<Serializable, Serializable> redisTemplate){
        this.redisTemplate = redisTemplate;
        this.keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        this.valueSerializer = (RedisSerializer<Object>) redisTemplate.getValueSerializer();
    }

    @Override
    public void del(String key) {
        this.redisTemplate.delete(key);
    }

    @Override
    public void delAll(String pattern) {
        this.redisTemplate.delete(keys(pattern));
    }

    @Override
    public Object get(String key) {
        return this.redisTemplate.boundValueOps(key).get();
    }

    @Override
    public Object get(String key, Integer expire) {
        this.expire(key, expire);
        return this.redisTemplate.boundValueOps(key).get();
    }

    @Override
    public Set<Object> getAll(String pattern) {
        Set<Object> values = new HashSet<>();
        Set<Serializable> keys = keys(pattern);
        if(keys != null){
            for (Serializable key : keys) {
                values.add(this.redisTemplate.opsForValue().get(key));
            }
        }
        return values;
    }

    @Override
    public Set<Object> getAll(String pattern, Integer expire) {
        Set<Object> values = new HashSet<>();
        Set<Serializable> keys = keys(pattern);
        if(keys != null){
            for (Serializable key : keys) {
                this.expire(key.toString(), expire);
                values.add(this.redisTemplate.opsForValue().get(key));
            }
        }
        return values;
    }

    @Override
    public Boolean exists(String key) {
        return this.redisTemplate.hasKey(key);
    }

    @Override
    public Boolean setNX(String key, Serializable value) {
        RedisConnectionFactory factory = this.redisTemplate.getConnectionFactory();
        RedisConnection redisConnection = null;
        Boolean isDone;
        try{
            redisConnection = RedisConnectionUtils.getConnection(factory);
            if(redisConnection != null){
                return redisConnection.setNX(this.keySerializer.serialize(key), this.valueSerializer.serialize(value));
            }
            isDone = this.redisTemplate.boundValueOps(key).setIfAbsent(value);
        }finally {
            RedisConnectionUtils.releaseConnection(redisConnection, factory);
        }
        return isDone;
    }

    @Override
    public Boolean setEX(String key, Long expireTime, Serializable value) {
        return this.redisTemplate.boundValueOps(key).setIfAbsent(value, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Serializable value, Integer time) {
        this.redisTemplate.boundValueOps(key).set(value);
        this.expire(key, time);
    }

    @Override
    public void set(String key, Serializable value) {
        this.redisTemplate.boundValueOps(key).set(value);
        this.expire(key, this.EXPIRE);
    }

    @Override
    public Boolean lock(String key) {
        RedisConnectionFactory factory = this.redisTemplate.getConnectionFactory();
        RedisConnection redisConnection = null;
        Boolean isDone;
        try {
            redisConnection = RedisConnectionUtils.getConnection(factory);
            if(redisConnection == null){
                return this.redisTemplate.boundValueOps(key).setIfAbsent("0");
            }
            isDone = redisConnection.setNX(this.keySerializer.serialize(key), this.valueSerializer.serialize("0"));
        }finally {
            RedisConnectionUtils.releaseConnection(redisConnection, factory);
        }
        return isDone;
    }

    @Override
    public void unlock(String key) {
        this.redisTemplate.delete(key);
    }

    @Override
    public Object getFire(String key) {
        this.expire(key, this.EXPIRE);
        return this.redisTemplate.boundValueOps(key).get();
    }

    @Override
    public Boolean expire(String key, Integer time) {
        return this.redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    @Override
    public Boolean expireAt(String key, Long unixTime) {
        return this.redisTemplate.expireAt(key, new Date(unixTime));
    }

    @Override
    public Long ttl(String key) {
        return this.redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public String type(String key) {
        return this.redisTemplate.type(key).toString();
    }

    @Override
    public Object getAndSet(String key, Serializable newValue) {
        return this.redisTemplate.boundValueOps(key).getAndSet(newValue);
    }

    @Override
    public void hset(String key, Serializable field, Serializable value) {
        this.redisTemplate.boundHashOps(key).put(field, value);
    }

    @Override
    public Object hget(String key, Serializable field) {
        return this.redisTemplate.boundHashOps(key).get(field);
    }

    @Override
    public void hdel(String key, Serializable field) {
        this.redisTemplate.boundHashOps(key).delete(new Object[]{field});
    }

    @Override
    public void sadd(String key, Serializable value) {
        this.redisTemplate.boundSetOps(key).add(value);
    }

    @Override
    public Set<Object> sall(String key) {
        Set<Object> values = new HashSet<>();
        Set<Serializable> members = this.redisTemplate.boundSetOps(key).members();
        if(members != null){
            values.addAll(members);
        }
        return values;
    }

    @Override
    public void sdel(String key, Serializable value) {
        this.redisTemplate.boundSetOps(key).remove(value);
    }

    @Override
    public void setRange(String key, Integer index, Serializable value) {
        this.redisTemplate.boundListOps(key).set(index, value);
    }

    @Override
    public void pushRange(String key, Serializable value) {
        this.redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public List getRange(String key, long startOffset, long endOffset) {
        return this.redisTemplate.opsForList().range(key, startOffset, endOffset);
    }

    @Override
    public void multiSet(Map<String, Object> temps) {
        for (String s : temps.keySet()) {
            String key = String.valueOf(s);
            Object value = temps.get(key);
            if(value instanceof Map){
                Map<String, Object> paramMap = (Map<String, Object>) value;
                for(String valueKey : paramMap.keySet()){
                    hset(key, String.valueOf(valueKey), String.valueOf(paramMap.get(valueKey)));
                }
            }else if(value instanceof Set){
                Set<Serializable> paramSet = (Set<Serializable>) value;
                for(Serializable param : paramSet){
                    sadd(key, param);
                }
            }else if(value instanceof List){
                del(key);
                List<Serializable> paramList = (List<Serializable>) value;
                for(Serializable param : paramList){
                    pushRange(key, param);
                }
            }else {
                Serializable param = String.valueOf(value);
                set(key, param);
            }
        }
    }

    @Override
    public void scan(String pattern, Consumer<byte[]> consumer) {
        this.redisTemplate.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(Long.MAX_VALUE).match(pattern).build())) {
                cursor.forEachRemaining(consumer);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Set<Serializable> keys(String pattern) {
        Set<Serializable> keys = new HashSet<>();
        this.scan(pattern, item -> {
            //符合条件的key
            String key = new String(item, StandardCharsets.UTF_8);
            keys.add(key);
        });
        return keys;
    }
}
