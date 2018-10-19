package ru.gzpn.spc.csl.services.bl;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.interfaces.IUserSettingsRepository;

@Service
@Transactional
public class DataUserSettigsService {
	public static final Logger logger = LoggerFactory.getLogger(DataUserSettigsService.class);
	@Autowired
	private IUserSettingsRepository repository;
	
	public LinkedHashMap<String, List<String>> getNodesOrder() {
		
		List<String> hproject = Arrays.asList("HProject", "projectId", "name");
		List<String> cproject = Arrays.asList("CProject", "name");
		List<String> phase = Arrays.asList("Phase");
		Stream.of(hproject, cproject, phase).collect(Collectors.toMap(obj->obj.get(0), obj->obj.subList(1, obj.size())));
		return Stream.of(hproject, cproject, phase).collect(Collectors.toMap(obj->obj.get(0), obj->obj.subList(1, obj.size()), (k, o)->k, LinkedHashMap::new));
	}
}
