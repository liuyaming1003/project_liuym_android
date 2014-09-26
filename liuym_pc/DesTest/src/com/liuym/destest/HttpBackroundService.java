package com.liuym.destest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class HttpBackroundService extends Service {
	private static final String TAG = "HttpSendService";
	public static final String ACTION = "com.liuym.destest.HttpBackroundService";
	private String url = null;
	private String hostAddr = null;
	private String username = null;
	private static int sendIndex = 1; //sendIndex = 3 ���ϴ�һ�μ�¼��Ȼ������ļ�

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
		Log.e(TAG, " 1onStart " + startId);		
		super.onStart(intent, startId);
		SharedPreferences sharedPreferences = getSharedPreferences("httpUrl", Context.MODE_PRIVATE);
		//getString()�ڶ�������Ϊȱʡֵ�����preference�в����ڸ�key��������ȱʡֵ
		url = sharedPreferences.getString("url", "null");
		hostAddr = sharedPreferences.getString("hostAddr", "null");
		username = sharedPreferences.getString("username", "null");
		if(url.equals("null") || hostAddr.equals("null") || username.equals("null")){
			return ;
		}
 
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("��̨���� + url" + url);
				String result;
				if(url == null){
					return ;
				}
				try {
					result = HttpRequest.httpGetRequest(url, hostAddr);
					System.out.println("�û�:" + username + ",���ʷ������������� = " + result );

					String retCode = result.substring(0, 1);
					String postStr = null;
					if(retCode.equals("1")){
						postStr = "1: ��֤�ɹ�";
					}else{
						postStr = result;
					}


					SimpleDateFormat formatter = new SimpleDateFormat("  [yyyy-MM-dd HH:mm:ss]");     
					Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��     
					String timeStr = formatter.format(curDate);
					String filePath = addFileData(username, postStr + timeStr);
					if(filePath != null){
						if(sendIndex++ == 3){
							sendIndex = 1;
							result = HttpRequest.postFile("http://172.16.88.138/logtxt/post_Server.aspx", filePath, username);
							//System.out.print("---------------result = " + result);
							String resultMsg = result.substring(0, 8);
							if(resultMsg.equals("success!")){
								//ɾ���ļ�
								if(deleteNssyFile(username)){
									System.out.println("�ļ�ɾ���ɹ�");
								}else{
									System.out.println("�ļ�ɾ��ʧ��,�ļ�������");
								}
							}
						}
					}else{
						System.out.print("�ļ�����ʧ��");
					}
				} catch (ClientProtocolException e) {
					SimpleDateFormat formatter = new SimpleDateFormat("  [yyyy-MM-dd HH:mm:ss]");     
					Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��     
					String timeStr = formatter.format(curDate);
					addFileData(username, "������֤ʧ��(�Ǻ�̨����)  -- " + timeStr);
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					SimpleDateFormat formatter = new SimpleDateFormat("  [yyyy-MM-dd HH:mm:ss]");     
					Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��     
					String timeStr = formatter.format(curDate);
					addFileData(username, "������֤ʧ��(�Ǻ�̨����)  -- " + timeStr);
					e.printStackTrace();
				}

			}
		}, 0, 600000);
	}

	public boolean deleteNssyFile(String fileName){
		boolean deleteFileFlag = false;
		String sign_dir = Environment.getExternalStorageDirectory() + File.separator;  
		sign_dir = sign_dir + "nssy/";  
		File file = new File(sign_dir);
		//�ж��ļ����Ƿ����,����������򴴽��ļ���
		if (file.exists()) {
			sign_dir = sign_dir + fileName + ".txt";
			File dir = new File(sign_dir);  
			if (dir.exists()) {  
				//��ָ�����ļ����д����ļ�  
				dir.delete();    
				deleteFileFlag = true;
			}
		}

		return deleteFileFlag;
	}

	public String addFileData(String fileName, String data){
		String sign_dir = Environment.getExternalStorageDirectory() + File.separator;  
		sign_dir = sign_dir + "nssy/";  
		File file = new File(sign_dir);
		//�ж��ļ����Ƿ����,����������򴴽��ļ���
		if (!file.exists()) {
			file.mkdir();
		}

		sign_dir = sign_dir + fileName + ".txt";
		File dir = new File(sign_dir);  
		if (!dir.exists()) {  
			//��ָ�����ļ����д����ļ�  
			try {
				dir.createNewFile();
			} catch (IOException e) {
				sign_dir = null;
				e.printStackTrace();
			}    
		}

		/*try {  
			// ��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�  
			FileWriter writer = new FileWriter(fileName, true);  
			writer.write(data);  
			writer.close();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  */

		//FileInputStream fin;
		try {
			//fin = new FileInputStream(sign_dir);
			//int length = fin.available();

			FileOutputStream fout = new FileOutputStream(sign_dir, true);
			byte [] bytes = data.getBytes();
			fout.write(bytes);

			//д�뻻��
			String newline = System.getProperty("line.separator");
			fout.write(newline.getBytes());

			fout.close();
		} catch (FileNotFoundException e) {
			sign_dir = null;
			e.printStackTrace();
		} catch (IOException e) {
			sign_dir = null;
			e.printStackTrace();
		}
		return sign_dir;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.e(TAG, " onStartCommand ");
		return super.onStartCommand(intent, flags, startId);
	}	
}
