package io.eronalves1996.citypatrolback.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class City {

    @Id
    private String id;
    private String name;
    private long populationNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPopulationNumber() {
        return populationNumber;
    }

    public void setPopulationNumber(long populationNumber) {
        this.populationNumber = populationNumber;
    }

}
