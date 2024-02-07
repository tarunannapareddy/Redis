package session;

import io.lettuce.core.RedisClient;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.api.sync.RedisStreamCommands;
import io.lettuce.core.protocol.CommandArgs;

import java.util.List;

public class StreamConsumerGroup {
    public static final String STREAM_KEY = "order_placed_stream";
    public static final String CONSUMER_GROUP = "application_group";
    public static final String CONSUMER = "consumer1";

    public static void main(String[] args) {
        RedisClient redisClient = RedisClient.create("redis://127.0.0.1:6379");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisStreamCommands<String, String> streamCommands = connection.sync();
        String lastSeenOffset = "0-0";

        try {
            streamCommands.xgroupCreate( XReadArgs.StreamOffset.from(STREAM_KEY, "0-0"), CONSUMER_GROUP);
        }
        catch (RedisBusyException redisBusyException) {
            System.out.println( String.format("\t Group '%s' already exists",CONSUMER_GROUP));
        }

        while(true){

            List<StreamMessage<String, String>> messages = streamCommands.xreadgroup(Consumer.from(CONSUMER_GROUP, CONSUMER),
                    XReadArgs.Builder.count(3), XReadArgs.StreamOffset.lastConsumed(STREAM_KEY));
            if(!messages.isEmpty()){
                for(StreamMessage<String, String> message : messages){
                    System.out.print(lastSeenOffset+" received message");
                    System.out.print(" userId: " +message.getBody().get("userId"));
                    System.out.print(" order: "+message.getBody().get("order"));
                    System.out.println(" email: "+message.getBody().get("e-mail"));
                   streamCommands.xack(STREAM_KEY, CONSUMER_GROUP,  message.getId());
                }
            }
        }
    }
}
