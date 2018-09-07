package ru.gzpn.spc.csl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Test
	public void contextLoads() {

		try {
			final ObjectMapper mapper = new ObjectMapper();
			TestJson json = new TestJson();
			json.setUnums(Arrays.asList(new TestEnum[] { TestEnum.ONE, TestEnum.TWO }));
			json.setName("TestEnum");
			System.out.println(mapper.writeValueAsString(json));
		} catch (final Exception ex) {
			throw new RuntimeException("Failed to convert String to Invoice: " + ex.getMessage(), ex);
		}
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