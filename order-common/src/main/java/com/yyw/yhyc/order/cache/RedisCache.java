package com.yyw.yhyc.order.cache;

import com.yyw.yhyc.order.utils.RedisUtil;
import com.yyw.yhyc.order.utils.SerializeUtil;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by shiyongxi on 2016/7/27.
 */
public class RedisCache implements Cache {
    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Jedis redisClient;

    private String id;
    public RedisCache(final String id) {
        logger.debug("id->" + id);
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public int getSize() {
        try {
            redisClient = RedisUtil.getSingleton().getRedisClient();
            return Integer.valueOf(redisClient.dbSize().toString());
        }finally {
            redisClient.close();
        }
    }

    public void putObject(Object key, Object value) {
        try {
            redisClient = RedisUtil.getSingleton().getRedisClient();
            logger.debug("putObject->" + key + "=" + value);
            redisClient.set(SerializeUtil.serialize(key.toString()), SerializeUtil.serialize(value));
        }finally {
            redisClient.close();
        }
    }

    public Object getObject(Object key) {
        try {
            redisClient = RedisUtil.getSingleton().getRedisClient();
            Object value = SerializeUtil.unserialize(redisClient.get(SerializeUtil.serialize(key.toString())));
            logger.debug("getObject->" + key + "=" + value);
            return value;
        }finally {
            redisClient.close();
        }
    }

    public Object removeObject(Object key) {
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

    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
}
