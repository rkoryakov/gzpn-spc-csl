package ru.gzpn.spc.csl.services.bl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.junit4.SpringRunner;

import ru.gzpn.spc.csl.model.interfaces.IWorkSet;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkSetServiceTest extends WorkSetService {
	
	@Test
	public void countTest() {
		List<Order> orders = new ArrayList<>();
		
		orders.add(createSortOrder("name", Direction.ASC));
		orders.add(createSortOrder("code", Direction.DESC));
		Stream<IWorkSet> result = getItems(12L, orders, 0, 10);
//		result.forEach(e -> {
//			logger.debug(e.toString());
//		});
		assertThat(result).size().isEqualTo(5);
	}
}
