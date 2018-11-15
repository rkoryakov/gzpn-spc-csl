package ru.gzpn.spc.csl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ApplicationTests {
	public static final Logger logger = LoggerFactory.getLogger(ApplicationTests.class);
	@Test
	public void contextLoads() {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			TestJson json = new TestJson();
			json.setUnums(Arrays.asList(new TestEnum[] { TestEnum.ONE, TestEnum.TWO }));
			json.setName("TestEnum");
			logger.debug(mapper.writeValueAsString(json));
		} catch (final Exception ex) {
			throw new RuntimeException("Failed to convert String to Invoice: " + ex.getMessage(), ex);
		}
	}
	
	@Test
	public void randomNumberTest() {
		double n = Math.random();
		logger.debug("random value {};  5 * {} = {}, rounded {}", n, n, 5*n, (int)(5*n));
		n = Math.random();
		logger.debug("random value {};  5 * {} = {}, rounded {}", n, n, 5*n, (int)(5*n));
		n = Math.random();
		logger.debug("random value {};  5 * {} = {}, rounded {}", n, n, 5*n, (int)(5*n));
		n = Math.random();
		logger.debug("random value {};  5 * {} = {}, rounded {}", n, n, 5*n, (int)(5*n));
		n = Math.random();
		logger.debug("random value {};  5 * {} = {}, rounded {}", n, n, 5*n, (int)(5*n));
		n = Math.random();
		logger.debug("random value {};  5 * {} = {}, rounded {}", n, n, 5*n, (int)(5*n));
		n = Math.random();
	}
}


enum TestEnum {
	ONE, TWO, THREE;
}

class TestJson implements Serializable {
	private List<TestEnum> enums;
	private String name;

	public List<TestEnum> getUnums() {
		return enums;
	}

	public void setUnums(List<TestEnum> unums) {
		this.enums = unums;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}