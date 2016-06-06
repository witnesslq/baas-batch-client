package com.ai.baas.omc.condition;

public class ConditionParam {
	private String tenantId;
	private String owerType;
	private String amountType;
	private String eventType;
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getOwerType() {
		return owerType;
	}

	public void setOwerType(String owerType) {
		this.owerType = owerType;
	}

	public String getAmountType() {
		return amountType;
	}

	public void setAmountType(String amountType) {
		this.amountType = amountType;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
}
