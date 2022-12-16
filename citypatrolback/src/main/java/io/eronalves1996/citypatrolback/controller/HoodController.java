package io.eronalves1996.citypatrolback.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
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
import io.eronalves1996.citypatrolback.model.Hood;
import io.eronalves1996.citypatrolback.repository.CityRepository;
import io.eronalves1996.citypatrolback.repository.HoodRepository;

@RestController
@RequestMapping("/city/{cityId}/hood")
public class HoodController {

    private HoodRepository hoodRepository;
    private CityRepository cityRepository;

    public HoodController(HoodRepository hoodRepository, CityRepository cityRepository) {
        this.hoodRepository = hoodRepository;
        this.cityRepository = cityRepository;
    }

    @GetMapping
    public List<Hood> getHoodsForCity(@PathVariable("cityId") int cityId) {
        List<Hood> hoodies = hoodRepository.findByCityId(cityId);
        if (hoodies.size() != 0)
            return hoodies;
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City doesn't exists or doesn't have any hoods");
    }

    @GetMapping("/{id}")
    public Hood getHood(@PathVariable("cityId") int cityId, @PathVariable("id") int id) {
        Hood hood = hoodRepository.findByCityIdAndId(cityId, id);
        if (hood == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Hood doesn't exist or doesn't on this city or city doesn't exist");
        return hood;
    }

    @PostMapping
    public Hood createHood(@PathVariable("cityId") int cityId, @RequestBody Hood hood) {
        Optional<City> city = cityRepository.findById(cityId);
        if (!city.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City doesn't exist");
        hood.setCity(city.get());
        return hoodRepository.save(hood);
    }

    @PutMapping
    public Hood updateHood(@PathVariable("cityId") int cityId, @RequestBody Hood hood) {
        return createHood(cityId, hood);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deleteHood(@PathVariable("cityId") int cityId, @PathVariable("id") int id) {
        hoodRepository.deleteHoodByCityIdAndId(cityId, id);
    }

}
