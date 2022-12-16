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
import io.eronalves1996.citypatrolback.model.Region;
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

	@Test
	public void testCreateHood() {
		Iterable<City> cities = cityRepository.findAll();
		City city = cities.iterator().next();
		Hood hood = new Hood();
		hood.setName("Botafogo");
		hood.setPopulationNumber(2000);
		hood.setRegion(Region.SOUTH_EAST);
		ResponseEntity<Hood> response = restTemplate.postForEntity(API_URL + city.getId() + "/hood", hood, Hood.class);
		Hood hoodFetched = response.getBody();
		assertNotNull(hoodFetched.getId());
		assertNotNull(hoodFetched.getCity());
	}

	@Test
	@Transactional
	public void testUpdateHood() {
		Iterable<City> cities = cityRepository.findAll();
		City city = cities.iterator().next();
		Hood hood = city.getHoods().get(0);
		hood.setName("Janabia");
		restTemplate.put(API_URL + city.getId() + "/hood", hood);
		ResponseEntity<Hood> response = restTemplate.getForEntity(API_URL + city.getId() + "/hood/" + hood.getId(),
				Hood.class);
		Hood hoodFetched = response.getBody();
		assertEquals(hood, hoodFetched);
		assertEquals("Janabia", hoodFetched.getName());
	}

	@Test
	@Transactional
	public void testDeleteHood() {
		Iterable<City> cities = cityRepository.findAll();
		City city = cities.iterator().next();
		Hood hood = city.getHoods().get(0);
		restTemplate.delete(API_URL + city.getId() + "/hood/" + hood.getId());
		assertThrows(HttpClientErrorException.class,
				() -> restTemplate.getForEntity(API_URL + city.getId() + "/hood/" + hood.getId(), Hood.class));
	}

	@AfterAll
	public static void unloadContainer() {
		executor.shutdown();
		executor.shutdownNow();
	}

}
