package edu.columbia.parking.data;

import edu.columbia.parking.common.ParkingViolation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParkingJSONReader {

    public List<ParkingViolation> read(String fileName) throws IOException {
        List<ParkingViolation> result = new ArrayList<>();

        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(fileName)) {
            Object obj = parser.parse(reader);
            JSONArray array = (JSONArray) obj;

            for (Object element : array) {
                JSONObject json = (JSONObject) element;

                String timestamp = (String) json.get("timestamp");
                String fineStr = String.valueOf(json.get("fine"));
                String description = (String) json.get("violation");
                String vehicleId = (String) json.get("vehicle_id");
                String licenseState = (String) json.get("state");
                String violationId = String.valueOf(json.get("violation_id"));
                String zipRaw = (String) json.get("zip_code");

                double fine;
                try {
                    fine = Double.parseDouble(fineStr);
                } catch (NumberFormatException e) {
                    continue;
                }

                String zip = normalizeZip(zipRaw);

                result.add(new ParkingViolation(
                        timestamp, fine, description, vehicleId, licenseState, violationId, zip));
            }
        } catch (ParseException e) {
            throw new IOException("Failed to parse JSON file: " + fileName, e);
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