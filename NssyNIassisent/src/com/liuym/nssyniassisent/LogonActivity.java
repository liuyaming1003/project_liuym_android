package com.liuym.nssyniassisent;

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
				/*final Soap soap = Soap.getSoap();
				soap.setHostUrl("http://systeminfo.nssy.com.cn/di.asmx");
				soap.setNamespace("http://tempuri.org/");
				soap.putSoapParam("userName", username.getText().toString());
				soap.putSoapParam("password", password.getText().toString());
				soap.soapRequest("impersonateValidUser", 10000, new SoapInterface() {					
					@Override
					public void soapResult(boolean flag, String data) {
						System.out.println("impersonateValidUser = flag:" + flag + " data:" + data);
						soap.putSoapParam("userName", username.getText().toString());
						soap.soapRequest("Power_Judge", 10000, new SoapInterface() {					
							@Override
							public void soapResult(boolean flag, String data) {
								System.out.println("Power_Judge = flag:" + flag + " data:" + data);
							}
						});
					}
				});*/
				
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
			if ((System.currentTimeMillis() - mExitTime) > 2000) {// ������ΰ���ʱ��������2000���룬���˳�
				Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();// ����mExitTime
			} else {
				System.exit(0);// �����˳�����
			}
			return true;   
		}
		return super.onKeyDown(keyCode, event);
	}
}
