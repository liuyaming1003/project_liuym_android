package com.liuym.nssyniassisent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.liuym.nssyniassisent.R;
import com.liuym.soap.Soap;
import com.liuym.soap.Soap.SoapInterface;
import com.liuym.teacher.TeacherActivity;
import com.liuym.worker.WorkerActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogonActivity extends MainActivity {
	private EditText username = null;
	private EditText password = null;
	private Map<String, Object> map = null;
	private long mExitTime;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_logon);

		map=new HashMap<String, Object>();

		findViewById(R.id.Logon).setOnClickListener(new Button.OnClickListener() {			
			@Override
			public void onClick(View v) {					  
				map.put("username", username.getText().toString());
				map.put("password", password.getText().toString());
				final Soap soap = Soap.getSoap();
				soap.setHostUrl("http://systeminfo.nssy.com.cn/di.asmx");
				soap.setNamespace("http://tempuri.org/");
				soap.putSoapParam("userName", username.getText().toString());
				soap.putSoapParam("password", password.getText().toString());
				soap.soapRequest("impersonateValidUser", 10000, new SoapInterface() {		
					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						Object object = arrayList.get(0);
						if(object instanceof Boolean){
							boolean logon_flag = ((Boolean) object).booleanValue();
							if(logon_flag == true){
								System.out.println("登入成功");
							}else{
								System.out.println("登入失败");
							}
						}

						soap.putSoapParam("userName", username.getText().toString());
						soap.soapRequest("Power_Judge", 10000, new SoapInterface() {	
							@Override
							public void soapResult(ArrayList<Object> arrayList) {
								// TODO Auto-generated method stub
								Object object = arrayList.get(0);
								System.out.println("Power_Judge result = " + object);
							}

							@Override
							public void soapError(String error) {
								// TODO Auto-generated method stub

							}
						});
					}

					@Override
					public void soapError(String error) {
						System.out.println("soap error = " + error);
					}
				});

				if(username.getText().toString().equals("1")){
					push(TeacherActivity.class, "LogonActivity", map);
				}else{
					push(WorkerActivity.class, "LogonActivity", map);
				}
			}
		});

		username = (EditText)findViewById(R.id.username);
		password = (EditText)findViewById(R.id.password);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){  
			if ((System.currentTimeMillis() - mExitTime) > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();// 更新mExitTime
			} else {
				System.exit(0);// 否则退出程序
			}
			return true;   
		}
		return super.onKeyDown(keyCode, event);
	}
}
