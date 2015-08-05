package com.gzqining.inthegarden.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReqByHttp {

	public static void main(String[] args) {
		String url = "";
		//String mapMsg = sendUrlByPostAndReadByCharset(url, "output=json&location=22.61667,%20114.06667&key=37492c0ee6f924cb5e934fa08c6b1676", "utf-8");
		String mapMsg = ReqByHttp.sendUrlByPostAndReadByCharset(url, "flow_id=&accesstoken=&appid=", "utf-8");
		System.out.println("1111:"+mapMsg);
	
	}
	
	

	public static String sendUrlByPostAndReadByCharset(String urlStr, String content, String charSet) {
		URL url = null;
		HttpURLConnection connection = null;

		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();//�½�����ʵ��
			connection.setDoOutput(true);//�Ƿ������� true|false
			connection.setDoInput(true);//�Ƿ��������true|false
			connection.setRequestMethod("POST");//�ύ����POST|GET
			connection.setUseCaches(false);//�Ƿ񻺴�true|false
			connection.setConnectTimeout(6*1000);
			connection.setReadTimeout(5000*1000);
		   //log.info("messageLength: "+messageLength);
			connection.connect();//�����Ӷ˿�
		
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());//���������Զ˷�����д���
			out.writeBytes(content);//д���,Ҳ�����ύ��ı? name=xxx&pwd=xxx
			out.flush();//ˢ��
			out.close();//�ر����
	
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection
			.getInputStream(), charSet));//��Զ�д����� �Զ˷������������ ,��BufferedReader������ȡ
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
			   buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			return null;
		} finally {
			if (connection != null) {
			connection.disconnect();//�ر�����
			}
		}
	
	}
}
