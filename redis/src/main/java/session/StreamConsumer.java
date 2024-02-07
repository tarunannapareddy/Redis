package session;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStreamCommands;
import io.lettuce.core.*;
import java.util.List;

public class StreamConsumer {
    public static final String STREAM_KEY = "order_placed_stream";

    public static void main(String[] args) {
        RedisClient redisClient = RedisClient.create("redis://127.0.0.1:6379");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisStreamCommands<String, String> streamCommands = connection.sync();
        String lastSeenOffset = "0-0";

        while(true){
            List<StreamMessage<String, String>> streamMessages = streamCommands.xread(XReadArgs.Builder.block(500).count(1), XReadArgs.StreamOffset.from(STREAM_KEY, lastSeenOffset));
            for(StreamMessage<String, String> message : streamMessages){
                lastSeenOffset = message.getId();
                System.out.print(lastSeenOffset+" received message");
                System.out.print(" userId: " +message.getBody().get("userId"));
                System.out.print(" order: "+message.getBody().get("order"));
                System.out.println(" email: "+message.getBody().get("e-mail"));
            }
        }
    }
}
