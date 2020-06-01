package gateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementValues {
    private Long temperatureSensorId;
    private float temperatureValue;

    private Long humiditySensorId;
    private float humidityValue;

    private Long carbonDioxideSensorId;
    private float carbonDioxideValue;
}