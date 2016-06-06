package com.ai.baas.omc.core;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.baas.omc.constant.OmcCalKey;
import com.ai.baas.omc.core.interfaces.IBatchStart;
import com.ai.baas.omc.dao.interfaces.BlUserinfoMapper;
import com.ai.baas.omc.dao.mapper.bo.BlUserinfo;
import com.ai.baas.omc.dao.mapper.bo.BlUserinfoCriteria;
import com.ai.opt.sdk.components.mds.MDSClientFactory;
import com.ai.paas.ipaas.mds.IMessageSender;
import com.alibaba.fastjson.JSONObject;

@Component("batchStart")
public class BatchStart implements IBatchStart{
	private static final Logger log = LogManager.getLogger(BatchStart.class);
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	
	
	public String batchStart (String tenant_id,String ower_type,String amount_type,String event_type) throws Exception{
	       BlUserinfo blUserinfo = new BlUserinfo();
	       JSONObject jsonObject = new JSONObject();
	       BlUserinfoMapper blUserinfoMapper = sqlSessionTemplate.getMapper(BlUserinfoMapper.class);
	       //增加查询条件tenant_id
	       BlUserinfoCriteria blUserinfoCriteria = new BlUserinfoCriteria();
	       BlUserinfoCriteria.Criteria criteria = blUserinfoCriteria.createCriteria();
	       criteria.andTenantIdEqualTo(tenant_id);
//	       List<BlUserinfo> userInfoListAmount =blUserinfoMapper.selectByExample(blUserinfoCriteria);
//	       int amount=userInfoListAmount.size();
	       String mdsns = OmcCalKey.BASS_OMC_TOPIC;//
	       IMessageSender msgSender = MDSClientFactory.getSenderClient(mdsns);
	       int i = 0;
           int j=0;
	       while(true){
	           
	           blUserinfoCriteria.setLimitStart(i);
	           blUserinfoCriteria.setLimitEnd(50);
	           List<BlUserinfo> userInfoList =blUserinfoMapper.selectByExample(blUserinfoCriteria); 
	           log.info("-------------->>>:"+userInfoList.size());
	           if(userInfoList.size()>0){
	               for(BlUserinfo r : userInfoList){
	            	   jsonObject.put(OmcCalKey.OMC_OWNER_ID, r.getSubsId());
	            	   jsonObject.put(OmcCalKey.OMC_TENANT_ID, tenant_id);
	            	   jsonObject.put(OmcCalKey.OMC_AMOUNT, "0");//0
	                   //acct:账户；cust:客户；subs:用户
	                   jsonObject.put(OmcCalKey.OMC_OWNER_TYPE, ower_type);
	                   jsonObject.put(OmcCalKey.OMC_AMOUNT_TYPE, amount_type);
	                   //事件类型：CASH主业务（按资料信控），VOICE 语音，SMS 短信，DATA 数据
	                   jsonObject.put(OmcCalKey.OMC_EVENT_TYPE, event_type);//DATA
	                   //数量的增减属性，包括PLUS(导致余额增加的，如缴费导致的)，MINUS(导致余额减少的，如业务使用导致的)
	                   jsonObject.put(OmcCalKey.OMC_AMOUNT_MARK, "PLUS");//PLUS
	                   //来源 resource：资源入账，bmc：计费
	                   jsonObject.put(OmcCalKey.OMC_SOURCE_TYPE, "bmc");
	                 //系统id
	                   jsonObject.put(OmcCalKey.OMC_SYSTEM_ID, "VIV");
	                   //事件id
	                   jsonObject.put(OmcCalKey.OMC_EVENT_ID, Long.toString(System.currentTimeMillis()));
	                   //扩展信息，用json传递具体信息
	                   jsonObject.put(OmcCalKey.OMC_EXPANDED_INFO, "");
	                   /*
	                    *  传到mds
	                    */ 


	                   int part=j%2;
	                   j=++j;
	                   msgSender.send(jsonObject.toString(),part);
	               }
	           }
	           //判断是否读取完整张表 break
	           if(userInfoList.size() < 49){
	               int num=i+userInfoList.size();
	               log.info("总共传入"+num+"条用户信息");
	               break;
	           }
	           i = i + 50;
//	           if(amount<i||amount==i){
//	        	   break;
//	           }
	           
	       }	       
	       
	       return null;
	}
	
	

}
