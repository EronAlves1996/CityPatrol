package io.eronalves1996.citypatrolback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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
				() -> restTemplate.getForEntity("http://localhost:8080/city/" + 4, City.class)).getStatusCode();
		assertEquals(HttpStatusCode.valueOf(404), statusCode);
	}

	@AfterAll
	public static void unloadContainer() {
		executor.shutdown();
		executor.shutdownNow();
	}

}
