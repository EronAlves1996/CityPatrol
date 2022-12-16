package io.eronalves1996.citypatrolback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import io.eronalves1996.citypatrolback.model.Hood;
import io.eronalves1996.citypatrolback.repository.CityRepository;
import io.eronalves1996.citypatrolback.repository.HoodRepository;

@SpringBootTest
class HoodControllerTest {

	private static final String API_URL = "http://localhost:8080/city/";
	private static ExecutorService executor = Executors.newSingleThreadExecutor();
	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private HoodRepository hoodRepository;

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
	public void testGetHoodsForCity() {
		Iterable<City> cities = cityRepository.findAll();
		City city = cities.iterator().next();
		List<Hood> hoodiesTested = hoodRepository.findByCityId(city.getId());
		ResponseEntity<Hood[]> response = restTemplate.getForEntity(API_URL + city.getId() + "/hood", Hood[].class);
		List<Hood> hoodiesFetched = Arrays.asList(response.getBody());
		assertNotNull(hoodiesFetched);
		assertEquals(hoodiesTested.size(), hoodiesFetched.size());
	}

	@Test
	public void testGetHoodsForCityThatNotExists() {
		Iterable<City> cities = cityRepository.findAll();
		City city = cities.iterator().next();
		assertThrows(HttpClientErrorException.class,
				() -> restTemplate.getForEntity(API_URL + (city.getId() + 10) + "/hood", Hood[].class));

	}

	@Test
	@Transactional
	public void testGetCertainHoodInCity() {
		Iterable<City> cities = cityRepository.findAll();
		City city = cities.iterator().next();
		List<Hood> hoods = city.getHoods();
		ResponseEntity<Hood> response = restTemplate
				.getForEntity(API_URL + city.getId() + "/hood/" + hoods.get(0).getId(), Hood.class);
		Hood hood = response.getBody();
		assertEquals(hoods.get(0), hood);
	}

	@Test
	@Transactional
	public void testGetCertainHoodInACityThatDoesntExists() {
		Iterable<City> cities = cityRepository.findAll();
		City city = cities.iterator().next();
		List<Hood> hoods = city.getHoods();
		assertThrows(HttpClientErrorException.class, () -> restTemplate
				.getForEntity(API_URL + city.getId() + 100 + "/hood/" + hoods.get(0).getId(), Hood.class));
	}

	@Test
	@Transactional
	public void testGetCertainHoodThatNotExistsInACityThatExists() {
		Iterable<City> cities = cityRepository.findAll();
		City city = cities.iterator().next();
		List<Hood> hoods = city.getHoods();
		assertThrows(HttpClientErrorException.class, () -> restTemplate
				.getForEntity(API_URL + city.getId() + "/hood/" + hoods.get(0).getId() + 100, Hood.class));
	}

	@Test
	@Transactional
	public void testGetCertainHoodExistsButNotOnCertainCity() {
		Iterable<City> cities = cityRepository.findAll();
		City city = cities.iterator().next();
		City city2 = cities.iterator().next();
		List<Hood> hoods = city.getHoods();
		assertThrows(HttpClientErrorException.class, () -> restTemplate
				.getForEntity(API_URL + city2.getId() + "/hood/" + hoods.get(0).getId() + 100, Hood.class));
	}

	@AfterAll
	public static void unloadContainer() {
		executor.shutdown();
		executor.shutdownNow();
	}

}
