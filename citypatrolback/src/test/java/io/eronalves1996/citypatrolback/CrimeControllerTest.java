package io.eronalves1996.citypatrolback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import io.eronalves1996.citypatrolback.model.City;
import io.eronalves1996.citypatrolback.model.Crime;
import io.eronalves1996.citypatrolback.model.CrimeType;
import io.eronalves1996.citypatrolback.model.Hood;
import io.eronalves1996.citypatrolback.repository.CityRepository;
import io.eronalves1996.citypatrolback.repository.CrimeRepository;
import io.eronalves1996.citypatrolback.repository.HoodRepository;

@SpringBootTest
class CrimeControllerTest {

	private static final String API_URL = "http://localhost:8080/crime";
	private static ExecutorService executor = Executors.newSingleThreadExecutor();
	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private CrimeRepository crimeRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private HoodRepository hoodRepository;

	@BeforeAll
	public static void loadContainer() throws InterruptedException {
		executor.execute(() -> {
			CitypatrolbackApplication.main(new String[0]);
		});
		Thread.sleep(2000L);
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testGetAllCrimes() {
		Iterable<Crime> crimes = crimeRepository.findAll();
		List<Crime> crimesTested = new ArrayList<>();
		crimes.forEach(crimesTested::add);

		ResponseEntity<Crime[]> response = restTemplate.getForEntity(API_URL, Crime[].class);
		List<Crime> crimesFetched = Arrays.asList(response.getBody());
		assertNotNull(crimesFetched);
		assertEquals(crimesTested.size(), crimesFetched.size());
	}

	@Test
	@Transactional
	public void testGetAllCrimesForCity() {
		Iterable<City> cities = cityRepository.findAll();
		City city = cities.iterator().next();
		List<Crime> crimesForCity = crimeRepository.findByHoodCityId(city.getId());
		ResponseEntity<Crime[]> response = restTemplate.getForEntity(API_URL + "?city=" + city.getId(), Crime[].class);
		List<Crime> crimesFetched = Arrays.asList(response.getBody());
		assertNotNull(crimesFetched);
		assertEquals(crimesForCity.size(), crimesFetched.size());
	}

	@Test
	@Transactional
	public void testGetAllCrimesForCityThatDoesntExist() {
		// CITY ID 100 doesnt exist on test database
		assertThrows(HttpClientErrorException.class,
				() -> restTemplate.getForEntity(API_URL + "?city=" + 100, Crime[].class));
	}

	@Test
	public void testGetAllCrimesForHood() {
		Iterable<Hood> hoodies = hoodRepository.findAll();
		Hood hood = hoodies.iterator().next();
		List<Crime> crimesForHood = crimeRepository.findByHoodId(hood.getId());

		ResponseEntity<Crime[]> response = restTemplate.getForEntity(API_URL + "?hood=" + hood.getId(), Crime[].class);
		List<Crime> crimesFetched = Arrays.asList(response.getBody());
		assertNotNull(crimesFetched);
		assertEquals(crimesForHood.size(), crimesFetched.size());
	}

	@Test
	public void testGetCrimeById() {
		Iterable<Crime> crimes = crimeRepository.findAll();
		Crime crime = crimes.iterator().next();
		ResponseEntity<Crime> response = restTemplate.getForEntity(API_URL + "/" + crime.getId(), Crime.class);
		Crime crimeFetched = response.getBody();

		assertEquals(crime, crimeFetched);
	}

	@Test
	public void testCreateCrime() {
		Hood hood = hoodRepository.findAll().iterator().next();
		Crime crime = new Crime();
		crime.setType(CrimeType.MURDER);
		crime.setHood(hood);
		crime.setDescrition("Ele me matou");

		ResponseEntity<Crime> response = restTemplate.postForEntity(API_URL, crime, Crime.class);
		Crime crimeCreated = response.getBody();
		assertNotNull(crimeCreated.getId());
		assertEquals(crime.getDescrition(), crimeCreated.getDescrition());
	}

	@Test
	public void testUpdateCrime() {
		Crime crime = crimeRepository.findAll().iterator().next();
		crime.setDescrition("Customized Description");

		restTemplate.put(API_URL, crime);
		ResponseEntity<Crime> response = restTemplate.getForEntity(API_URL + "/" + crime.getId(), Crime.class);

		Crime crimeUpdated = response.getBody();
		assertEquals(crime, crimeUpdated);
		assertEquals(crime.getDescrition(), crimeUpdated.getDescrition());
	}

	@AfterAll
	public static void unloadContainer() {
		executor.shutdown();
		executor.shutdownNow();
	}

}
