package edu.columbia.parking.processor;

import edu.columbia.parking.common.ParkingViolation;
import edu.columbia.parking.data.DataRepository;

import java.text.DecimalFormat;
import java.util.*;

public class FinesProcessor {

    private final DataRepository repository;
    private Map<String, Double> finesPerCapitaCache = null;

    public FinesProcessor(DataRepository repository) {
        this.repository = repository;
    }

    public SortedMap<String, Double> getFinesPerCapita() {
        if (finesPerCapitaCache == null) {
            finesPerCapitaCache = new HashMap<>();

            Map<String, List<ParkingViolation>> map = repository.getViolationsByZip();
            Map<String, Integer> popMap = repository.getPopulationByZip();

            for (Map.Entry<String, List<ParkingViolation>> entry : map.entrySet()) {
                String zip = entry.getKey();
                int population = popMap.getOrDefault(zip, 0);
                if (population <= 0) continue;

                double totalFines = entry.getValue().stream()
                        .filter(v -> "PA".equalsIgnoreCase(v.getLicenseState()))
                        .mapToDouble(ParkingViolation::getFine)
                        .sum();

                if (totalFines > 0) {
                    double perCapita = totalFines / population;
                    finesPerCapitaCache.put(zip, perCapita);
                }
            }
        }

        SortedMap<String, Double> sorted = new TreeMap<>();
        sorted.putAll(finesPerCapitaCache);
        return sorted;
    }

    public static String formatFourDecimals(double value) {
        DecimalFormat df = new DecimalFormat("0.0000");
        return df.format(value);
    }
}