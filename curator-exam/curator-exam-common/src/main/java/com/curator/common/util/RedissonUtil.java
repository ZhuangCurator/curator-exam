package com.curator.common.util;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redisson 工具类
 *
 * @author Jun
 * @date 2021/4/18
 */
@Component
@ConditionalOnClass(RedissonClient.class)
public class RedissonUtil {

    private static RedissonClient redissonClient;

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;

    @PostConstruct
    public void init() {
        Config config = new Config();
        //使用json序列化方式
        config.setCodec(new JsonJacksonCodec());
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password);
        redissonClient = Redisson.create(config);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public static <T> void setCacheObject(final String key, final T value){
        RBucket<T> rBucket = redissonClient.getBucket(key);
        rBucket.set(value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public static <T> void setCacheObject(final String key, final T value, final Long timeout, final TimeUnit timeUnit){
        RBucket<T> rBucket = redissonClient.getBucket(key);
        rBucket.set(value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final long timeout, final TimeUnit unit){
        return redissonClient.getBucket(key).expire(timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public static <T> T getCacheObject(final String key){
        return (T) redissonClient.getBucket(key).get();
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存键值
     */
    public static boolean deleteObject(final String key){
        return redissonClient.getBucket(key).delete();
    }

    /**
     * 缓存List数据
     *
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存成功与否
     */
    public static <T> boolean setCacheList(final String key, final List<T> dataList){
        RList<T> rBucket = redissonClient.getList(key);
        if(Help.isNotEmpty(dataList)) {
            return rBucket.addAll(dataList);
        }
        return false;
    }

    /**
     * 缓存List数据
     *
     * @param key 缓存的键值
     * @param data 待缓存的数据
     * @return 缓存成功与否
     */
    public static <T> boolean setCacheListValue(final String key, T data){
        RList<T> rBucket = redissonClient.getList(key);
        if(Help.isNotEmpty(data)) {
            return rBucket.add(data);
        }
        return false;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public static <T> List<T> getCacheList(final String key){
        RList<T> rBucket = redissonClient.getList(key);
        return new ArrayList<>(rBucket);
    }

    /**
     * 缓存Set
     *
     * @param key 缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存成功与否
     */
    public static <T> boolean setCacheSet(final String key, final Set<T> dataSet){
        RSet<T> rBucket = redissonClient.getSet(key);
        if(Help.isNotEmpty(dataSet)) {
            return rBucket.addAll(dataSet);
        }
        return false;
    }

    /**
     * 获得缓存的set
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public static <T> Set<T> getCacheSet(final String key){
        RSet<T> rBucket = redissonClient.getSet(key);
        return new HashSet<>(rBucket);
    }

    /**
     * 缓存Set
     *
     * @param key 缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存成功与否
     */
    public static <T> boolean setCacheSortedSet(final String key, final SortedSet<T> dataSet){
        RSortedSet<T> rBucket = redissonClient.getSortedSet(key);
        if(Help.isNotEmpty(dataSet)) {
            return rBucket.addAll(dataSet);
        }
        return false;
    }

    /**
     * 获得缓存的set
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public static <T> SortedSet<T> getCacheSortedSet(final String key){
        RSortedSet<T> rBucket = redissonClient.getSortedSet(key);
        return new TreeSet<>(rBucket);
    }

    /**
     * 缓存Map
     *
     * @param key 缓存键值
     * @param dataMap 缓存的数据
     */
    public static <T> void setCacheMap(final String key, final Map<String, T> dataMap){
        RMap<String, T> rBucket = redissonClient.getMap(key);
        if(Help.isNotEmpty(dataMap)) {
            rBucket.putAll(dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public static <T> Map<String, T> getCacheMap(final String key){
        RMap<String, T> rBucket = redissonClient.getMap(key);
        return new HashMap<>(rBucket);
    }

    /**
     * 获得缓存的Map
     *
     * @param name 对象名
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public static <T> T getCacheMap(final String name, final String key){
        RMap<String, T> rBucket = redissonClient.getMap(name);
        return rBucket.get(key);
    }

    /**
     * 获取可重入锁
     *
     * @param name  锁名
     * @param timeout 过期时间(毫秒)
     */
    public static void lock(final String name, final long timeout) {
        RLock lock = redissonClient.getLock(name);
        lock.lock(timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 解除可重入锁
     *
     * @param name 锁名
     */
    public static void unlock(String name) {
        RLock lock = redissonClient.getLock(name);
        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
