package ru.gzpn.spc.csl.model.enums;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
	
	public static Role getRoleByTaskDefinition(String taskDefinition) {
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
