package com.liuym.nssyniassisent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.liuym.nssyniassisent.R;
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
		username = (EditText)findViewById(R.id.username);
		password = (EditText)findViewById(R.id.password);

		findViewById(R.id.Logon).setOnClickListener(new Button.OnClickListener() {			
			@Override
			public void onClick(View v) {					  
				map.put("username", username.getText().toString());
				map.put("password", password.getText().toString());
				nssySoap.impersonateValidUser(username.getText().toString(), password.getText().toString(), 10000, new SoapInterface() {		
					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						Object object = arrayList.get(0);
						String logon_info = object.toString();
						if(logon_info.equals("true")){
							showMessage("����ɹ�");
						}else{
							showMessage("����ʧ��");
							return ;
						}
						nssySoap.Power_Judge(username.getText().toString(), 10000, new SoapInterface() {	
							@Override
							public void soapResult(ArrayList<Object> arrayList) {
								Object object = arrayList.get(0);
								String user_role = object.toString();
								if(user_role.equals("Teacher")){
									push(TeacherActivity.class, "LogonActivity", map);
								}else if(user_role.equals("TeamLead") || user_role.equals("Worker")){
									push(WorkerActivity.class, "LogonActivity", map);
								}else{
									showMessage("�û���ɫ = " + user_role);
									return;
								}
								mainData.setUserName(username.getText().toString());
							}

							@Override
							public void soapError(String error) {
								showMessage("���ʴ��� : " + error);
							}
						});
					}

					@Override
					public void soapError(String error) {
						showMessage("���ʴ��� : " + error);
					}
				});
			}
		});
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
