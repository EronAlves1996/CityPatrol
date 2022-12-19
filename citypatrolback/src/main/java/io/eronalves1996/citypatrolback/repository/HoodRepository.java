package io.eronalves1996.citypatrolback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import io.eronalves1996.citypatrolback.model.Hood;

public interface HoodRepository extends CrudRepository<Hood, Integer> {

    List<Hood> findByCityId(int cityid);

    Hood findByCityIdAndId(int cityId, int id);

    void deleteHoodByCityIdAndId(int cityId, int id);

    @Query("SELECT h.populationNumber, cr.type, COUNT(cr.type) "
            + "FROM Hood h "
            + "JOIN h.crimes cr "
            + "WHERE h.id = :hoodId AND h.city.id= :cityId "
            + "GROUP BY cr.type")
    List<Object[]> getAnalytics(@Param("hoodId") int hoodId, @Param("cityId") int cityId);
}
