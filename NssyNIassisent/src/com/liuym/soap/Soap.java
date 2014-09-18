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

import android.os.Handler;
import android.os.Message;

public class Soap{
	//static Soap soap = null;
	public static String namespace = null;	
	//private String methodName = null;
	public static String hostUrl = null;
	//private int timeout;
	//private Map<String, Object> map = null;
	//private ArrayList<Object> arrayList = null;
	private SoapInterface soapInterface = null;
	private Handler handler = null;

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
	
	public Soap(){
		//map = new HashMap<String, Object>();
		//arrayList = new ArrayList<Object>();
		
		handler = new Handler(){
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				if(soapInterface == null){
					return;
				}
				switch(msg.what){
				case 0:
					if(msg.obj instanceof ArrayList){
						soapInterface.soapResult((ArrayList<Object>)msg.obj);
					}
					break;
				case 1:
					soapInterface.soapError((String)msg.obj);
					break;
				}
				super.handleMessage(msg);
			}
		};
	}

	/**
	 * ����soap�ӿ�
	 * @param methodName soap�ӿڷ�����
	 * @param timeout ��ʱʱ��
	 * @param soapInterface ����soap���ػص�
	 */
	public void soapRequest(String methodName, int timeout, final SoapInterface soapInterface, Map<String, Object> map){
		//this.methodName = methodName;
		//this.timeout = timeout;
		this.soapInterface = soapInterface;
		if(namespace == null || hostUrl == null){
			soapInterface.soapError("��������soap���������ռ����soap�����ַ");
			return ;
		}
		
		soapHttp(namespace, methodName, hostUrl, timeout, map, handler);
	}


	@SuppressWarnings("rawtypes")
	private void soapHttp(final String namespace, final String methodName, String hostUrl, int timeout, 
			Map<String, Object> map, final Handler handler){
		//ʵ����SoapObject����
		SoapObject request=new SoapObject(namespace, methodName);
		Iterator iter = map.entrySet().iterator(); 
		while (iter.hasNext()) { 
			Map.Entry entry = (Map.Entry) iter.next(); 
			String key = entry.getKey().toString(); 
			Object value = entry.getValue(); 
			request.addProperty(key, value);
		}

		//������л���Envelope
		final SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut=request;
		envelope.dotNet = true;
		(new MarshalBase64()).register(envelope);

		final HttpTransportSE transport=new HttpTransportSE(hostUrl, timeout);
		transport.debug=true;

		new Thread(new Runnable() {		
			@Override
			public void run() {
				 Message message = new Message();
				try {
					transport.call(namespace+methodName, envelope);
					// ��ȡ���ص�����  
					SoapObject object = (SoapObject) envelope.bodyIn;  
					// ��ȡ���صĽ��  
					ArrayList<Object> arrayList = new ArrayList<Object>();
					for(int i = 0; i < object.getPropertyCount(); i++){
						arrayList.add(object.getProperty(0));
					}
					message.what = 0;
					message.obj = arrayList;
					handler.sendMessage(message);
				} catch (IOException e) {
					message.what = 1;
					message.obj = e.getMessage();
					handler.sendMessage(message);
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					message.what = 1;
					message.obj = e.getMessage();
					handler.sendMessage(message);					
					e.printStackTrace();
				}				
			}
		}).start();
	}
}