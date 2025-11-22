package edu.columbia.parking.common;

public class ZipCodePopulation {
    private final String zipCode;
    private final int population;

    public ZipCodePopulation(String zipCode, int population) {
        this.zipCode = zipCode;
        this.population = population;
    }

    public String getZipCode() {
        return zipCode;
    }

    public int getPopulation() {
        return population;
    }
}