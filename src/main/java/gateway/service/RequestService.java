package gateway.service;

import gateway.model.MeasurementValues;

public interface RequestService {
    boolean postMeasurementValues(MeasurementValues measurementValues);
}
