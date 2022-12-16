package io.eronalves1996.citypatrolback.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.eronalves1996.citypatrolback.model.Hood;

public interface HoodRepository extends CrudRepository<Hood, Integer> {

    List<Hood> findByCityId(int cityid);

    Hood findByCityIdAndId(int cityId, int id);

    void deleteHoodByCityIdAndId(int cityId, int id);
}
