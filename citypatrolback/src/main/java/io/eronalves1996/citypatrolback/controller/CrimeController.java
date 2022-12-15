package io.eronalves1996.citypatrolback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.eronalves1996.citypatrolback.repository.CrimeRepository;

@RestController
@RequestMapping("/crime")
public class CrimeController {

    private CrimeRepository repository;

    @Autowired
    public CrimeController(CrimeRepository repository) {
        this.repository = repository;
    }

}
