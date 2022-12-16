package io.eronalves1996.citypatrolback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Crime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "crime_type")
    @Enumerated(EnumType.STRING)
    private CrimeType type;

    @Column(name = "crime_description")
    private String descrition;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hood_id", nullable = false)
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
