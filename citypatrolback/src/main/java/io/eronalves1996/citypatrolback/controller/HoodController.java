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
import io.eronalves1996.citypatrolback.model.Hood;
import io.eronalves1996.citypatrolback.repository.CityRepository;
import io.eronalves1996.citypatrolback.repository.HoodRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/city/{cityId}/hood")
public class HoodController {

    private HoodRepository hoodRepository;
    private CityRepository cityRepository;

    public HoodController(HoodRepository hoodRepository, CityRepository cityRepository) {
        this.hoodRepository = hoodRepository;
        this.cityRepository = cityRepository;
    }

    @Operation(summary = "Get all hoods of some city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "An array containing all hoods for some city", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Hood[].class))),
            @ApiResponse(responseCode = "404", description = "City not found", content = @Content(mediaType = "application/json")) })
    @GetMapping
    public List<Hood> getHoodsForCity(@Parameter(description = "id of the city") @PathVariable("cityId") int cityId) {
        List<Hood> hoodies = hoodRepository.findByCityId(cityId);
        if (hoodies.size() != 0)
            return hoodies;
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City doesn't exists or doesn't have any hoods");
    }

    @Operation(summary = "Get a hood of some city by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hood founded", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Hood.class))),
            @ApiResponse(responseCode = "404", description = "City/Hood not found", content = @Content(mediaType = "application/json")) })
    @GetMapping("/{id}")
    public Hood getHood(@Parameter(description = "id of the city") @PathVariable("cityId") int cityId,
            @Parameter(description = "id of the hood") @PathVariable("id") int id) {
        Hood hood = hoodRepository.findByCityIdAndId(cityId, id);
        if (hood == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Hood doesn't exist or doesn't on this city or city doesn't exist");
        return hood;
    }

    @Operation(summary = "Create a hood in some city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "City created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Hood.class))),
            @ApiResponse(responseCode = "404", description = "City not found", content = @Content(mediaType = "application/json")) })
    @PostMapping
    public Hood createHood(@Parameter(description = "id of the city") @PathVariable("cityId") int cityId,
            @RequestBody Hood hood) {
        Optional<City> city = cityRepository.findById(cityId);
        if (!city.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City doesn't exist");
        hood.setCity(city.get());
        return hoodRepository.save(hood);
    }

    @Operation(summary = "Update a hood in some city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hood updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Hood.class))),
            @ApiResponse(responseCode = "404", description = "City not found", content = @Content(mediaType = "application/json")) })
    @PutMapping
    public Hood updateHood(@Parameter(description = "id of the city") @PathVariable("cityId") int cityId,
            @RequestBody Hood hood) {
        return createHood(cityId, hood);
    }

    @Operation(summary = "Delete a hood in some city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hood deleted", content = @Content(mediaType = "application/json")) })
    @DeleteMapping("/{id}")
    @Transactional
    public void deleteHood(@Parameter(description = "id of the city") @PathVariable("cityId") int cityId,
            @Parameter(description = "id of the hood") @PathVariable("id") int id) {
        hoodRepository.deleteHoodByCityIdAndId(cityId, id);
    }

    @Operation(summary = "Get analytics about crime information about a hood")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Overall analytics about a hood", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnalyticsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Hood not found", content = @Content(mediaType = "application/json"))

    })
    @GetMapping("/{id}/analytics")
    public AnalyticsDTO getAnalytics(@Parameter(description = "id of the city") @PathVariable("cityId") int cityId,
            @Parameter(description = "id of the hood") @PathVariable("id") int id,
            @Parameter(description = "get results in some proportion") @RequestParam(name = "proportion", required = false) Integer proportion) {
        List<Object[]> analytics = hoodRepository.getAnalytics(id, cityId);
        if (analytics.size() == 0)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (proportion != null)
            return new AnalyticsDTO(analytics, proportion);
        return new AnalyticsDTO(analytics);
    }

}
