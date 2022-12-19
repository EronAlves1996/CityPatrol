package io.eronalves1996.citypatrolback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import io.eronalves1996.citypatrolback.model.City;

public interface CityRepository extends CrudRepository<City, Integer> {

    @Query("SELECT c.populationNumber, cr.type, COUNT(cr.type) "
            + "FROM City c "
            + "JOIN c.hoods ch "
            + "JOIN ch.crimes cr "
            + "WHERE c.id = :cityId "
            + "GROUP BY cr.type")
    List<Object[]> getAnalytics(@Param("cityId") int cityId);

}
