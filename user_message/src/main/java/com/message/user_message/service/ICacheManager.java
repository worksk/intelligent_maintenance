package com.message.user_message.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @Auther: wyx
 * @Date: 2019-07-26 11:03
 * @Description: Redis 工具类接口
 */
public interface ICacheManager {

    /**
     * 删除缓存
     * @param key
     */
    void del(String key);

    /**
     * 删除匹配的所有缓存
     * @param pattern
     */
    void delAll(String pattern);

    /**
     * 获取指定缓存 key 的值
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * 获取指定缓存 key 的值, 同时设置截止时间
     * @param key
     * @param expire
     * @return
     */
    Object get(String key, Integer expire);

    /**
     * 获取匹配的所有 key 的值
     * @param pattern
     * @return
     */
    Set<Object> getAll(String pattern);

    /**
     * 获取匹配的所有 key 的值, 同时设置截止时间
     * @param pattern
     * @param expire
     * @return
     */
    Set<Object> getAll(String pattern, Integer expire);

    /**
     * 指定缓存 key 是否存在
     * @param key
     * @return
     */
    Boolean exists(String key);

    /**
     * 设置缓存 key
     * <p>
     *  当缓存 key 不存在
     * </p>
     * @param key
     * @param value
     * @return
     */
    Boolean setNX(String key, Serializable value);

    /**
     * 设置缓存 key 及其过期时间
     * <p>
     *  当缓存 key 不存在
     * </p>
     * @param key
     * @param value
     * @return
     */
    Boolean setEX(String key, Long expireTime, Serializable value);

    /**
     * 设置缓存 key
     * <p>
     *     当缓存 key 存在
     * </p>
     * @param key
     * @param value
     * @param time
     */
    void set(String key, Serializable value, Integer time);

    /**
     * 设置缓存-存活时间默认
     * @param key
     * @param value
     */
    void set(String key, Serializable value);

    /**
     * 给指定缓存 key 加锁
     * @param key
     * @return
     */
    Boolean lock(String key);

    /**
     * 给指定缓存 key 解锁
     * @param key
     */
    void unlock(String key);

    /**
     * 指定缓存 key 的命中值, 重新设置为默认过期时间
     * @param key
     * @return
     */
    Object getFire(String key);

    /**
     * 设置指定缓存 key 的过期时间
     * @param key
     * @param time
     * @return
     */
    Boolean expire(String key, Integer time);

    /**
     * 设置指定缓存 key 的过期时间
     * @param key
     * @param unixTime
     * @return
     */
    Boolean expireAt(String key, Long unixTime);

    /**
     * 缓存 key 的存活时间
     * @param key
     * @return
     */
    Long ttl(String key);

    /**
     * 获取指定缓存 key 值的类型
     * @param key
     * @return
     */
    String type(String key);

    /**
     * 设置新值, 返回旧值
     * @param key
     * @param newValue
     * @return
     */
    Object getAndSet(String key, Serializable newValue);

    /**
     * 设置缓存-Map 类型
     * @param key Map 对象的标识
     * @param field 缓存字段 key
     * @param value 缓存字段 key 值
     */
    void hset(String key, Serializable field, Serializable value);

    /**
     * 获取缓存值-Map 类型
     * @param key Map 对象的标识
     * @param field 缓存字段 key
     * @return
     */
    Object hget(String key, Serializable field);

    /**
     * 删除缓存值-Map 类型
     * @param key Map 对象的标识
     * @param field 缓存字段 key
     */
    void hdel(String key, Serializable field);

    /**
     * 设置缓存-Set 类型
     * @param key
     * @param value
     */
    void sadd(String key, Serializable value);

    /**
     * 获取缓存 key 值-Set 类型
     * @param key
     * @return
     */
    Set<Object> sall(String key);

    /**
     * 删除指定缓存 key 值-Set 类型
     * @param key
     * @param value
     */
    void sdel(String key, Serializable value);

    /**
     * 设置缓存值-List 类型
     * @param key
     * @param index
     * @param value
     */
    void setRange(String key, Integer index, Serializable value);

    /**
     * 向缓存中的 key 添加值
     * @param key
     * @param value
     */
    void pushRange(String key, Serializable value);

    /**
     * 获取缓存值-List 类型
     * @param key
     * @return
     */
    List getRange(String key, long startOffset, long endOffset);

    /**
     * 设置缓存-多个值
     * @param temps
     */
    void multiSet(Map<String, Object> temps);

    /**
     * scan 实现
     * @param pattern
     * @param consumer
     */
    void scan(String pattern, Consumer<byte[]> consumer);

    /**
     * 获取符合条件的key
     * @param pattern 表达式
     * @return
     */
    Set<Serializable> keys(String pattern);

}

