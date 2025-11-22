package edu.columbia.parking.common;

public enum InputFormat {
    CSV,
    JSON;

    public static InputFormat fromString(String s) {
        if ("csv".equals(s)) {
            return CSV;
        } else if ("json".equals(s)) {
            return JSON;
        } else {
            throw new IllegalArgumentException("Input format must be 'csv' or 'json'");
        }
    }
}