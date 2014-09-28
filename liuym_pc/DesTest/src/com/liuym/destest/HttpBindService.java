package com.liuym.destest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

public class HttpBindService extends Service {
	private String url = null;
	private String hostAddr = null;
	private String username = null;
	private static int sendIndex = 1; //sendIndex = 3 是上传一次记录，然后清掉文件
	private static final String TAG = "BindService";
	
	public void MyMethod(){
		SharedPreferences sharedPreferences = getSharedPreferences("httpUrl", Context.MODE_PRIVATE);
		//getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
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
				System.out.println("后台调用 + url" + url);
				String result;
				if(url == null){
					return ;
				}
				try {
					result = HttpRequest.httpGetRequest(url, hostAddr);
					System.out.println("用户:" + username + ",访问服务器返回数据 = " + result );

					String retCode = result.substring(0, 1);
					String postStr = null;
					if(retCode.equals("1")){
						postStr = "1: 认证成功";
					}else{
						postStr = result;
						final Timer timer1 = new Timer();
						timer1.schedule(new TimerTask() {
							@Override
							public void run() {
								String result;
								try {
									result = HttpRequest.httpGetRequest(url, hostAddr);
									String retCode = result.substring(0, 1);
									if(retCode.equals("1")){
										System.out.println("10分钟一次认证失败后，2.5秒后重新认证");
										timer1.cancel();
									}
								} catch (ClientProtocolException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}, 0, 2500);
					}


					SimpleDateFormat formatter = new SimpleDateFormat("  [yyyy-MM-dd HH:mm:ss]");     
					Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
					String timeStr = formatter.format(curDate);
					String filePath = addFileData(username, postStr + timeStr);
					if(filePath != null){
						if(sendIndex++ == 3){
							sendIndex = 1;
							result = HttpRequest.postFile("http://172.16.88.138/logtxt/post_Server.aspx", filePath, username);
							//System.out.print("---------------result = " + result);
							String resultMsg = result.substring(0, 8);
							if(resultMsg.equals("success!")){
								//删除文件
								if(deleteNssyFile(username)){
									System.out.println("文件删除成功");
								}else{
									System.out.println("文件删除失败,文件不存在");
								}
							}
						}
					}else{
						System.out.print("文件操作失败");
					}
				} catch (ClientProtocolException e) {
					SimpleDateFormat formatter = new SimpleDateFormat("  [yyyy-MM-dd HH:mm:ss]");     
					Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
					String timeStr = formatter.format(curDate);
					addFileData(username, "网络认证失败(非后台返回)  -- " + timeStr);
					e.printStackTrace();
				} catch (IOException e) {
					SimpleDateFormat formatter = new SimpleDateFormat("  [yyyy-MM-dd HH:mm:ss]");     
					Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
					String timeStr = formatter.format(curDate);
					addFileData(username, "网络认证失败(非后台返回)  -- " + timeStr);
					e.printStackTrace();
				}

			}
		}, 0, 600000);
    }
	
	@Override
	public IBinder onBind(Intent arg0) {
		return myBinder;
	}
	
	public class MyBinder extends Binder{
        
        public HttpBindService getService(){
            return HttpBindService.this;
        }
    }
	
	private MyBinder myBinder = new MyBinder();

	public boolean deleteNssyFile(String fileName){
		boolean deleteFileFlag = false;
		String sign_dir = Environment.getExternalStorageDirectory() + File.separator;  
		sign_dir = sign_dir + "nssy/";  
		File file = new File(sign_dir);
		//判断文件夹是否存在,如果不存在则创建文件夹
		if (file.exists()) {
			sign_dir = sign_dir + fileName + ".txt";
			File dir = new File(sign_dir);  
			if (dir.exists()) {  
				//在指定的文件夹中创建文件  
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
		//判断文件夹是否存在,如果不存在则创建文件夹
		if (!file.exists()) {
			file.mkdir();
		}

		sign_dir = sign_dir + fileName + ".txt";
		File dir = new File(sign_dir);  
		if (!dir.exists()) {  
			//在指定的文件夹中创建文件  
			try {
				dir.createNewFile();
			} catch (IOException e) {
				sign_dir = null;
				e.printStackTrace();
			}    
		}

		/*try {  
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件  
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

			//写入换行
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
	
}
