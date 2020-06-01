package gateway.service;

import gateway.model.MeasurementValues;
import gateway.socket.DataPacket;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RequestServiceImpl implements RequestService {
    private static final String API_URL = "http://localhost:8080/api/archive/measurementValues";

    @Override
    public boolean postMeasurementValues(DataPacket dataPacket) {
        MeasurementValues valuesToPost = getValuesFromDataPacket(dataPacket);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(API_URL, valuesToPost, Boolean.class);
    }

    private MeasurementValues getValuesFromDataPacket(DataPacket dataPacket)
    {
        String CO2HexLow = dataPacket.getData().substring(0,2);
        String CO2HexHigh = dataPacket.getData().substring(2,4);
        String tempHex = dataPacket.getData().substring(4,6);
        String humHex = dataPacket.getData().substring(6,8);

        long CO2 = Long.parseLong(CO2HexLow + CO2HexHigh,16);
        long temp = Long.parseLong(tempHex,16);
        long hum = Long.parseLong(humHex,16);

        MeasurementValues measurementValues = new MeasurementValues();
        measurementValues.setTemperatureSensorId((long) 5);
        measurementValues.setTemperatureValue(temp);
        measurementValues.setHumiditySensorId((long)6);
        measurementValues.setHumidityValue(hum);
        measurementValues.setCarbonDioxideSensorId((long) 7);
        measurementValues.setCarbonDioxideValue(CO2);

        return measurementValues;

    }
}
