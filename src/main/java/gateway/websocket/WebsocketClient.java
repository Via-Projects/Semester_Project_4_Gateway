package gateway.websocket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.model.MeasurementValues;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

public class WebsocketClient implements WebSocket.Listener {
    private WebSocket server = null;

    public void sendDownLink(String jsonTelegram) {
        server.sendText(jsonTelegram,true);
    }

    public WebsocketClient(String url) {
        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
                .buildAsync(URI.create(url), this);

        server = ws.join();
        sendData(null);
    }

    public void onOpen(WebSocket webSocket) {
        // This WebSocket will invoke onText, onBinary, onPing, onPong or onClose methods on the associated listener (i.e. receive methods) up to n more times
        webSocket.request(1);
        System.out.println("WebSocket Listener has been opened for requests.");
    }

    public void onError​(WebSocket webSocket, Throwable error) {
        System.out.println("A " + error.getCause() + " exception was thrown.");
        System.out.println("Message: " + error.getLocalizedMessage());
        webSocket.abort();
    }

    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        System.out.println("WebSocket closed!");
        System.out.println("Status:" + statusCode + " Reason: " + reason);
        return new CompletableFuture().completedFuture("onClose() completed.").thenAccept(System.out::println);
    }

    public CompletionStage<?> onPing​(WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        System.out.println("Ping: Client ---> Server");
        System.out.println(message.asCharBuffer().toString());
        return new CompletableFuture().completedFuture("Ping completed.").thenAccept(System.out::println);
    }

    public CompletionStage<?> onPong​(WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        System.out.println("Pong: Client ---> Server");
        System.out.println(message.asCharBuffer().toString());
        return new CompletableFuture().completedFuture("Pong completed.").thenAccept(System.out::println);
    }

    // Here you receive data.
    public CompletionStage<?> onText​(WebSocket webSocket, CharSequence data, boolean last) {
        System.out.println("onText invoked");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            DataPacket receivedDataPacket = objectMapper.readValue(data.toString(), DataPacket.class);
            System.out.println(receivedDataPacket);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        webSocket.request(1);
        return new CompletableFuture().completedFuture("onText() completed.").thenAccept(System.out::println);
    }

    private void sendData(DataPacket dataPacket) {
//        float co2 = dataPacket.getData().charAt(0) + dataPacket.getData().charAt(1);
//        float temperature = dataPacket.getData().charAt(2);
//        float humidity = dataPacket.getData().charAt(3);

        // Dummy data
        MeasurementValues measurementValues = new MeasurementValues();
        measurementValues.setTemperatureSensorId((long) 5);
        measurementValues.setTemperatureValue(21.15f);
        measurementValues.setHumiditySensorId((long)6);
        measurementValues.setHumidityValue(88.67f);
        measurementValues.setCarbonDioxideSensorId((long) 7);
        measurementValues.setCarbonDioxideValue(600);

        postMeasurementValues(measurementValues);
    }


    public boolean postMeasurementValues(MeasurementValues measurementValues) {
        String API_URL = "https://librarydatabaseapi.azurewebsites.net/api/archive/measurementValues";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(API_URL, measurementValues, Boolean.class);
    }
}
