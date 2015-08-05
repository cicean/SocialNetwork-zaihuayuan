package com.gzqining.inthegarden.util;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public class JacksonMapper {
	private static ObjectMapper mapper ;  
	private JacksonMapper() { 
		if(mapper == null){
			mapper = new ObjectMapper();
			SerializationConfig serializationConfig = mapper.getSerializationConfig();
			//NULL will be ignore
//			serializationConfig = serializationConfig.withSerializationInclusion(Inclusion.NON_NULL);
			
			mapper.setSerializationConfig(serializationConfig);
			
			DeserializationConfig deserializationConfig = mapper.getDeserializationConfig();
			//unknow properties will allowed?
			//deserializationConfig = deserializationConfig.without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.setDeserializationConfig(deserializationConfig);
			
		}
	}
	private static class JacksonMapperHolder{
		public static final JacksonMapper instance = new JacksonMapper();		
	}
	public static ObjectMapper getInstance() {
		return JacksonMapperHolder.instance.getMapper();
	}
	public ObjectMapper getMapper(){
		return mapper;
	}
}
