package io.eronalves1996.citypatrolback.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.eronalves1996.citypatrolback.model.City;
import io.eronalves1996.citypatrolback.model.Hood;
import io.eronalves1996.citypatrolback.repository.CityRepository;
import io.eronalves1996.citypatrolback.repository.HoodRepository;

@RestController
@RequestMapping("/city/{cityId}/hood")
public class HoodController {

    private HoodRepository hoodRepository;
    private CityRepository cityRepository;

    @Autowired
    public HoodController(HoodRepository hoodRepository, CityRepository cityRepository) {
        this.hoodRepository = hoodRepository;
        this.cityRepository = cityRepository;
    }

    @GetMapping
    public List<Hood> getHoodsForCity(@PathVariable("cityId") int cityId) {
        Optional<City> city = cityRepository.findById(cityId);
        if (city.isPresent())
            return hoodRepository.findByCity(city.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City doesn't exist");
    }

    @GetMapping("/{id}")
    public Hood getHood(@PathVariable("cityId") int cityId, @PathVariable("id") int id) {
        Optional<City> city = cityRepository.findById(cityId);
        if (city.isPresent())
            return hoodRepository.findByCityAndId(city.get(), id);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City or hood doesn't exist");

    }

    @PostMapping
    public Hood createHood(@PathVariable("cityId") int cityId, Hood hood) {
        Optional<City> city = cityRepository.findById(cityId);
        if (!city.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City doesn't exist");
        hood.setCity(city.get());
        return hoodRepository.save(hood);
    }

    @PutMapping
    public Hood updateHood(@PathVariable("cityId") int cityId, Hood hood) {
        return createHood(cityId, hood);
    }

}
