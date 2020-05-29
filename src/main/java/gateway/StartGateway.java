package gateway;

import gateway.websocket.WebsocketClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StartGateway {
    public static void main(String[] args) {
        SpringApplication.run(StartGateway.class,args);
        WebsocketClient websocketClient = new WebsocketClient("wss://iotnet.teracom.dk/app?token=vnoSxwAAABFpb3RuZXQudGVyYWNvbS5ka1XN9jtj-hrHpSJ1cKRNYRg=");
    }
}
