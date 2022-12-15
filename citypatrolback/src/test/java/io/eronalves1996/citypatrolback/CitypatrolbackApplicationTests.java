package io.eronalves1996.citypatrolback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CitypatrolbackApplicationTests {

	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private Future<?> contextExecution;

	@BeforeAll
	public void loadContainer() {
		executor.execute(() -> {
			CitypatrolbackApplication.main(new String[0]);
		});
	}

	@Test
	void contextLoads() {
	}

	@AfterAll
	public void unloadContainer() {
		executor.shutdown();
		executor.shutdownNow();
	}

}
