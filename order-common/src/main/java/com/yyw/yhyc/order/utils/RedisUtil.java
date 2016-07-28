package com.yyw.yhyc.order.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Properties;

/**
 * Created by shiyongxi on 2016/7/28.
 */
public class RedisUtil {
    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    private int maxIdle = 8;
    private int maxWait = 2000;
    private boolean testOnBorrow = true;
    private String host;
    private int port;
    private int timeout;
    private String password;
    private int database = 0;
    private String clientName;

    private static JedisPool pool;
    private static RedisUtil factory;

    public static RedisUtil getSingleton(){
        if(null == factory){
            factory = new RedisUtil();
        }

        return factory;
    }

    public RedisUtil() {
        Properties pro = PropertiesUtil.getConfig("redis.properties");
        logger.debug("pro:" + pro);

        String value = (String) pro.get("redis.maxIdle");
        if(null != value && !"".equals(value)){
            maxIdle = Integer.parseInt(value);
        }

        value = (String) pro.get("redis.maxWait");
        if(null != value && !"".equals(value)){
            maxWait = Integer.parseInt(value);
        }

        value = (String) pro.get("redis.testOnBorrow");
        if(null != value && !"".equals(value)){
            testOnBorrow = Boolean.parseBoolean(value);
        }

        value = (String) pro.get("redis.host");
        if(null != value && !"".equals(value)){
            host = value;
        }

        value = (String) pro.get("redis.port");
        if(null != value && !"".equals(value)){
            port = Integer.parseInt(value);
        }

        value = (String) pro.get("redis.timeout");
        if(null != value && !"".equals(value)){
            timeout = Integer.parseInt(value);
        }

        value = (String) pro.get("redis.password");
        if(null != value && !"".equals(value)){
            password = value;
        }

        value = (String) pro.get("redis.database");
        if(null != value && !"".equals(value)){
            database = Integer.parseInt(value);
        }

        value = (String) pro.get("redis.password");
        if(null != value && !"".equals(value)){
            clientName = value;
        }
    }

    public JedisPool getJedisPool(){
        if(null == pool){
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxIdle(maxIdle);
            poolConfig.setMaxWaitMillis(maxWait);
            poolConfig.setTestOnBorrow(testOnBorrow);

            pool = new JedisPool(poolConfig, host, port, timeout);
        }

        return pool;
    }

    public Jedis getRedisClient(){
        return getJedisPool().getResource();
    }

    public static void main(String[] args) {
        RedisUtil.getSingleton().getRedisClient();
    }
}
