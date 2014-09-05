package com.liuym.soap;

import java.io.IOException;
import java.util.ArrayList;
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
	private ArrayList<Object> arrayList = null;
	private SoapInterface soapInterface = null;

	public interface SoapInterface{
		/**
		 * 调用soap接口返回接口，只有一个参数调用
		 * @param object object为接口返回数据
		 */
		//public void soapResult(Object object);
		
		/**
		 * 调用soap接口返回接口,可以为多个参数
		 * @param arrayList arrayList元素为Object对象
		 */
		
		public void soapResult(ArrayList<Object> arrayList);
		
		/**
		 * 调用soap接口错误信息
		 * @param error 错误信息
		 */
		public void soapError(String error);
	}
	
	private Soap(){
		map = new HashMap<String, Object>();
		arrayList = new ArrayList<Object>();
	}

	/**
	 * 返回Sopa实例对象
	 * @return
	 */
	public static Soap getSoap(){
		if(soap == null){
			soap = new Soap();
		}
		return soap;
	}
	public String getNamespace() {
		return namespace;
	}

	/**
	 * 设置soap访问的命名空间，如果服务端不变，只需要设置一次即可
	 * @param namespace
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getHostUrl() {
		return hostUrl;
	}

	/**
	 * 设置sopa访问的服务地址，如果服务端不变，只需要设置一次即可
	 * @param hostUrl
	 */
	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	/**
	 * 设置soap接口参数
	 * @param key
	 * @param value
	 */
	public void putSoapParam(String key, Object value){
		map.put(key, value);
	}

	/**
	 * 调用soap接口
	 * @param methodName soap接口方法名
	 * @param timeout 超时时间
	 * @param soapInterface 设置soap返回回调
	 */
	public void soapRequest(String methodName, int timeout, SoapInterface soapInterface){
		this.methodName = methodName;
		this.timeout = timeout;
		this.soapInterface = soapInterface;
		if(namespace == null || hostUrl == null){
			soapInterface.soapError("请先设置soap服务命名空间或者soap服务地址");
			return ;
		}	
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
		arrayList.clear();

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
					for(int i = 0; i < object.getPropertyCount(); i++){
						arrayList.add(object.getProperty(0));
					}
					soapInterface.soapResult(arrayList);
				} catch (IOException e) {
					soapInterface.soapError(e.getMessage());
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					soapInterface.soapError(e.getMessage());					
					e.printStackTrace();
				}				
			}
		}).start();
	}
}