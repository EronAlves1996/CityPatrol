package io.eronalves1996.citypatrolback.controller;

import org.springframework.beans.factory.annotation.Autowired;

import io.eronalves1996.citypatrolback.repository.HoodRepository;

public class HoodController {

    private HoodRepository repository;

    @Autowired
    public HoodController(HoodRepository repository) {
        this.repository = repository;
    }

}
