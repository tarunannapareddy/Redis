package session;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

public class SessionManager {

    private static final int SESSION_TIMEOUT_SECONDS = 60;

    private Jedis jedis;

    private Gson gson;

    public SessionManager() {
        jedis = JedisConnector.getJedis();
        gson = new Gson();
    }

    public void loginUser(Session session) {
        String key = "user:"+session.getUserId();
        jedis.setex(key,SESSION_TIMEOUT_SECONDS,gson.toJson(session));
    }

    public Session getUserSession(String userId) {
        // Retrieve user data from Redis using userId
        String key = "user:" + userId;
        String data = jedis.get(key);
        return gson.fromJson(data, Session.class);
    }
}