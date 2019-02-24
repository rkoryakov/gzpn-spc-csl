package ru.gzpn.spc.csl.model.enums;
public enum MessageType {
	FILL_SSR_CARD("fill_ssr"),
	SSR_APPROVAL("ssr_approval"),
	ESTIMATES_NOT_APPROVED("est_not_approved"),
	CREATE_CONTRACT_CARD("create_cont"),
	MAX_LOT_PRICE_CALCULATION("max_price_calc"),
	NONE("none");
	
	private String type;
	
	private MessageType(String type) {
		this.type = type;
	}
	
	public static MessageType getType(String type) {
		MessageType result = NONE;
		for (MessageType t : MessageType.values()) {
			if (t.type.equals(type)) {
				result = t;
				break;
			}
		}
		
		return result;
	}
}