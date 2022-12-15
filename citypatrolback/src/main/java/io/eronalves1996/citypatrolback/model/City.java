package io.eronalves1996.citypatrolback.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class City {

    @Id
    private String id;
    private String name;
    private long populationNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    private List<Hood> hoods;

    public List<Hood> getHoods() {
        return hoods;
    }

    public void setHoods(List<Hood> hoods) {
        this.hoods = hoods;
    }

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
