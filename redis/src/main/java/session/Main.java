package session;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class Main {
    public static SessionManager sessionManager = new SessionManager();
    public static PlaceOrder placeOrder = new PlaceOrder();

    public static void main(String[] args) throws IOException {
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.println("*** provide operation ***");
            String operation = userInput.readLine();

            if(operation.equals("LOGIN")) {
                System.out.println("*** provide userId to LOGIN ***");
                String userId = userInput.readLine();
                String userType = "Buyer";
                String location = "CityA";
                String email = "buyer@example.com";
                Session session = Session.builder()
                        .userId(userId).userType(userType).location(location).email(email)
                        .build();
                sessionManager.loginUser(session);
            } else if(operation.equals("PLACE_ORDER")){
                System.out.println("*** provide userId ***");
                String userId = userInput.readLine();
                Session sessionData = sessionManager.getUserSession(userId);
                if(sessionData !=null) {
                    String order =  UUID.randomUUID().toString();
                    System.out.println("order placed and notified to #"+placeOrder.notifyOrderPubSub(sessionData,order));
                    placeOrder.notifyOrderStream(sessionData, order);

                } else{
                    System.out.println("user session expired please login Again");
                }
            } else{
                System.out.println("invalid operation");
            }
        }
    }
}