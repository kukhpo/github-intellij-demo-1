package edu.columbia.parking.processor;

import edu.columbia.parking.data.DataRepository;

import java.util.Map;

public class PopulationProcessor {

    private final DataRepository repository;

    public PopulationProcessor(DataRepository repository) {
        this.repository = repository;
    }

    public long getTotalPopulation() {
        long sum = 0;
        for (int value : repository.getPopulationByZip().values()) {
            sum += value;
        }
        return sum;
    }
}