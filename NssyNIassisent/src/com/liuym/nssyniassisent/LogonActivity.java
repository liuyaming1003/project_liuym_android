package com.liuym.nssyniassisent;

import java.util.ArrayList;
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
	private long mExitTime;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_logon);
		
		username = (EditText)findViewById(R.id.username);
		password = (EditText)findViewById(R.id.password);

		findViewById(R.id.logon_button).setOnClickListener(new Button.OnClickListener() {			
			@Override
			public void onClick(View v) {	

				waittingDialog.show(LogonActivity.this, "", "������...");
				nssySoap.impersonateValidUser(username.getText().toString(), password.getText().toString(), 10000, new SoapInterface() {		
					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						waittingDialog.dismiss();
						Object object = arrayList.get(0);
						String logon_info = object.toString();
						if(logon_info.equals("1")){
							waittingDialog.show(LogonActivity.this, "", "ʶ���û���ɫ, ��ȴ�...");
							nssySoap.Power_Judge(username.getText().toString(), 10000, new SoapInterface() {	
								@Override
								public void soapResult(ArrayList<Object> arrayList) {
									waittingDialog.dismiss();
									Object object = arrayList.get(0);
									String user_role = object.toString();
									mainData.setUserName(username.getText().toString());
									if(user_role.equals("Teacher")){
										waittingDialog.show(LogonActivity.this, "", "��ȡ��ʦ��Ϣ����ȴ�...");
										nssySoap.Teacher_InfoList(mainData.getUserName(), 10000, new SoapInterface() {					
											@Override
											public void soapResult(ArrayList<Object> arrayList) {
												waittingDialog.dismiss();
												String info_list = arrayList.get(0).toString();	
												if(mainData.setUserInfoList(info_list) == true){
													push(TeacherActivity.class);
												}else{
													showMessage("��ʦ��Ϣ����");
												}						
											}

											@Override
											public void soapError(String error) {
												waittingDialog.dismiss();
												showMessage("������Ϣ :" + error);						
											}
										});
									}else if(user_role.equals("TeamLead") || user_role.equals("Worker")){
										waittingDialog.show(LogonActivity .this, "", "��ȡ������Ա��Ϣ����ȴ�...");
										nssySoap.Teacher_InfoList(mainData.getUserName(), 10000, new SoapInterface() {					
											@Override
											public void soapResult(ArrayList<Object> arrayList) {
												String info_list = arrayList.get(0).toString();	
												waittingDialog.dismiss();
												if(mainData.setUserInfoList(info_list) == true){
													push(WorkerActivity.class);
												}else{
													showMessage("������Ա��Ϣ ����");
												}						
											}

											@Override
											public void soapError(String error) {
												waittingDialog.dismiss();
												showMessage("������Ϣ :" + error);						
											}
										});

									}else{
										showMessage("�û���ɫ = " + user_role);
										return;
									}
								}

								@Override
								public void soapError(String error) {
									waittingDialog.dismiss();
									showMessage("���ʴ��� : " + error);
								}
							});
						}else if(logon_info.equals("2")){
							showMessage("�û�������");
						}else if(logon_info.equals("3")){
							showMessage("�������");
						}else{
							showMessage("��������");
						}
					}

					@Override
					public void soapError(String error) {
						waittingDialog.dismiss();
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
