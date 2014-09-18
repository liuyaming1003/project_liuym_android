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
	 * 调用soap接口
	 * @param methodName soap接口方法名
	 * @param timeout 超时时间
	 * @param soapInterface 设置soap返回回调
	 */
	public void soapRequest(String methodName, int timeout, final SoapInterface soapInterface, Map<String, Object> map){
		//this.methodName = methodName;
		//this.timeout = timeout;
		this.soapInterface = soapInterface;
		if(namespace == null || hostUrl == null){
			soapInterface.soapError("请先设置soap服务命名空间或者soap服务地址");
			return ;
		}
		
		soapHttp(namespace, methodName, hostUrl, timeout, map, handler);
	}


	@SuppressWarnings("rawtypes")
	private void soapHttp(final String namespace, final String methodName, String hostUrl, int timeout, 
			Map<String, Object> map, final Handler handler){
		//实例化SoapObject对象
		SoapObject request=new SoapObject(namespace, methodName);
		Iterator iter = map.entrySet().iterator(); 
		while (iter.hasNext()) { 
			Map.Entry entry = (Map.Entry) iter.next(); 
			String key = entry.getKey().toString(); 
			Object value = entry.getValue(); 
			request.addProperty(key, value);
		}

		//获得序列化的Envelope
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
					// 获取返回的数据  
					SoapObject object = (SoapObject) envelope.bodyIn;  
					// 获取返回的结果  
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