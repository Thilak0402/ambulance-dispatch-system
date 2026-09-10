package com.ambulance.model;

public class Ambulance {
    private String ambulanceId;
    private AmbulanceType type;
    private AmbulanceStatus status;
    private Driver driver;
    private double currentDistanceToPatient;

    public Ambulance(String ambulanceId, AmbulanceType type, Driver driver) {
        this.ambulanceId = ambulanceId;
        this.type = type;
        this.status = AmbulanceStatus.AVAILABLE;
        this.driver = driver;
    }

    public String getAmbulanceId() { return ambulanceId; }
    public AmbulanceType getType() { return type; }
    public AmbulanceStatus getStatus() { return status; }
    public void setStatus(AmbulanceStatus status) { this.status = status; }
    public double getCurrentDistanceToPatient() { return currentDistanceToPatient; }
    public void setCurrentDistanceToPatient(double distance) { this.currentDistanceToPatient = distance; }
}