package edu.columbia.parking.data;

import edu.columbia.parking.common.ZipCodePopulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PopulationReader {

    public List<ZipCodePopulation> read(String fileName) throws IOException {
        List<ZipCodePopulation> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\s+");
                if (parts.length != 2) continue;

                String zip = normalizeZip(parts[0]);
                if (zip == null) continue;

                try {
                    int population = Integer.parseInt(parts[1]);
                    if (population < 0) continue;
                    result.add(new ZipCodePopulation(zip, population));
                } catch (NumberFormatException e) {
                }
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