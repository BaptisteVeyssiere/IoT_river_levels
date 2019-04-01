package com.flood.monitoring;

public class Main {

    public static void main(String[] args) {
        FloodDataCollector collector = new FloodDataCollector();
        collector.collectAndSaveData();
    }
}