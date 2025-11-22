package edu.columbia.parking.data;

import edu.columbia.parking.common.ParkingViolation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParkingCSVReader {

    public List<ParkingViolation> read(String fileName) throws IOException {
        List<ParkingViolation> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length != 7) {
                    continue;
                }
                String timestamp = parts[0].trim();
                double fine;
                try {
                    fine = Double.parseDouble(parts[1].trim());
                } catch (NumberFormatException e) {
                    continue;
                }
                String description = parts[2].trim();
                String vehicleId = parts[3].trim();
                String licenseState = parts[4].trim();
                String violationId = parts[5].trim();
                String zipRaw = parts[6].trim();
                String zip = normalizeZip(zipRaw);

                result.add(new ParkingViolation(
                        timestamp, fine, description, vehicleId, licenseState, violationId, zip));
            }
        }
        return result;
    }

    private String normalizeZip(String raw) {
        if (raw == null || raw.isEmpty()) {
            return null;
        }
        String digits = raw.replaceAll("[^0-9]", "");
        if (digits.length() < 5) {
            return null;
        }
        return digits.substring(0, 5);
    }
}