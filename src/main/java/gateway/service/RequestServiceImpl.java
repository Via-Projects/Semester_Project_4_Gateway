package gateway.service;

import gateway.model.MeasurementValues;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RequestServiceImpl implements RequestService {
    private static final String API_URL = "http://localhost:8080/api/archive/measurementValues";

    @Override
    public boolean postMeasurementValues(MeasurementValues measurementValues) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(API_URL, measurementValues, Boolean.class);
    }
}
