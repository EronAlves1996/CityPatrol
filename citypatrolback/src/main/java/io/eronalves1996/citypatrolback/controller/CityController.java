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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.eronalves1996.citypatrolback.dto.AnalyticsDTO;
import io.eronalves1996.citypatrolback.model.City;
import io.eronalves1996.citypatrolback.repository.CityRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/city")
public class CityController {

    private CityRepository repository;

    public CityController(CityRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Get all cities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "An array containing all cities", content = @Content(mediaType = "application/json", schema = @Schema(implementation = City[].class))) })
    @GetMapping
    @Transactional
    public Iterable<City> getCities() {
        return repository.findAll();
    }

    @Operation(summary = "Get a city by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found a city", content = @Content(mediaType = "application/json", schema = @Schema(implementation = City.class))),
            @ApiResponse(responseCode = "404", description = "City not found", content = @Content)
    })
    @GetMapping("/{id}")
    public City getCityById(@Parameter(description = "id of the city to be searched") @PathVariable("id") int id) {
        Optional<City> city = repository.findById(id);
        if (city.isPresent()) {
            return city.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City doesn't exist");
    }

    @Operation(summary = "Create a new city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New city created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = City.class))),
    })
    @PostMapping
    public City createCity(@RequestBody City city) {
        return repository.save(city);
    }

    @Operation(summary = "Update an information about a city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "City information updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = City.class))),
    })
    @PutMapping
    public City updateCityInformation(@RequestBody City city) {
        return createCity(city);
    }

    @Operation(summary = "Delete some city by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City deleted", content = @Content(mediaType = "application/json")),
    })
    @DeleteMapping("/{id}")
    public void deleteCity(@Parameter(description = "id of the city to be deleted") @PathVariable("id") int id) {
        repository.deleteById(id);
    }

    @Operation(summary = "Get analytics about crime information about a city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Overall analytics about a city", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnalyticsDTO.class))),
            @ApiResponse(responseCode = "404", description = "City not found", content = @Content(mediaType = "application/json"))

    })
    @GetMapping("/{id}/analytics")
    public AnalyticsDTO getOverallCrimeAnalytics(
            @Parameter(description = "id of the city to get analytics") @PathVariable("id") int id,
            @Parameter(description = "get results in some proportion") @RequestParam(name = "proportion", required = false) Integer proportion) {
        List<Object[]> analytics = repository.getAnalytics(id);
        if (analytics.size() == 0)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (proportion != null)
            return new AnalyticsDTO(analytics, proportion);
        return new AnalyticsDTO(analytics);
    }

}
