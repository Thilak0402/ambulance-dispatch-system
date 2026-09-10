package com.ambulance.service;

import com.ambulance.exception.InvalidRequestException;
import com.ambulance.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DispatchServiceTest {
    private DispatchService service;

    @BeforeEach
    void setUp() {
        service = new DispatchService();
        Driver driver = new Driver("D1", "John Doe", "1234567890");
        Ambulance amb = new Ambulance("AMB-01", AmbulanceType.ICU, driver);
        amb.setCurrentDistanceToPatient(4.0);
        service.registerAmbulance(amb);
    }

    @Test
    void testSuccessfulDispatch() {
        EmergencyRequest req = new EmergencyRequest("P1", EmergencyPriority.CRITICAL, AmbulanceType.ICU, "Street A", "Hospital B", 5.0);
        service.processEmergency(req);
        assertEquals(1, service.getEmergencyHistory().size());
    }

    @Test
    void testInvalidRequestThrowsException() {
        EmergencyRequest invalidReq = new EmergencyRequest("", EmergencyPriority.NORMAL, AmbulanceType.BASIC, "", "", 0);
        assertThrows(InvalidRequestException.class, () -> service.processEmergency(invalidReq));
    }

    @Test
    void testQueueManagementAndStateTransition() {
        EmergencyRequest req1 = new EmergencyRequest("P1", EmergencyPriority.HIGH, AmbulanceType.ICU, "A", "B", 2.0);
        EmergencyRequest req2 = new EmergencyRequest("P2", EmergencyPriority.CRITICAL, AmbulanceType.ICU, "C", "D", 3.0);
        
        service.processEmergency(req1); // AMB-01 is dispatched
        service.processEmergency(req2); // Goes to queue
        assertEquals(1, service.getWaitingQueueSize());

        service.updateAmbulanceStatus("AMB-01", AmbulanceStatus.AVAILABLE); // Should auto-allocate queued item
        assertEquals(0, service.getWaitingQueueSize());
    }
}