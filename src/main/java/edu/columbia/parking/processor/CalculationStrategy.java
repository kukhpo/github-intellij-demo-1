package edu.columbia.parking.processor;

@FunctionalInterface
public interface CalculationStrategy {
    double compute(String zipCode);
}
