package io.eronalves1996.citypatrolback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Crime {

    @Id
    private int id;
    private CrimeType type;
    private String descrition;

    @ManyToOne
    @JsonIgnore
    private Hood hood;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CrimeType getType() {
        return type;
    }

    public void setType(CrimeType type) {
        this.type = type;
    }

    public String getDescrition() {
        return descrition;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    public Hood getHood() {
        return hood;
    }

    public void setHood(Hood hood) {
        this.hood = hood;
    }

}
