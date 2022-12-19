package io.eronalves1996.citypatrolback.controller;

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

import io.eronalves1996.citypatrolback.model.Crime;
import io.eronalves1996.citypatrolback.repository.CrimeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/crime")
public class CrimeController {

    private CrimeRepository crimeRepository;

    public CrimeController(CrimeRepository crimeRepository) {
        this.crimeRepository = crimeRepository;
    }

    @Operation(summary = "Get all Crimes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "An array containing all crimes", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Crime[].class)))
    })
    @GetMapping
    public Iterable<Crime> getAllCrimes(
            @Parameter(description = "id of the city") @RequestParam(name = "city", required = false) Integer cityId,
            @Parameter(description = "id of the hood") @RequestParam(name = "hood", required = false) Integer hoodId) {
        if (cityId != null)
            return getAllCrimesFromCity(cityId);
        if (hoodId != null)
            return getAllCrimesFromHood(hoodId);
        return crimeRepository.findAll();
    }

    private List<Crime> getAllCrimesFromCity(int cityId) {
        List<Crime> crimesForCity = crimeRepository.findByHoodCityId(cityId);
        if (crimesForCity.size() == 0)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City doesn't exist");
        return crimesForCity;
    }

    private List<Crime> getAllCrimesFromHood(int hoodId) {
        List<Crime> crimesForHood = crimeRepository.findByHoodId(hoodId);
        if (crimesForHood.size() == 0)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hood doesn't exist");
        return crimesForHood;
    }

    @Operation(summary = "Get a crime by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Crime founded", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Crime.class))),
            @ApiResponse(responseCode = "404", description = "Crime not founded", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public Crime getCrimeById(@Parameter(description = "Id of the crime") @PathVariable("id") int id) {
        Optional<Crime> crime = crimeRepository.findById(id);
        if (crime.isPresent()) {
            return crime.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Crime registry doesn't exist");
    }

    @Operation(summary = "Register a new crime")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Crime created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Crime.class))),
    })
    @PostMapping
    public Crime createCrimeRegistry(@RequestBody Crime crime) {
        return crimeRepository.save(crime);
    }

    @Operation(summary = "Update a crime register")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Crime updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Crime.class))),
    })
    @PutMapping
    public Crime updateCrime(@RequestBody Crime crime) {
        return createCrimeRegistry(crime);
    }

    @Operation(summary = "Delete a crime by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Crime deleted", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public void deleteCrime(@Parameter(description = "Id of the crime") @PathVariable("id") int id) {
        crimeRepository.deleteById(id);
    }

}
