package ru.gzpn.spc.csl.model.repositories;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseRepositoryImplTest {

	@Autowired
	HProjectRepository baseRepository;
	
	@BeforeClass
	public static void fillData() {
		
	}
}
