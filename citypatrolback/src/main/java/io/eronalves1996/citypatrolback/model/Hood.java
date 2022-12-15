package io.eronalves1996.citypatrolback.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Hood {

    @Id
    private int id;
    private String name;
    private Region region;
    private long populationNumber;

    @ManyToOne
    private City city;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Crime> crimes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public long getPopulationNumber() {
        return populationNumber;
    }

    public void setPopulationNumber(long populationNumber) {
        this.populationNumber = populationNumber;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

}
