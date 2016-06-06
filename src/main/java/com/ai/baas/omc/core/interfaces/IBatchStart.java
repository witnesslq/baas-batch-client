package com.ai.baas.omc.core.interfaces;


public interface IBatchStart {
	public String batchStart (String tenantId,String ower_type,String amount_type,String event_type) throws Exception;
}
