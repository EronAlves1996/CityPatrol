package io.eronalves1996.citypatrolback.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.eronalves1996.citypatrolback.model.CrimeType;

public class CityAnalyticsDTO {

    private long populationNumber;
    private Map<CrimeType, Double> quantity = new HashMap<>();

    public CityAnalyticsDTO(List<Object[]> rawAnalytics) {
        setPopulation((long) rawAnalytics.get(0)[0]);
        rawAnalytics.forEach(analytic -> {
            quantity.put((CrimeType) analytic[1], Long.valueOf((long) analytic[2]).doubleValue());
        });
    }

    public CityAnalyticsDTO(List<Object[]> rawAnalytics, int proportion) {
        setPopulation((long) rawAnalytics.get(0)[0]);
        rawAnalytics.forEach(analytic -> {
            quantity.put((CrimeType) analytic[1],
                    (long) analytic[2] / Long.valueOf(populationNumber).doubleValue() * proportion);
        });

    }

    public Map<CrimeType, Double> getQuantity() {
        return quantity;
    }

    private void setPopulation(long population) {
        this.populationNumber = population;
    }

    public long getPopulationNumber() {
        return populationNumber;
    }

}
