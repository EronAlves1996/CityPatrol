package io.eronalves1996.citypatrolback.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.eronalves1996.citypatrolback.model.City;
import io.eronalves1996.citypatrolback.model.Crime;
import io.eronalves1996.citypatrolback.repository.CityRepository;
import io.eronalves1996.citypatrolback.repository.CrimeRepository;
import io.eronalves1996.citypatrolback.repository.HoodRepository;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/crime")
public class CrimeController {

    private CrimeRepository crimeRepository;
    private HoodRepository hoodRepository;
    private CityRepository cityRepository;

    @Autowired
    public CrimeController(CrimeRepository crimeRepository, HoodRepository hoodRepository,
            CityRepository cityRepository) {
        this.crimeRepository = crimeRepository;
        this.hoodRepository = hoodRepository;
        this.cityRepository = cityRepository;
    }

    @GetMapping
    public Iterable<Crime> getAllCrimes() {
        return crimeRepository.findAll();
    }

    @GetMapping
    public List<Crime> getAllCrimesFromCity(@PathParam("city") int cityId) {
        Optional<City> city = cityRepository.findById(cityId);
        if (city.isPresent()) {
            return city.get().getHoods().stream().map(hood -> hood.getCrimes()).reduce(new ArrayList<Crime>(),
                    (finalCrimeList, hoodCrimes) -> {
                        hoodCrimes.forEach(finalCrimeList::add);
                        return finalCrimeList;
                    });
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City doesn't exist");
    }

}
