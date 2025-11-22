package edu.columbia.parking.data;

import edu.columbia.parking.common.PropertyRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PropertyReader {

    public List<PropertyRecord> read(String fileName) throws IOException {
        List<PropertyRecord> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                return result;
            }

            String[] headers = headerLine.split(",", -1);
            int idxMarket = indexOf(headers, "market_value");
            int idxArea = indexOf(headers, "total_livable_area");
            int idxZip = indexOf(headers, "zip_code");

            if (idxMarket == -1 || idxArea == -1 || idxZip == -1) {
                throw new IOException("Required property fields missing in header");
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < headers.length) {
                    continue;
                }

                String zipRaw = parts[idxZip].trim();
                String zip = normalizeZip(zipRaw);
                if (zip == null) {
                    continue;
                }

                Double marketValue = parsePositiveDoubleOrNull(parts[idxMarket].trim());
                Double area = parsePositiveDoubleOrNull(parts[idxArea].trim());

                result.add(new PropertyRecord(zip, marketValue, area));
            }
        }
        return result;
    }

    private int indexOf(String[] headers, String name) {
        for (int i = 0; i < headers.length; i++) {
            if (name.equals(headers[i].trim())) {
                return i;
            }
        }
        return -1;
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

    private Double parsePositiveDoubleOrNull(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        try {
            double value = Double.parseDouble(s);
            if (value <= 0) {
                return null;
            }
            return value;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}