package session;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Order {
    private String userId;
    private String order;
    private String emailId;
}
