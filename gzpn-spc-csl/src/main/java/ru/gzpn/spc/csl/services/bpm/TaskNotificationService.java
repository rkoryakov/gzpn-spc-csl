package ru.gzpn.spc.csl.services.bpm;

import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ru.gzpn.spc.csl.model.enums.MessageType;

@Service
public class TaskNotificationService implements ITaskNotificationService {
	public static final Logger logger = LoggerFactory.getLogger(TaskNotificationService.class);
	
	public static final String REST_TEMPLATE = 
			"http://sapdp7.gazprom-neft.local:50000/NDI_EPCOMMON_D~gzpn~mto~services~rs~gazprom-neft.ru/utils/documents/{to}/{type}/{taskId}/{attributes}";
	
	@Override
	public void sendMessage(MessageType type, InternetAddress to, String taskId,  String attributes) {
		RestTemplate restTemplate = new RestTemplate();
		StringBuilder url = new StringBuilder();
		url.append(to.getAddress()).append('/');
		url.append(type).append('/');
		url.append(attributes);
		logger.debug("[TaskNotificationService]  " + REST_TEMPLATE);
		logger.debug("[TaskNotificationService]  " + to);
		logger.debug("[TaskNotificationService]  " + type);
		logger.debug("[TaskNotificationService]  " + taskId);
		logger.debug("[TaskNotificationService]  " + attributes);
		
        restTemplate.execute(REST_TEMPLATE, HttpMethod.GET, null, null, to.getAddress(), type, taskId, attributes);
	}
}
