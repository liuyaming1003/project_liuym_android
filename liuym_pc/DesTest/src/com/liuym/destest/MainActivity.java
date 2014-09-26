package com.liuym.destest;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.net.util.Base64;
import org.apache.http.client.ClientProtocolException;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	private EditText username;
	private EditText password;
	//private EditText deskey;
	//private EditText hostip;
	private Button logon;
	private String wifiIp;
	private TextView resultView;
	private Handler handler;
	private String httpUrl;
	private boolean isSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //MyDes.shareMyDes().decDes("+mOPaXsh2YjBBm1or9HFRRSc/ZAKjWf+Gki98i2+D6dtoLSmCHiuww==");
        
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        //deskey = (EditText)findViewById(R.id.deskey);
        //hostip = (EditText)findViewById(R.id.hostip);
        resultView = (TextView)findViewById(R.id.result);
        
        wifiIp = getWifiAddress();
        
        handler = new Handler(){
      	   @Override
             public void handleMessage(Message msg) { 
          	   if(msg.what == 0)//���ؽ�����
          	   {
          		   logon.setEnabled(true);
          		   setLogText(msg.obj.toString());
          	   }
             } 
         };
        
        logon = (Button)findViewById(R.id.logon);
        logon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				resultView.setText("");
				String usernameText = username.getText().toString();
				String passwordText = password.getText().toString();
				String deskeyText = "nssy2014";//deskey.getText().toString();
				final String hostipText = "172.16.88.64";//hostip.getText().toString();
				if(!usernameText.equals("") && !passwordText.equals("")){
					if(MyDes.shareMyDes().setDesKey(deskeyText) == true){
						try {
							String data = MyDes.shareMyDes().encDes("username="+ new String(Base64.encodeBase64(usernameText.getBytes())) +",password="+ new String(Base64.encodeBase64(passwordText.getBytes())));
							final String netUrl = "http://"+ hostipText +"/portal/entrance/http_index.jsp?userinfo=" + URLEncoder.encode(data, "utf-8") + "&userip="+ wifiIp +"&userPublicIp="+ wifiIp +"&language=Chinese";
							setLogText("url��ַ" + netUrl);
							httpUrl = netUrl;
							logon.setEnabled(false);
							new Thread(){
								Message msg = new Message();
								public void run(){
									String result = null;									
									msg.what = 0;
									try {
										result = HttpRequest.httpGetRequest(netUrl, hostipText);
										msg.obj = "���ʷ������������� = " + result;
										String retCode = result.substring(0, 1);
										if(retCode.equals("1")){
											isSuccess = true;
											msg.obj = "���ʷ������������� = " + "��֤�ɹ�";
										}else{
											isSuccess = false;
										}
										handler.sendMessage(msg);
									} catch (ClientProtocolException e) {
										msg.obj = "Client ����" + e.getMessage();
										handler.sendMessage(msg);
										e.printStackTrace();
									} catch (IOException e) {
										msg.obj = "IO ����" + e.getMessage();
										handler.sendMessage(msg);
										e.printStackTrace();
									}
								}
							}.start();							
						} catch (Exception e) {
							setLogText("����" + e.getMessage());
							e.printStackTrace();
						}
					}else{
						
					}
				}else{
					Toast.makeText(MainActivity.this, "�û���������Ϊ��", Toast.LENGTH_SHORT).show();
				}
			}
		});
        
        
       /* String data = null;
		try {
			data = MyDes.shareMyDes().encDes("username="+ Base64.encodeBase64URLSafeString("meiyt".getBytes()) +",password="+ Base64.encodeBase64URLSafeString("123456".getBytes()));
			MyDes.shareMyDes().decDes(data);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String netUrl = null;
        try {
			netUrl = "http://172.16.88.64/portal/entrance/http_index.jsp?userinfo=" + URLEncoder.encode(data, "utf-8") + "&userip=192.168.72.27&userPublicIp=192.168.72.27&language=Chinese";
			System.out.println("result = " + netUrl);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        String result = null;
		try {
			result = HttpRequest.httpGetRequest(netUrl);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("result = " + result);*/
        /*
        
        String decData = MyDes.shareMyDes().decDes(url);
        System.out.println("decData = " + decData);*/
    }
    
    private void setLogText(String text){
    	resultView.setText(resultView.getText() + "\n" + text);
    }

    private String getWifiAddress(){
    	 //��ȡwifi����  
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
        //�ж�wifi�Ƿ���  
        if (!wifiManager.isWifiEnabled()) {  
        wifiManager.setWifiEnabled(true);    
        }  
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
        int ipAddress = wifiInfo.getIpAddress();   
        String ip = intToIp(ipAddress);  
        return ip;
    }

    private String intToIp(int i) {
    	return (i & 0xFF ) + "." +       
    			((i >> 8 ) & 0xFF) + "." +       
    			((i >> 16 ) & 0xFF) + "." +       
    			( i >> 24 & 0xFF) ;  
    } 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    
    private void httpSendService(){
    	Intent intent = new Intent(HttpBackroundService.ACTION);
    	saveUrl(httpUrl);
    	startService(intent);
    }
    
    private void saveUrl(String url){
		SharedPreferences sharedPreferences = getSharedPreferences("httpUrl", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("url", url);
		editor.putString("hostAddr", /*hostip.getText().toString()*/"172.16.88.64");
		editor.putString("username", username.getText().toString());
		//��������
		editor.commit();
	}
    
    @Override  
    protected void onStart() {  
        super.onStart();  
        System.out.println("onStart");  
    }  
    @Override  
    protected void onResume() {  
        super.onResume();  
        System.out.println("onResume");  
    }  
    @Override  
    protected void onPause() {  
        super.onPause();  
        if(isSuccess == true){
        	httpSendService();
        }
        System.out.println("onPause");  
    }  
    @Override  
    protected void onStop() {  
        super.onStop();  
        System.out.println("onStop");  
    }  
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        System.out.println("onDestroy");  
    }  
}
