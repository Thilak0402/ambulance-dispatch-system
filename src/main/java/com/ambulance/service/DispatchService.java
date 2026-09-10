package com.ambulance.service;

import com.ambulance.exception.InvalidRequestException;
import com.ambulance.exception.ResourceUnavailableException;
import com.ambulance.model.*;
import java.util.*;

public class DispatchService {
    private final List<Ambulance> ambulances = new ArrayList<>();
    private final PriorityQueue<EmergencyRequest> waitingQueue = new PriorityQueue<>();
    private final List<String> emergencyHistory = new ArrayList<>();

    public void registerAmbulance(Ambulance ambulance) {
        ambulances.add(ambulance);
    }

    public synchronized void processEmergency(EmergencyRequest request) {
        if (request == null || request.getPatientId() == null || request.getPatientId().isEmpty()) {
            throw new InvalidRequestException("Emergency request data is invalid.");
        }

        Optional<Ambulance> match = findBestAmbulance(request.getRequiredType());
        if (match.isPresent()) {
            dispatch(match.get(), request);
        } else {
            waitingQueue.add(request);
            emergencyHistory.add("Queued emergency request for Patient ID: " + request.getPatientId());
        }
    }

    private Optional<Ambulance> findBestAmbulance(AmbulanceType type) {
        return ambulances.stream()
                .filter(a -> a.getStatus() == AmbulanceStatus.AVAILABLE && a.getType() == type)
                .min(Comparator.comparingDouble(Ambulance::getCurrentDistanceToPatient));
    }

    private void dispatch(Ambulance ambulance, EmergencyRequest request) {
        ambulance.setStatus(AmbulanceStatus.DISPATCHED);
        double etaMinutes = (request.getEstimatedDistance() / 50.0) * 60.0; // assuming avg speed 50 km/h
        emergencyHistory.add(String.format("Dispatched Ambulance %s to Patient %s. ETA: %.1f mins", 
                ambulance.getAmbulanceId(), request.getPatientId(), etaMinutes));
    }

    public synchronized void updateAmbulanceStatus(String ambulanceId, AmbulanceStatus newStatus) {
        Ambulance ambulance = ambulances.stream()
                .filter(a -> a.getAmbulanceId().equals(ambulanceId))
                .findFirst()
                .orElseThrow(() -> new ResourceUnavailableException("Ambulance ID not found: " + ambulanceId));

        ambulance.setStatus(newStatus);
        emergencyHistory.add("Ambulance " + ambulanceId + " transitioned to state: " + newStatus);

        if (newStatus == AmbulanceStatus.AVAILABLE && !waitingQueue.isEmpty()) {
            EmergencyRequest next = waitingQueue.poll();
            findBestAmbulance(next.getRequiredType()).ifPresentOrElse(
                amb -> dispatch(amb, next),
                () -> waitingQueue.add(next) // Re-queue if no matching type found
            );
        }
    }

    public List<String> getEmergencyHistory() { return emergencyHistory; }
    public int getWaitingQueueSize() { return waitingQueue.size(); }
}