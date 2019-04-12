package ru.gzpn.spc.csl.model.enums;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public enum Role {
	ADMIN_ROLE, 
	USER_ROLE, 
	CREATOR_ROLE("CreateDocument"), 
	EXPERT_ES_ROLE("EstimatesRegistration"), 
	CONTRACT_ES_ROLE("CalculateMaxLotPrice"),
	APPROVER_NTC_ROLE("EstimatesApproval"),
	CONTRACT_EX_ROLE("CreateContract"),
	NONE;
	
	private Set<String> taskDefinitionSet;
	
	private Role(Collection<String> taskDefinitions) {
		this.taskDefinitionSet = new HashSet<>(taskDefinitions);
	}
	
	private Role(String ...taskDefinitions) {
		this.taskDefinitionSet = new HashSet<>(Arrays.asList(taskDefinitions));
	}
	
	public Set<String> getDefinitions() {
		return taskDefinitionSet;
	}
	
	/**
	 * Checks whether the current user has the given role
	 * @param role
	 * @return
	 */
	public static final boolean hasRole(Role role) {
		boolean result = false;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Set<String> authorities = authentication.getAuthorities().stream()
										.map(a -> a.getAuthority())
											.collect(Collectors.toSet());
			result = authorities.contains(role.name());
		}
		
		return result;
	}
	
	public static final Role getRoleByTaskDefinition(String taskDefinition) {
		Role result = Role.NONE;
		for (Role role : Role.values()) {
			if (role.taskDefinitionSet.contains(taskDefinition)) {
				result = role;
				break;
			}
		}
		
		return result;
	}
}
