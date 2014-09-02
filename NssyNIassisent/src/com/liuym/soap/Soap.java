package com.liuym.soap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class Soap{
	static Soap soap = null;
	private String namespace = null;	
	private String methodName = null;
	private String hostUrl = null;
	private int timeout;
	private Map<String, Object> map = null;
	private SoapInterface soapInterface = null;
	
	public interface SoapInterface{
		public void soapResult(boolean flag, String data);
	}
	
	private Soap(){
		map = new HashMap<String, Object>();
	}

	public static Soap getSoap(){
		if(soap == null){
			soap = new Soap();
		}
		return soap;
	}
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getHostUrl() {
		return hostUrl;
	}

	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void putSoapParam(String key, Object value){
		map.put(key, value);
	}
	
	public void soapRequest(String methodName, int timeout, SoapInterface soapInterface){
		this.methodName = methodName;
		this.timeout = timeout;
		this.soapInterface = soapInterface;
		soapHttp();
	}


	@SuppressWarnings("rawtypes")
	private void soapHttp(){
		//实例化SoapObject对象
		SoapObject request=new SoapObject(namespace, methodName);
		Iterator iter = map.entrySet().iterator(); 
		while (iter.hasNext()) { 
			Map.Entry entry = (Map.Entry) iter.next(); 
			String key = entry.getKey().toString(); 
			Object value = entry.getValue(); 
			request.addProperty(key, value);
		}
		map.clear();

		//获得序列化的Envelope
		final SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER12);
		envelope.bodyOut=request;
		(new MarshalBase64()).register(envelope);

		final HttpTransportSE transport=new HttpTransportSE(hostUrl, timeout);
		transport.debug=true;

		new Thread(new Runnable() {			
			@Override
			public void run() {
				try {
					transport.call(namespace+methodName, envelope);
					// 获取返回的数据  
					SoapObject object = (SoapObject) envelope.bodyIn;  
					// 获取返回的结果  
					String result = object.getProperty(0).toString(); 
					soapInterface.soapResult(true, result);
					//System.out.println("result:" + result);
				} catch (IOException e) {
					soapInterface.soapResult(false, e.getMessage());
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					soapInterface.soapResult(false, e.getMessage());					
					e.printStackTrace();
				}				
			}
		}).start();
	}
}