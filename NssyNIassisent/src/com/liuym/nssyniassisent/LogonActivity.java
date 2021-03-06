package com.liuym.nssyniassisent;

import java.util.ArrayList;
import com.liuym.nssyniassisent.R;
import com.liuym.soap.Soap.SoapInterface;
import com.liuym.teacher.TeacherActivity;
import com.liuym.worker.WorkerActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
		
		username.setText(getLocal("username"));
		password.setText(getLocal("password"));

		findViewById(R.id.logon_button).setOnClickListener(new Button.OnClickListener() {			
			@Override
			public void onClick(View v) {	

				waittingDialog.show(LogonActivity.this, "", "登入中...");
				nssySoap.impersonateValidUser(username.getText().toString(), password.getText().toString(), 10000, new SoapInterface() {		
					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						waittingDialog.dismiss();
						Object object = arrayList.get(0);
						String logon_info = object.toString();
						if(logon_info.equals("1")){
							//保存用户名和密码
							saveLocal("username", username.getText().toString());
							saveLocal("password", password.getText().toString());
							waittingDialog.show(LogonActivity.this, "", "识别用户角色, 请等待...");
							nssySoap.Power_Judge(username.getText().toString(), 10000, new SoapInterface() {	
								@Override
								public void soapResult(ArrayList<Object> arrayList) {
									waittingDialog.dismiss();
									Object object = arrayList.get(0);
									String user_role = object.toString();
									mainData.setUserName(username.getText().toString());
									if(user_role.equals("Teacher")){
										waittingDialog.show(LogonActivity.this, "", "获取教师信息，请等待...");
										nssySoap.Teacher_InfoList(mainData.getUserName(), 10000, new SoapInterface() {					
											@Override
											public void soapResult(ArrayList<Object> arrayList) {
												waittingDialog.dismiss();
												String info_list = arrayList.get(0).toString();	
												if(mainData.setUserInfoList(info_list) == true){
													push(TeacherActivity.class);
												}else{
													showMessage("教师信息错误");
												}						
											}

											@Override
											public void soapError(String error) {
												waittingDialog.dismiss();
												showMessage("错误信息 :" + error);						
											}
										});
									}else if(user_role.equals("TeamLead") || user_role.equals("Worker")){
										waittingDialog.show(LogonActivity .this, "", "获取工作人员信息，请等待...");
										nssySoap.Teacher_InfoList(mainData.getUserName(), 10000, new SoapInterface() {					
											@Override
											public void soapResult(ArrayList<Object> arrayList) {
												String info_list = arrayList.get(0).toString();	
												waittingDialog.dismiss();
												if(mainData.setUserInfoList(info_list) == true){
													push(WorkerActivity.class);
												}else{
													showMessage("工作人员信息 错误");
												}						
											}

											@Override
											public void soapError(String error) {
												waittingDialog.dismiss();
												showMessage("错误信息 :" + error);						
											}
										});

									}else{
										showMessage("用户角色 = " + user_role);
										return;
									}
								}

								@Override
								public void soapError(String error) {
									waittingDialog.dismiss();
									showMessage("访问错误 : " + error);
								}
							});
						}else if(logon_info.equals("2")){
							showMessage("用户不存在");
						}else if(logon_info.equals("3")){
							showMessage("密码错误");
						}else{
							showMessage("其他错误");
						}
					}

					@Override
					public void soapError(String error) {
						waittingDialog.dismiss();
						showMessage("访问错误 : " + error);
					}
				});


			}
		});
	}
	
	private void saveLocal(String key, String data){
		SharedPreferences sharedPreferences = getSharedPreferences("Nssy", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, data);
		//保存数据
		editor.commit();
	}
	
	private String getLocal(String key){
		SharedPreferences sharedPreferences = getSharedPreferences("Nssy", Context.MODE_PRIVATE);
		//getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
		return sharedPreferences.getString(key, "");
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
