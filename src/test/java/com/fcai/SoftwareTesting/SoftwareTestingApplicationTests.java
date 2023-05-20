package com.fcai.SoftwareTesting;

import org.junit.jupiter.api.Test;
import org.junit.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SoftwareTestingApplicationTests {

	@Test
	void contextLoads() {
		assertNotEquals(1, 2);
	}

}
