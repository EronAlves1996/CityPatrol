package io.eronalves1996.citypatrolback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.eronalves1996.citypatrolback.model.City;
import io.eronalves1996.citypatrolback.repository.CityRepository;

@SpringBootTest
class CitypatrolbackApplicationTests {

	private static ExecutorService executor = Executors.newSingleThreadExecutor();
	private RestTemplate restTemplate = new RestTemplate();

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
	public void testGetCities() {
		Iterable<City> cities = cityRepository.findAll();
		ResponseEntity<Iterable> response = restTemplate.getForEntity("http://localhost:8080/city", Iterable.class);
		Iterable<City> citiesFetched = response.getBody();
		assertNotNull(citiesFetched);
		assertEquals(List.of(cities).size(), List.of(citiesFetched).size());
	}

	@Test
	public void testGetCityById() {
		Optional<City> testedCity = cityRepository.findById(1);
		if (testedCity.isPresent()) {
			int id = testedCity.get().getId();
			ResponseEntity<City> cityFetched = restTemplate.getForEntity("http://localhost:8080/city/" + id,
					City.class);
			assertEquals(testedCity.get(), cityFetched.getBody());
		}
	}

	@Test
	public void testGetCityByAnIdThatNotExists() {
		HttpStatusCode statusCode = assertThrows(HttpClientErrorException.class,
				() -> restTemplate.getForEntity("http://localhost:8080/city/" + 30, City.class)).getStatusCode();
		assertEquals(HttpStatusCode.valueOf(404), statusCode);
	}

	@Test
	public void testPostCity() {
		City city = new City();
		city.setName("Divinópolis");
		city.setPopulationNumber(20000);
		ResponseEntity<City> cityCreated = restTemplate.postForEntity("http://localhost:8080/city", city, City.class);
		assertNotNull(cityCreated.getBody().getId());
	}

	@Test
	public void testPutCity() {
		City city = restTemplate.getForEntity("http://localhost:8080/city/1", City.class).getBody();
		city.setName("Josafá");
		city.setHoods(null);
		restTemplate.put("http://localhost:8080/city", city);
		City city2 = restTemplate.getForEntity("http://localhost:8080/city/1", City.class).getBody();
		assertNotNull(city.getName(), city2.getName());
	}

	@Test
	public void testDeleteCity() {
		ResponseEntity<City[]> response = restTemplate.getForEntity("http://localhost:8080/city",
				City[].class);
		List<City> cities = Arrays.asList(response.getBody());
		int lastId = cities.get(cities.size() - 1).getId();
		restTemplate.delete("http://localhost:8080/city/" + lastId);
		assertThrows(HttpClientErrorException.class,
				() -> restTemplate.getForEntity("http://localhost:8080/city/" + lastId, City.class));
	}

	@AfterAll
	public static void unloadContainer() {
		executor.shutdown();
		executor.shutdownNow();
	}

}
