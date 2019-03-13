package ru.gzpn.spc.csl.model.enums;
public enum MessageType {
	FILL_SSR_CARD("fill_ssr", "EstimatesRegistration"),
	SSR_APPROVAL("ssr_approval", "EstimatesApproval"),
	ESTIMATES_NOT_APPROVED("est_not_approved", "EstimatesRegistration"),
	CREATE_CONTRACT_CARD("create_cont", "CreateContract"),
	MAX_LOT_PRICE_CALCULATION("max_price_calc", "CalculateMaxLotPrice"),
	NONE("none", "none");
	
	private String type;
	private String taskDefinition;
	
	private MessageType(String type, String taskDefifnition) {
		this.type = type;
		this.taskDefinition = taskDefifnition;
	}
	
	public static MessageType getMessageType(String type) {
		MessageType result = NONE;
		for (MessageType t : MessageType.values()) {
			if (t.type.equals(type)) {
				result = t;
				break;
			}
		}
		
		return result;
	}
	
	public static MessageType getMessageTypeByDefinition(String definition) {
		MessageType result = NONE;
		for (MessageType t : MessageType.values()) {
			if (t.taskDefinition.equals(definition)) {
				result = t;
				break;
			}
		}
		
		return result;
	}
	
	public String getTaskDefinition() {
		return this.taskDefinition;
	}
	
	@Override
	public String toString() {
		return type;
	}
}