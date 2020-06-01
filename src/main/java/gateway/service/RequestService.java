package gateway.service;

import gateway.socket.DataPacket;

public interface RequestService {
    boolean postMeasurementValues(DataPacket dataPacket);
}
