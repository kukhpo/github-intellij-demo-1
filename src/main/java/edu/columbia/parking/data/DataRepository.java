package edu.columbia.parking.data;

import edu.columbia.parking.common.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DataRepository {

    private static final DataRepository INSTANCE = new DataRepository();

    private final Map<String, List<ParkingViolation>> violationsByZip = new HashMap<>();
    private final Map<String, List<PropertyRecord>> propertiesByZip = new HashMap<>();
    private final Map<String, Integer> populationByZip = new HashMap<>();

    private DataRepository() { }

    public static DataRepository getInstance() {
        return INSTANCE;
    }

    public void loadAll(InputFormat format,
                        String parkingFile,
                        String propertyFile,
                        String populationFile) throws IOException {
        loadParking(format, parkingFile);
        loadProperties(propertyFile);
        loadPopulation(populationFile);
    }

    private void loadParking(InputFormat format, String fileName) throws IOException {
        List<ParkingViolation> violations;
        if (format == InputFormat.CSV) {
            violations = new ParkingCSVReader().read(fileName);
        } else {
            violations = new ParkingJSONReader().read(fileName);
        }
        violationsByZip.clear();
        violationsByZip.putAll(
                violations.stream()
                        .filter(v -> v.getZipCode() != null)
                        .collect(Collectors.groupingBy(ParkingViolation::getZipCode))
        );
    }

    private void loadProperties(String fileName) throws IOException {
        List<PropertyRecord> props = new PropertyReader().read(fileName);
        propertiesByZip.clear();
        propertiesByZip.putAll(
                props.stream()
                        .collect(Collectors.groupingBy(PropertyRecord::getZipCode))
        );
    }

    private void loadPopulation(String fileName) throws IOException {
        List<ZipCodePopulation> pops = new PopulationReader().read(fileName);
        populationByZip.clear();
        for (ZipCodePopulation p : pops) {
            populationByZip.put(p.getZipCode(), p.getPopulation());
        }
    }

    public Map<String, List<ParkingViolation>> getViolationsByZip() {
        return Collections.unmodifiableMap(violationsByZip);
    }

    public Map<String, List<PropertyRecord>> getPropertiesByZip() {
        return Collections.unmodifiableMap(propertiesByZip);
    }

    public Map<String, Integer> getPopulationByZip() {
        return Collections.unmodifiableMap(populationByZip);
    }

    public List<PropertyRecord> getPropertiesForZip(String zip) {
        return propertiesByZip.getOrDefault(zip, Collections.emptyList());
    }

    public List<ParkingViolation> getViolationsForZip(String zip) {
        return violationsByZip.getOrDefault(zip, Collections.emptyList());
    }

    public Integer getPopulationForZip(String zip) {
        return populationByZip.get(zip);
    }
}