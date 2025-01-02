package com.synchrony.assignment.studentmanagement.service;

import jakarta.annotation.PostConstruct;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService<T> {

    @Autowired
    private RedissonClient redissonClient;

    @Value("${redis.ttl}")
    private Long ttl;

    // RList methods
    public void saveList(String key, List<T> items) {
        RList<T> redisList = redissonClient.getList(key);
        redisList.clear();
        redisList.addAll(items);


        if (ttl > 0) {
            redissonClient.getKeys().expire(key, ttl, TimeUnit.SECONDS);
        }
    }

    public void saveItem(String key, T item) {
        RList<T> redisList = redissonClient.getList(key);
        redisList.add(item);
    }

    public List<T> getList(String key) {
        RList<T> redisList = redissonClient.getList(key);
        return redisList.readAll();
    }

    public boolean listExists(String key) {
        RList<Object> redisList = redissonClient.getList(key);
        return redisList.isExists();
    }

    public T getItem(String key, int index) {
        RList<T> redisList = redissonClient.getList(key);
        return redisList.get(index);
    }

    public void deleteItem(String key, int index) {
        RList<T> redisList = redissonClient.getList(key);
        redisList.remove(index);
    }

    public int deleteKeysByPattern(String pattern) {
        RKeys keys = redissonClient.getKeys();
        Iterator<String> iterator = keys.getKeysByPattern(pattern).iterator();
        int count = 0;
        while (iterator.hasNext()) {
            String key = iterator.next();
            redissonClient.getKeys().delete(key);
            count++;
        }
        return count;
    }

    // RBucket methods
    public void saveBucket(String key, T value) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        if (ttl > 0) {
            bucket.set(value, ttl, TimeUnit.SECONDS);
        } else {
            bucket.set(value);
        }
    }

    public T getBucket(String key) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    public boolean bucketExists(String key) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket.isExists();
    }

    public void deleteBucket(String key) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.delete();
    }
}
