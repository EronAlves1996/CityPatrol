package io.eronalves1996.citypatrolback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.eronalves1996.citypatrolback.repository.CityRepository;

@RestController
@RequestMapping("/city")
public class CityController {

    private CityRepository repository;

    @Autowired
    public CityController(CityRepository repository) {
        this.repository = repository;
    }

}
