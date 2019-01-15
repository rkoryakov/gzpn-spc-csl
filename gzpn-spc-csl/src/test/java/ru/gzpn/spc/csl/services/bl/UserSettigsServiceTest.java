package ru.gzpn.spc.csl.services.bl;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserSettigsServiceTest {

	@Autowired
	UserSettigsService settigsService;
	
	@Test
	public void testJson() {
		CreateDocSettingsJson settings = settigsService.getUserSettings();
		NodeWrapper nodeSettings = settings.getLeftDefaultNodesHierarchy();
		
		ColumnHeaderGroup head1 = new ColumnHeaderGroup("root1");
		ColumnHeaderGroup children1 = new ColumnHeaderGroup("child1");
		ColumnHeaderGroup children2 = new ColumnHeaderGroup("child2");
		ColumnHeaderGroup children3 = new ColumnHeaderGroup("child3");
		ColumnHeaderGroup head2 = new ColumnHeaderGroup("root2");
		head1.setChildren(Arrays.asList(children1, children2, children3));
		head2.setChildren(Arrays.asList(children1, children2, children3));
		settings.setLeftColumnHeaders(Arrays.asList(head1, head2));
		
		
		ObjectMapper mapper = new ObjectMapper();
		final StringWriter w = new StringWriter();
		try {
			mapper.writeValue(w, settings);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
	}
}
