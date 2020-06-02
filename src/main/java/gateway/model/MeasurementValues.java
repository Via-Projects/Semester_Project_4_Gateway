package gateway.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MeasurementValues {
    private Long temperatureSensorId;
    private float temperatureValue;

    private Long humiditySensorId;
    private float humidityValue;

    private Long carbonDioxideSensorId;
    private float carbonDioxideValue;
}