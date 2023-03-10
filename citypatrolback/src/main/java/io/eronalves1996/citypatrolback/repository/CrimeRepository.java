package io.eronalves1996.citypatrolback.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.eronalves1996.citypatrolback.model.Crime;
import io.eronalves1996.citypatrolback.model.Hood;

public interface CrimeRepository extends CrudRepository<Crime, Integer> {

    List<Crime> findByHoodCityId(int cityId);

    List<Crime> findByHoodId(int hoodId);

    List<Crime> findByHoodIn(List<Hood> hoods);
}
