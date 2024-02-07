package session;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.params.XAddParams;

import java.util.HashMap;
import java.util.Map;

public class PlaceOrder {
    private Jedis jedis;
    private static final String CHANNEL_NAME = "order_placed";
    public static final String STREAM_NAME = "order_placed_stream";

    private Gson gson;

    public PlaceOrder() {
        this.jedis = JedisConnector.getJedis();
        this.gson = new Gson();
    }

    public long notifyOrderPubSub(Session session, String order){
        Order orderPojo = Order.builder()
                            .order(order).userId(session.getUserId()).emailId(session.getEmail())
                                        .build();
        return jedis.publish(CHANNEL_NAME, gson.toJson(orderPojo));
    }

    public void notifyOrderStream(Session session, String order){
        Map<String, String> map = new HashMap<>();
        map.put("userId",session.getUserId());
        map.put("order", order);
        map.put("e-mail", session.getEmail());
        StreamEntryID id = jedis.xadd(STREAM_NAME, XAddParams.xAddParams(), map);
        System.out.println("Order added to Stream "+id);
    }
}
