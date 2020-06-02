package gateway.service;

import gateway.model.MeasurementValues;
import gateway.socket.DataPacket;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Service
@Slf4j
public class RequestServiceImpl implements RequestService {
    private static final String API_URL = "https://librarydatabaseapi.azurewebsites.net/api/archive/measurementValues";

    @Override
    public boolean postMeasurementValues(DataPacket dataPacket) {
        log.info("Extracting MeasurementValues from dataPacket ...");
        MeasurementValues valuesToPost = getValuesFromDataPacket(dataPacket);
        RestTemplate restTemplate = new RestTemplate();
        log.info("Making a POST request to Database with values: " + valuesToPost);
        return restTemplate.postForObject(API_URL, valuesToPost, Boolean.class);
    }

    public MeasurementValues getValuesFromDataPacket(DataPacket dataPacket)
    {
        String CO2HexLow = dataPacket.getData().substring(0,2);
        String CO2HexHigh = dataPacket.getData().substring(2,4);
        String tempHex = dataPacket.getData().substring(4,6);
        String humHex = dataPacket.getData().substring(6,8);

        long CO2 = Long.parseLong(CO2HexLow + CO2HexHigh,16);
        log.info("[Extractor] Extracted CO2 level: " + CO2);

        long temp = Long.parseLong(tempHex,16);
        log.info("[Extractor] Extracted temperature value: " + temp);

        long hum = Long.parseLong(humHex,16);
        log.info("[Extractor] Extracted humidity value: " + hum);


        MeasurementValues measurementValues = new MeasurementValues();
        measurementValues.setTemperatureSensorId((long) 5);
        measurementValues.setTemperatureValue(temp);
        measurementValues.setHumiditySensorId((long)6);
        measurementValues.setHumidityValue(hum);
        measurementValues.setCarbonDioxideSensorId((long) 7);
        measurementValues.setCarbonDioxideValue(CO2);
        log.info("[Extractor] MeasurementValues object created with data: " + measurementValues);

        return measurementValues;

    }
}
