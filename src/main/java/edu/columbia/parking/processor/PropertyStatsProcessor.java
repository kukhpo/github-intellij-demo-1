package edu.columbia.parking.processor;

import edu.columbia.parking.common.PropertyRecord;
import edu.columbia.parking.data.DataRepository;

import java.util.*;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Objects;

public class PropertyStatsProcessor {

    private final DataRepository repository;

    private final Map<String, Long> avgMarketCache = new HashMap<>();
    private final Map<String, Long> avgAreaCache = new HashMap<>();
    private final Map<String, Long> marketPerCapitaCache = new HashMap<>();
    private final Map<String, Long> avgValuePerSqFtCache = new HashMap<>();

    public PropertyStatsProcessor(DataRepository repository) {
        this.repository = repository;
    }

    /** Menu option #3 */
    public long getAverageMarketValue(String zip) {
        return avgMarketCache.computeIfAbsent(zip, this::computeAverageMarketValueInternal);
    }

    private long computeAverageMarketValueInternal(String zip) {
        List<PropertyRecord> props = repository.getPropertiesForZip(zip);
        double sum = 0;
        int count = 0;
        for (PropertyRecord p : props) {
            Double mv = p.getMarketValue();
            if (mv != null) {
                sum += mv;
                count++;
            }
        }
        if (count == 0) return 0;
        return Math.round(sum / count);
    }

    /** Menu option #4 */
    public long getAverageTotalLivableArea(String zip) {
        return avgAreaCache.computeIfAbsent(zip, this::computeAverageTotalLivableAreaInternal);
    }

    private long computeAverageTotalLivableAreaInternal(String zip) {
        List<PropertyRecord> props = repository.getPropertiesForZip(zip);
        double sum = 0;
        int count = 0;
        for (PropertyRecord p : props) {
            Double area = p.getTotalLivableArea();
            if (area != null) {
                sum += area;
                count++;
            }
        }
        if (count == 0) return 0;
        return Math.round(sum / count);
    }

    /** Menu option #5 */
    public long getMarketValuePerCapita(String zip) {
        return marketPerCapitaCache.computeIfAbsent(zip, this::computeMarketValuePerCapitaInternal);
    }

    private long computeMarketValuePerCapitaInternal(String zip) {
        List<PropertyRecord> props = repository.getPropertiesForZip(zip);
        if (props.isEmpty()) return 0;

        double totalMarket = props.stream()
                .map(PropertyRecord::getMarketValue)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();

        if (totalMarket <= 0) return 0;

        Integer population = repository.getPopulationForZip(zip);
        if (population == null || population <= 0) {
            return 0;
        }

        return Math.round(totalMarket / population);
    }

    /** Menu option #6: Average value per square foot in ZIP code. */
    public long getAverageValuePerSquareFoot(String zip) {
        return avgValuePerSqFtCache.computeIfAbsent(zip, this::computeAverageValuePerSqFtInternal);
    }

    private long computeAverageValuePerSqFtInternal(String zip) {
        List<PropertyRecord> props = repository.getPropertiesForZip(zip);
        double sumRatio = 0;
        int count = 0;
        for (PropertyRecord p : props) {
            Double mv = p.getMarketValue();
            Double area = p.getTotalLivableArea();
            if (mv != null && area != null && area > 0) {
                sumRatio += (mv / area);
                count++;
            }
        }
        if (count == 0) return 0;
        return Math.round(sumRatio / count);
    }

    public long countHomesInMarketRange(String zip, double minValue, double maxValue) {
        if (minValue > maxValue) {
            double tmp = minValue;
            minValue = maxValue;
            maxValue = tmp;
        }

        final double min = minValue;
        final double max = maxValue;

        List<PropertyRecord> props = repository.getPropertiesForZip(zip);

        return props.stream()
                .map(PropertyRecord::getMarketValue)
                .filter(Objects::nonNull)
                .filter(v -> v >= min && v <= max)
                .count();
    }

    public double applyStrategy(String zip, CalculationStrategy strategy) {
        return strategy.compute(zip);
    }
}