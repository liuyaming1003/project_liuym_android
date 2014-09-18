package com.liuym.teacher;

import java.util.ArrayList;

import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.soap.Soap.SoapInterface;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.*;
import android.widget.EditText;
import android.widget.TextView;

public class PersonActivity extends MainActivity {
	private Navigation navi = null;
	private TextView teacher_info_TextView = null;
	private EditText username_info_editText = null;
	private EditText username_phone_editText = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person);
		
		navi = (Navigation)findViewById(R.id.navigationView);
		navi.getBtn_left().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				pop();
			}
		});
		
		findViewById(R.id.confirm_btn).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				String info = "{name:" + username_info_editText.getText().toString() + ",phone:" + username_phone_editText.getText().toString() + "}";
				waittingDialog.show(PersonActivity.this, "", "�����ύ��, ��ȴ�...");
				nssySoap.Report_Repair_Recode(mainData.getUserInfo().Domain_UserName, mainData.getUserInfo().DepartID, info, 1, 10000, new SoapInterface() {
					
					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						waittingDialog.dismiss();
						String result = arrayList.get(0).toString();
						if(result.equals("s")){
							showMessage("���޳ɹ�");
							pop();
						}else{
							showMessage("����ʧ��" + result);
						}						
					}
					
					@Override
					public void soapError(String error) {
						// TODO Auto-generated method stub
						showMessage("���ʴ���:" + error);
					}
				});
				
			}
		});		
		username_info_editText = (EditText)findViewById(R.id.username_info);
		username_phone_editText = (EditText)findViewById(R.id.username_phone);
		teacher_info_TextView = (TextView)findViewById(R.id.teacher_info);
		String realName = mainData.getUserInfo().RealName;
		teacher_info_TextView.setText(realName + ",���У԰����������������: 00:30:25");
		username_info_editText.setText(realName);
		String phone = mainData.getUserInfo().Mobile_Tel;
		username_phone_editText.setText(phone);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.person, menu);
		return true;
	}

}
