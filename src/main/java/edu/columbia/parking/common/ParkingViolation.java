package edu.columbia.parking.common;

public class ParkingViolation {
    private final String timestamp;
    private final double fine;
    private final String description;
    private final String vehicleId;
    private final String licenseState;
    private final String violationId;
    private final String zipCode;

    public ParkingViolation(String timestamp, double fine, String description,
                            String vehicleId, String licenseState, String violationId,
                            String zipCode) {
        this.timestamp = timestamp;
        this.fine = fine;
        this.description = description;
        this.vehicleId = vehicleId;
        this.licenseState = licenseState;
        this.violationId = violationId;
        this.zipCode = zipCode;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getFine() {
        return fine;
    }

    public String getDescription() {
        return description;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getLicenseState() {
        return licenseState;
    }

    public String getViolationId() {
        return violationId;
    }

    public String getZipCode() {
        return zipCode;
    }
}