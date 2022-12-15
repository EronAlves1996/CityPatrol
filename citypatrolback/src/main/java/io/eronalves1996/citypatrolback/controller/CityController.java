package io.eronalves1996.citypatrolback.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.eronalves1996.citypatrolback.model.City;
import io.eronalves1996.citypatrolback.repository.CityRepository;

@RestController
@RequestMapping("/city")
public class CityController {

    private CityRepository repository;

    @Autowired
    public CityController(CityRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Iterable<City> getCities() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public City getCityById(@PathVariable("id") int id) {
        Optional<City> city = repository.findById(id);
        if (city.isPresent()) {
            return city.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City doesn't exist");
    }

    @PostMapping
    public City createCity(@RequestBody City city) {
        return repository.save(city);
    }

    @PutMapping
    public City updateCityInformation(@RequestBody City city) {
        return createCity(city);
    }

    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable("id") int id) {
        repository.deleteById(id);
    }

}
