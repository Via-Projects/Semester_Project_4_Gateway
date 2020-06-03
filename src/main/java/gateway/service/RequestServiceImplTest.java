package gateway.service;

import gateway.model.DataPacketException;
import gateway.model.MeasurementValues;
import gateway.socket.DataPacket;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestServiceImplTest {
    private static RequestServiceImpl requestService;
    private static DataPacket dataPacket;

    @BeforeAll
    public static void setUp() {
        requestService = new RequestServiceImpl();
    }

    @AfterAll
    public static void tearDown() {
        requestService = null;
    }

    @BeforeEach
    public void initDataPacket(){
        dataPacket = new DataPacket();
    }

    @Test
    public void testCorrectDataConversion() {
        dataPacket.setData("02632217");
        final MeasurementValues values = requestService.getValuesFromDataPacket(dataPacket);

        assertAll("Extracting values",
                () -> assertEquals(611, values.getCarbonDioxideValue()),
                () -> assertEquals(34, values.getTemperatureValue()),
                () -> assertEquals(23, values.getHumidityValue()));
    }

    @Test
    public void testIncorrectDataConversion() {
        dataPacket.setData("DDRXQMNI");

        assertThrows(NumberFormatException.class,
                () -> requestService.getValuesFromDataPacket(dataPacket));
    }

    @Test
    public void testZeroDataConversion() {
        dataPacket.setData("00000000");
        final MeasurementValues values = requestService.getValuesFromDataPacket(dataPacket);

        assertAll("Extracting values",
                () -> assertEquals(0, values.getCarbonDioxideValue()),
                () -> assertEquals(0, values.getTemperatureValue()),
                () -> assertEquals(0, values.getHumidityValue()));
    }

    @Test
    public void testMaxValueDataConversion() {
        dataPacket.setData("FFFFFFFF");
        final MeasurementValues values = requestService.getValuesFromDataPacket(dataPacket);

        assertAll("Extracting values",
                () -> assertEquals(65535, values.getCarbonDioxideValue()),
                () -> assertEquals(255, values.getTemperatureValue()),
                () -> assertEquals(255, values.getHumidityValue()));
    }

    @Test
    public void testTooLongInputDataConversion() {
        dataPacket.setData("026322173563");

        assertThrows(DataPacketException.class,
                () -> requestService.getValuesFromDataPacket(dataPacket));
    }

    @Test
    public void testEmptyInputDataConversion() {
        dataPacket.setData("");

        assertThrows(DataPacketException.class,
                () -> requestService.getValuesFromDataPacket(dataPacket));
    }

    @Test
    public void testNullInputDataConversion() {
        dataPacket.setData(null);

        assertThrows(NullPointerException.class,
                () -> requestService.getValuesFromDataPacket(dataPacket));
    }
}