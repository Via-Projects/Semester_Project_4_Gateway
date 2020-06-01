package gateway;

import gateway.socket.WebsocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {
    private final WebsocketClient websocketClient;

    @Autowired
    public Runner(WebsocketClient websocketClient) {
        this.websocketClient = websocketClient;
    }

    @Override
    public void run(String... args) throws Exception {
        websocketClient.start("wss://iotnet.teracom.dk/app?token=vnoSxwAAABFpb3RuZXQudGVyYWNvbS5ka1XN9jtj-hrHpSJ1cKRNYRg=");
    }
}
