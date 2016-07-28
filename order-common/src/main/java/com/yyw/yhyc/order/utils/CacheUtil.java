package com.yyw.yhyc.order.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * Created by shiyongxi on 2016/7/28.
 */
public class CacheUtil {
    private static final Logger logger = LoggerFactory.getLogger(CacheUtil.class);
    private static CacheUtil factory;

    public static CacheUtil getSingleton()
    {
        if(factory == null)
        {
            factory = new CacheUtil();
        }
        return factory;
    }

    private Jedis redisClient;

    public int getSize() {
        try {
            redisClient = RedisUtil.getSingleton().getRedisClient();
            return Integer.valueOf(redisClient.dbSize().toString());
        }finally {
            redisClient.close();
        }
    }

    public void add(Object key, Object value){
        update(key, value);
    }

    public void add(Object key, Object value, int expiry){
        update(key, value, expiry);
    }

    public void update(Object key, Object value) {
        try {
            redisClient = RedisUtil.getSingleton().getRedisClient();
            logger.debug("putObject->" + key + "=" + value);
            redisClient.set(SerializeUtil.serialize(key.toString()), SerializeUtil.serialize(value));
        }finally {
            redisClient.close();
        }
    }

    public void update(Object key, Object value, int expiry) {
        try {
            redisClient = RedisUtil.getSingleton().getRedisClient();
            logger.debug("putObject->" + key + "=" + value);
            redisClient.set(SerializeUtil.serialize(key.toString()), SerializeUtil.serialize(value));
            redisClient.expire(SerializeUtil.serialize(key.toString()), expiry);
        }finally {
            redisClient.close();
        }
    }

    public Object get(Object key) {
        try {
            redisClient = RedisUtil.getSingleton().getRedisClient();
            Object value = SerializeUtil.unserialize(redisClient.get(SerializeUtil.serialize(key.toString())));
            logger.debug("getObject->" + key + "=" + value);
            return value;
        }finally {
            redisClient.close();
        }
    }

    public Object delete(Object key) {
        try {
            redisClient = RedisUtil.getSingleton().getRedisClient();
            return redisClient.expire(SerializeUtil.serialize(key.toString()), 0);
        }finally {
            redisClient.close();
        }
    }

    public void clear() {
        try {
            redisClient = RedisUtil.getSingleton().getRedisClient();
            redisClient.flushDB();
        }finally {
            redisClient.close();
        }
    }
}
