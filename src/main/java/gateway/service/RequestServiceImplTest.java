package gateway.service;

import gateway.model.MeasurementValues;
import gateway.socket.DataPacket;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RequestServiceImplTest {
    static RequestServiceImpl requestService;

    @BeforeAll
    public static void setUp() {
        requestService = new RequestServiceImpl();
    }

    @AfterAll
    public static void tearDown() {
        requestService = null;
    }

    @Test
    public void testConverting() {
        DataPacket packet = new DataPacket();
        MeasurementValues values;

        packet.setData("02632217");
        values = requestService.getValuesFromDataPacket(packet);

        packet.setData("052d2219");
        values = requestService.getValuesFromDataPacket(packet);

        packet.setData("02fd1b1d");
        values = requestService.getValuesFromDataPacket(packet);

        packet.setData("02fc1328");
        values = requestService.getValuesFromDataPacket(packet);

//        assertEquals(611, values.getCarbonDioxideValue());
    }
}