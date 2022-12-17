package io.eronalves1996.citypatrolback.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.eronalves1996.citypatrolback.model.City;
import io.eronalves1996.citypatrolback.model.Crime;
import io.eronalves1996.citypatrolback.model.Hood;
import io.eronalves1996.citypatrolback.repository.CityRepository;
import io.eronalves1996.citypatrolback.repository.CrimeRepository;
import io.eronalves1996.citypatrolback.repository.HoodRepository;

@RestController
@RequestMapping("/crime")
public class CrimeController {

    private CrimeRepository crimeRepository;
    private HoodRepository hoodRepository;
    private CityRepository cityRepository;

    public CrimeController(CrimeRepository crimeRepository, HoodRepository hoodRepository,
            CityRepository cityRepository) {
        this.crimeRepository = crimeRepository;
        this.hoodRepository = hoodRepository;
        this.cityRepository = cityRepository;
    }

    @GetMapping
    public Iterable<Crime> getAllCrimes(@RequestParam(name = "city", required = false) Integer cityId,
            @RequestParam(name = "hood", required = false) Integer hoodId) {
        if (cityId != null)
            return getAllCrimesFromCity(cityId);
        if (hoodId != null)
            return getAllCrimesFromHood(hoodId);
        return crimeRepository.findAll();
    }

    public List<Crime> getAllCrimesFromCity(int cityId) {
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

    public List<Crime> getAllCrimesFromHood(int hoodId) {
        Optional<Hood> hood = hoodRepository.findById(hoodId);
        if (hood.isPresent()) {
            return hood.get().getCrimes();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hood doesn't exist");
    }

    @GetMapping("/{id}")
    public Crime getCrimeById(@PathVariable("id") int id) {
        Optional<Crime> crime = crimeRepository.findById(id);
        if (crime.isPresent()) {
            return crime.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Crime registry doesn't exist");
    }

    @PostMapping
    public Crime createCrimeRegistry(@RequestBody Crime crime) {
        return crimeRepository.save(crime);
    }

    @PutMapping
    public Crime updateCrime(@RequestBody Crime crime) {
        return createCrimeRegistry(crime);
    }

    @DeleteMapping("/{id}")
    public void deleteCrime(@PathVariable("id") int id) {
        crimeRepository.deleteById(id);
    }

}
