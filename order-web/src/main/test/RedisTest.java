import com.yyw.yhyc.utils.CacheUtil;
import com.yyw.yhyc.utils.PropertiesUtil;
import com.yyw.yhyc.utils.RedisUtil;
import com.yyw.yhyc.utils.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by shiyongxi on 2016/8/15.
 */
public class RedisTest {
    private int maxIdle = 8;
    private int maxWait = 2000;
    private boolean testOnBorrow = true;


    private static JedisPool pool;
    private Jedis redis;


    public JedisPool getJedisPool(){
        if(null == pool){
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxIdle(maxIdle);
            poolConfig.setMaxWaitMillis(maxWait);
            poolConfig.setTestOnBorrow(testOnBorrow);

            pool = new JedisPool(poolConfig, "redis.test.yiyaowang.com", 6379, 3000, "foobared");
        }

        return pool;
    }

    public Jedis getRedisClient(){
        return getJedisPool().getResource();
    }

    public static void main(String[] args) {
        Jedis redis = new RedisTest().getRedisClient();
        System.out.println(CacheUtil.getSingleton().get("SESSION:JSID_28254644E4928B8789FCB42BB245A".getBytes()));
    }
}
