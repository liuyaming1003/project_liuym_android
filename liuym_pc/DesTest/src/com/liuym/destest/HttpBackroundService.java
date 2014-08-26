package com.liuym.destest;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Log;

public class HttpBackroundService extends Service {
	private static final String TAG = "HttpSendService";
	public static final String ACTION = "com.liuym.destest.HttpBackroundService";
	private String url = null;
	private String hostAddr = null;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.e(TAG, " onBind ");		
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.e(TAG, " onCreate ");
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		Log.e(TAG, " 1onStart ");		
		super.onStart(intent, startId);
		SharedPreferences sharedPreferences = getSharedPreferences("httpUrl", Context.MODE_PRIVATE);
		//getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
		url = sharedPreferences.getString("url", "null");
		hostAddr = sharedPreferences.getString("hostAddr", "null");
		if(url == "null" || hostAddr == "null"){
			return ;
		}
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("后台调用 + url" + url);
				String result;
				if(url == null){
					return ;
				}
				try {
					result = HttpRequest.httpGetRequest(url, hostAddr);
					System.out.println("访问服务器返回数据 = " + result);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}, 0, 600000);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.e(TAG, " onStartCommand ");
		return super.onStartCommand(intent, flags, startId);
	}	
}
