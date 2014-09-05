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
		 * ����soap�ӿڷ��ؽӿڣ�ֻ��һ����������
		 * @param object objectΪ�ӿڷ�������
		 */
		//public void soapResult(Object object);
		
		/**
		 * ����soap�ӿڷ��ؽӿ�,����Ϊ�������
		 * @param arrayList arrayListԪ��ΪObject����
		 */
		
		public void soapResult(ArrayList<Object> arrayList);
		
		/**
		 * ����soap�ӿڴ�����Ϣ
		 * @param error ������Ϣ
		 */
		public void soapError(String error);
	}
	
	private Soap(){
		map = new HashMap<String, Object>();
		arrayList = new ArrayList<Object>();
	}

	/**
	 * ����Sopaʵ������
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
	 * ����soap���ʵ������ռ䣬�������˲��䣬ֻ��Ҫ����һ�μ���
	 * @param namespace
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getHostUrl() {
		return hostUrl;
	}

	/**
	 * ����sopa���ʵķ����ַ���������˲��䣬ֻ��Ҫ����һ�μ���
	 * @param hostUrl
	 */
	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	/**
	 * ����soap�ӿڲ���
	 * @param key
	 * @param value
	 */
	public void putSoapParam(String key, Object value){
		map.put(key, value);
	}

	/**
	 * ����soap�ӿ�
	 * @param methodName soap�ӿڷ�����
	 * @param timeout ��ʱʱ��
	 * @param soapInterface ����soap���ػص�
	 */
	public void soapRequest(String methodName, int timeout, SoapInterface soapInterface){
		this.methodName = methodName;
		this.timeout = timeout;
		this.soapInterface = soapInterface;
		if(namespace == null || hostUrl == null){
			soapInterface.soapError("��������soap���������ռ����soap�����ַ");
			return ;
		}	
		soapHttp();
	}


	@SuppressWarnings("rawtypes")
	private void soapHttp(){
		//ʵ����SoapObject����
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

		//������л���Envelope
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
					// ��ȡ���ص�����  
					SoapObject object = (SoapObject) envelope.bodyIn;  
					// ��ȡ���صĽ��  
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