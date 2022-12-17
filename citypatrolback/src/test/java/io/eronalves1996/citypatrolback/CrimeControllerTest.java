package io.eronalves1996.citypatrolback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import io.eronalves1996.citypatrolback.model.City;
import io.eronalves1996.citypatrolback.model.Crime;
import io.eronalves1996.citypatrolback.repository.CityRepository;
import io.eronalves1996.citypatrolback.repository.CrimeRepository;

@SpringBootTest
class CrimeControllerTest {

	private static final String API_URL = "http://localhost:8080/crime";
	private static ExecutorService executor = Executors.newSingleThreadExecutor();
	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private CrimeRepository crimeRepository;

	@Autowired
	private CityRepository cityRepository;

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

	@AfterAll
	public static void unloadContainer() {
		executor.shutdown();
		executor.shutdownNow();
	}

}
