package session;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class PubSubConsumer {
    private static final String CHANNEL_NAME = "order_placed";
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        Thread consumerThread = new Thread(new MyJedisPubSub(), "Email notification Thread");
        consumerThread.start();
    }

    static class MyJedisPubSub extends JedisPubSub implements Runnable {
        @Override
        public void run() {
            try (Jedis jedis = new Jedis("localhost", 6379)) {
                System.out.println("*** started consumer "+Thread.currentThread().getName());
                jedis.subscribe(this, CHANNEL_NAME);
            }
        }

        @Override
        public void onMessage(String channel, String message) {
            Order order = gson.fromJson(message, Order.class);
            System.out.println(Thread.currentThread().getName() + " received message from channel '" + channel + "': " + order.getOrder());
        }
    }
}
