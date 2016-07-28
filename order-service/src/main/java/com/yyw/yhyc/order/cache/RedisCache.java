package com.yyw.yhyc.order.cache;

import com.yyw.yhyc.order.helper.SpringBeanHelper;
import com.yyw.yhyc.order.utils.PropertiesUtil;
import com.yyw.yhyc.order.utils.SerializeUtil;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by shiyongxi on 2016/7/27.
 */
public class RedisCache implements Cache {
    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private JedisPool pool;
    private Jedis redisClient;
    private String id;
    public RedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        logger.debug("MybatisRedisCache:id=" + id);
        this.id = id;
        this.redisClient = createReids();
    }

    public String getId() {
        return this.id;
    }

    public int getSize() {
        return Integer.valueOf(redisClient.dbSize().toString());
    }

    public void putObject(Object key, Object value) {
        logger.debug("putObject:" + key + "=" + value);
        redisClient.set(SerializeUtil.serialize(key.toString()), SerializeUtil.serialize(value));
    }

    public Object getObject(Object key) {
        Object value = SerializeUtil.unserialize(redisClient.get(SerializeUtil.serialize(key.toString())));
        logger.debug("getObject:" + key + "=" + value);
        return value;
    }

    public Object removeObject(Object key) {
        return redisClient.expire(SerializeUtil.serialize(key.toString()), 0);
    }

    public void clear() {
        redisClient.flushDB();
    }

    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    private Jedis createReids(){
        return getJedisPool().getResource();
    }

    private JedisPool getJedisPool(){
        if(null == pool){
            Properties pro = PropertiesUtil.getConfig("redis.properties");
            logger.debug("pro:" + pro);

            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxIdle(Integer.parseInt((String) pro.get("redis.maxIdle")));
            poolConfig.setMaxWaitMillis(Integer.parseInt((String) pro.get("redis.maxWait")));
            poolConfig.setTestOnBorrow(Boolean.parseBoolean((String) pro.get("redis.testOnBorrow")));
            this.pool = new JedisPool(poolConfig, (String) pro.get("redis.host"), Integer.parseInt((String) pro.get("redis.port")), Integer.parseInt((String) pro.get("redis.timeout")));
        }

        return this.pool;
    }
}
