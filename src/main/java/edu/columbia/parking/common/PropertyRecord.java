package edu.columbia.parking.common;

public class PropertyRecord {
    private final String zipCode;
    private final Double marketValue;
    private final Double totalLivableArea;

    public PropertyRecord(String zipCode, Double marketValue, Double totalLivableArea) {
        this.zipCode = zipCode;
        this.marketValue = marketValue;
        this.totalLivableArea = totalLivableArea;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Double getMarketValue() {
        return marketValue;
    }

    public Double getTotalLivableArea() {
        return totalLivableArea;
    }
}