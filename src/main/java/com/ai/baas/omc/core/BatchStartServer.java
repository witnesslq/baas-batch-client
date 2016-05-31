package com.ai.baas.omc.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ai.baas.omc.condition.ConditionParam;
import com.ai.baas.omc.core.interfaces.IBatchStart;

public class BatchStartServer {
	private static final Logger log = LogManager.getLogger(BatchStartServer.class);
	//
	private static final String[] PATH = { "classpath:context/core-context.xml" };
	
	private static ClassPathXmlApplicationContext context;
	
	
	//
	public static void main(String[] args){
	       // 启动spring容器
	       log.info("启动spring容器,配置文件路径为"+PATH[0]);
	       System.out.println("启动spring容器,配置文件路径为"+PATH[0]);
	       context = new ClassPathXmlApplicationContext(PATH);
	       context.registerShutdownHook();
	       context.start();
	       ConditionParam conditionParam = (ConditionParam)context.getBean("conditionParam");
	       String tenant_id = conditionParam.getTenantId();
	       IBatchStart batchstart =(IBatchStart)context.getBean("batchStart");
	       //
	       try {
	    	
			batchstart.batchStart(tenant_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	       System.out.println("finish");
	       }
}
