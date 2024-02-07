package session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
public class Session {
    private String userId;
    private String  userType;
    private String location;
    private String email;

    public String toString() {
        return "Session{" +
                "userId='" + userId + '\'' +
                ", userType='" + userType + '\'' +
                ", location='" + location + '\'' +
                ", email='" + email + '\'' +
                "}";

    }
}
