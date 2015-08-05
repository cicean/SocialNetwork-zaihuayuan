package com.gzqining.inthegarden.util;





import com.google.gson.Gson;

/**
 *
 *
 */
public class ReqJsonUtil {
	
	
	/**
	 * 
	 * @param request
	 * @param cla
	 * @return
	 */
	public static Object changeToObject(String jsonContent, Class cla) {
		Object object = null;
		try {
			
			System.out.println("jsonContent:"+jsonContent);
			Gson gson = new Gson();
			object = gson.fromJson(jsonContent, cla);//gson解析json
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
}
