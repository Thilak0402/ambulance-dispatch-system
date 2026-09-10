package com.ambulance.model;

public class EmergencyRequest implements Comparable<EmergencyRequest> {
    private String patientId;
    private EmergencyPriority priority;
    private AmbulanceType requiredType;
    private String pickupLocation;
    private String destinationHospital;
    private double estimatedDistance;

    public EmergencyRequest(String patientId, EmergencyPriority priority, AmbulanceType requiredType, String pickupLocation, String destinationHospital, double estimatedDistance) {
        this.patientId = patientId;
        this.priority = priority;
        this.requiredType = requiredType;
        this.pickupLocation = pickupLocation;
        this.destinationHospital = destinationHospital;
        this.estimatedDistance = estimatedDistance;
    }

    public String getPatientId() { return patientId; }
    public EmergencyPriority getPriority() { return priority; }
    public AmbulanceType getRequiredType() { return requiredType; }
    public double getEstimatedDistance() { return estimatedDistance; }

    @Override
    public int compareTo(EmergencyRequest other) {
        return Integer.compare(this.priority.getLevel(), other.priority.getLevel());
    }
}