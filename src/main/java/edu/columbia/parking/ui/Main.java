package edu.columbia.parking.ui;

import edu.columbia.parking.common.InputFormat;
import edu.columbia.parking.data.DataRepository;
import edu.columbia.parking.processor.FinesProcessor;
import edu.columbia.parking.processor.PopulationProcessor;
import edu.columbia.parking.processor.PropertyStatsProcessor;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;

public class Main {

    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.println("Usage: java ... Main <csv|json> <parkingFile> <propertyFile> <populationFile>");
            return;
        }

        InputFormat format;
        try {
            format = InputFormat.fromString(args[0]);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: first argument must be 'csv' or 'json'.");
            return;
        }

        String parkingFile = args[1];
        String propertyFile = args[2];
        String populationFile = args[3];

        DataRepository repository = DataRepository.getInstance();

        try {
            repository.loadAll(format, parkingFile, propertyFile, populationFile);
        } catch (IOException e) {
            System.err.println("Error reading input files: " + e.getMessage());
            return;
        }

        PopulationProcessor populationProcessor = new PopulationProcessor(repository);
        FinesProcessor finesProcessor = new FinesProcessor(repository);
        PropertyStatsProcessor propertyProcessor = new PropertyStatsProcessor(repository);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            String choiceLine = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(choiceLine);
            } catch (NumberFormatException e) {
                System.out.println("Invalid selection. Please enter a number from 0 to 7.");
                continue;
            }

            switch (choice) {
                case 0:
                    System.out.println("Goodbye!");
                    return;
                case 1:
                    handleTotalPopulation(populationProcessor);
                    break;
                case 2:
                    handleFinesPerCapita(finesProcessor);
                    break;
                case 3:
                    handleAverageMarketValue(scanner, propertyProcessor);
                    break;
                case 4:
                    handleAverageLivableArea(scanner, propertyProcessor);
                    break;
                case 5:
                    handleMarketValuePerCapita(scanner, propertyProcessor);
                    break;
                case 6:
                    handleAverageValuePerSqFt(scanner, propertyProcessor);
                    break;
                case 7:
                    handleRangeCount(scanner, propertyProcessor);
                    break;
                default:
                    System.out.println("Invalid selection. Please enter a number from 0 to 7.");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("Please select an option:");
        System.out.println("1. Total population for all ZIP Codes");
        System.out.println("2. Parking fines per capita for each ZIP Code");
        System.out.println("3. Average residential market value for a ZIP Code");
        System.out.println("4. Average residential total livable area for a ZIP Code");
        System.out.println("5. Residential market value per capita for a ZIP Code");
        System.out.println("6. Average market value per square foot for a ZIP Code");
        System.out.println("7. Number of homes within a market value range for a ZIP Code");
        System.out.println("0. Quit");
        System.out.print("Enter your choice: ");
    }

    private static void handleTotalPopulation(PopulationProcessor populationProcessor) {
        long total = populationProcessor.getTotalPopulation();
        System.out.println(total);
    }

    private static void handleFinesPerCapita(FinesProcessor finesProcessor) {
        SortedMap<String, Double> map = finesProcessor.getFinesPerCapita();
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            String line = entry.getKey() + " " + FinesProcessor.formatFourDecimals(entry.getValue());
            System.out.println(line);
        }
    }

    private static void handleAverageMarketValue(Scanner scanner,
                                                 PropertyStatsProcessor propertyProcessor) {
        System.out.print("Enter ZIP Code: ");
        String zip = scanner.nextLine().trim();
        long avg = propertyProcessor.getAverageMarketValue(zip);
        System.out.println(avg);
    }

    private static void handleAverageLivableArea(Scanner scanner,
                                                 PropertyStatsProcessor propertyProcessor) {
        System.out.print("Enter ZIP Code: ");
        String zip = scanner.nextLine().trim();
        long avg = propertyProcessor.getAverageTotalLivableArea(zip);
        System.out.println(avg);
    }

    private static void handleMarketValuePerCapita(Scanner scanner,
                                                   PropertyStatsProcessor propertyProcessor) {
        System.out.print("Enter ZIP Code: ");
        String zip = scanner.nextLine().trim();
        long value = propertyProcessor.getMarketValuePerCapita(zip);
        System.out.println(value);
    }

    private static void handleAverageValuePerSqFt(Scanner scanner,
                                                  PropertyStatsProcessor propertyProcessor) {
        System.out.print("Enter ZIP Code: ");
        String zip = scanner.nextLine().trim();
        long value = propertyProcessor.getAverageValuePerSquareFoot(zip);
        System.out.println(value);
    }

    private static void handleRangeCount(Scanner scanner,
                                         PropertyStatsProcessor propertyProcessor) {
        System.out.print("Enter ZIP Code: ");
        String zip = scanner.nextLine().trim();

        System.out.print("Enter minimum market value: ");
        double min;
        try {
            min = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid minimum value.");
            return;
        }

        System.out.print("Enter maximum market value: ");
        double max;
        try {
            max = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid maximum value.");
            return;
        }

        long count = propertyProcessor.countHomesInMarketRange(zip, min, max);
        System.out.println(count);
    }
}