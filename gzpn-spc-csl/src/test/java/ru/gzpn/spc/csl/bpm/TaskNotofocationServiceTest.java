package ru.gzpn.spc.csl.bpm;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ru.gzpn.spc.csl.services.bpm.ITaskNotificationService;
import ru.gzpn.spc.csl.services.bpm.TaskNotificationService;


//@RunWith(SpringRunner.class)
//@SpringBootTest(classes=Application.class)
public class TaskNotofocationServiceTest extends TaskNotificationService {
	public static final String REST_TEMPLATE = 
			"http://sapdp7.gazprom-neft.local:50000/NDI_EPCOMMON_D~gzpn~mto~services~rs~gazprom-neft.ru/utils/documents/{to}/{type}/{taskId}/{attributes}";
	@Autowired
	ITaskNotificationService service; 
	

	
//	@Test
//	public void sendMessageTest() throws AddressException {
//		sendMessage(MessageType.CREATE_CONTRACT_CARD, new InternetAddress("koryakovrv@it-sk.ru"), "13234", " ");
//	}
//	
//	@Test
//	public void sendMessageByNotificationServiceTest() throws AddressException, UnsupportedEncodingException {
//		service.sendMessage(MessageType.CREATE_CONTRACT_CARD, new InternetAddress("koryakovrv@it-sk.ru"), "13234", URLEncoder.encode("ывавыфа выфавыафыва", "utf-8"));
//	}
	
	
//	@Override
//	public void sendMessage(MessageType type, InternetAddress to, String taskId,  String attributes) {
//		RestTemplate restTemplate = new RestTemplate();
//		StringBuilder url = new StringBuilder();
//		url.append(to.getAddress()).append('/');
//		url.append(type).append('/');
//		url.append(attributes);
//		restTemplate.execute(REST_TEMPLATE, HttpMethod.GET, null, null, to.getAddress(), type, taskId, attributes);
//	}
	
	
	@Test
	public void decodeUrlTest() throws UnsupportedEncodingException {
		String str = "%D0%9F%D0%BE%D0%B4%D0%BE%D1%80%D0%B2%D0%B0%D0%BB%D1%81%D1%8F+%D0%B8+%D1%81%D0%B4%D0%B5%D0%BB%D0%B0%D0%BB%21%21";
		assertThat(URLDecoder.decode(str, "utf-8")).contains("и");
	}
}
