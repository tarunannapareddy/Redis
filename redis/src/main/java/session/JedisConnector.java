package session;

import redis.clients.jedis.Jedis;

public class JedisConnector {

    private static Jedis jedis = new Jedis("127.0.0.1", 6379);
    private JedisConnector(){
    }

    public static Jedis getJedis(){
        return jedis;
    }
}
