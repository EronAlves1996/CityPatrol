package io.eronalves1996.citypatrolback.repository;

import org.springframework.data.repository.CrudRepository;

import io.eronalves1996.citypatrolback.model.Crime;

public interface CrimeRepository extends CrudRepository<Crime, Integer> {

}
