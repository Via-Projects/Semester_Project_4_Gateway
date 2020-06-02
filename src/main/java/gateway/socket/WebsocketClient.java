package gateway.socket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class WebsocketClient implements WebSocket.Listener {
    private WebSocket server = null;
    private final RequestService requestService;

    @Autowired
    public WebsocketClient(RequestService requestService) {
        this.requestService = requestService;
    }

    public void sendDownLink(String jsonTelegram) {
        server.sendText(jsonTelegram,true);
    }

    public void start(String url) {
        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
                .buildAsync(URI.create(url), this);

        server = ws.join();
    }

    public void onOpen(WebSocket webSocket) {
        // This WebSocket will invoke onText, onBinary, onPing, onPong or onClose methods on the associated listener (i.e. receive methods) up to n more times
        webSocket.request(1);
        log.info("WebSocket Listener has been opened for requests.");
    }

    public void onError​(WebSocket webSocket, Throwable error) {
        log.info("A " + error.getCause() + " exception was thrown.");
        log.info("Message: " + error.getLocalizedMessage());
        webSocket.abort();
    }

    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        log.info("WebSocket closed!");
        log.info("Status:" + statusCode + " Reason: " + reason);
        return new CompletableFuture().completedFuture("onClose() completed.").thenAccept(System.out::println);
    }

    public CompletionStage<?> onPing​(WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        log.info("Ping: Client ---> Server");
        log.info(message.asCharBuffer().toString());
        return new CompletableFuture().completedFuture("Ping completed.").thenAccept(System.out::println);
    }

    public CompletionStage<?> onPong​(WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        log.info("Pong: Client ---> Server");
        log.info(message.asCharBuffer().toString());
        return new CompletableFuture().completedFuture("Pong completed.").thenAccept(System.out::println);
    }

    public CompletionStage<?> onText​(WebSocket webSocket, CharSequence data, boolean last) {
        log.info("onText invoked");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info("JSON data: " + data.toString());
            DataPacket receivedDataPacket = objectMapper.readValue(data.toString(), DataPacket.class);
            log.info("Received data packet: " + receivedDataPacket);
            requestService.postMeasurementValues(receivedDataPacket);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        webSocket.request(1);
        return new CompletableFuture().completedFuture("onText() completed.").thenAccept(System.out::println);
    }
}
