package com.liuym.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import org.apache.http.impl.conn.tsccm.WaitingThread;

import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MySearch;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.MainData.UserInfoList;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.nssyniassisent.WaittingDialog;
import com.liuym.soap.Soap.SoapInterface;
import com.liuym.worker.AssetQueryActivity;

import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.*;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PersonActivity extends MainActivity {
	private LayoutInflater inflater = null;
	private View rootView = null;
	private Navigation navi = null;
	private TextView teacher_info_TextView = null;
	private EditText username_info_editText = null;
	private EditText username_phone_editText = null;
	private String repari_phone = null;
	private String Domain_UserName = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this); 
		rootView = inflater.inflate(R.layout.activity_person, null);
		setContentView(rootView);



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
				String info = username_info_editText.getText().toString();
				String phone = username_phone_editText.getText().toString();
				if(info.equals("")){
					showMessage("请输入报修人姓名");
					return;
				}else if(phone.equals("")){
					showMessage("请输入报修人电话");
					return;
				}
				if(phone.equals(repari_phone)){
					phone = "";
				}
				waittingDialog.show(PersonActivity.this, "", "报修提交中, 请等待...");
				nssySoap.Report_Repair_Recode(Domain_UserName, mainData.getUserInfo().DepartID, info, 1, phone, 10000, new SoapInterface() {

					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						waittingDialog.dismiss();
						String result = arrayList.get(0).toString();
						if(result.equals("s")){
							showMessage("报修成功");
							if(!repari_phone.equals(username_phone_editText.getText().toString())){
								mainData.getUserInfo().Mobile_Tel = username_phone_editText.getText().toString();
							}
							pop();
						}else{
							showMessage("报修失败" + result);
						}						
					}

					@Override
					public void soapError(String error) {
						waittingDialog.dismiss();
						showMessage("访问错误:" + error);
					}
				});

			}
		});		
		username_info_editText = (EditText)findViewById(R.id.username_info);
		username_phone_editText = (EditText)findViewById(R.id.username_phone);
		teacher_info_TextView = (TextView)findViewById(R.id.teacher_info);
		String realName = mainData.getUserInfo().RealName;
		Domain_UserName = mainData.getUserInfo().Domain_UserName;
		teacher_info_TextView.setText(realName + ",你的校园网无线网络已连接: 00:30:25");
		username_info_editText.setText(realName);
		repari_phone = mainData.getUserInfo().Mobile_Tel;
		username_phone_editText.setText(repari_phone);

		username_info_editText.setInputType(InputType.TYPE_NULL);
		username_info_editText.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				teackerList();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.person, menu);
		return true;
	}

	private void teackerList(){
		waittingDialog.show(PersonActivity.this, "", "获取老师列表中...");
		nssySoap.Teacher_InfoList_Depart(mainData.getUserInfo().DepartID, 10000, new SoapInterface() {
			@Override
			public void soapResult(ArrayList<Object> arrayList) {
				waittingDialog.dismiss();
				String result = arrayList.get(0).toString();
				ArrayList<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				if(mainData.setTeacherList(result)){
					for(int i = 0; i < mainData.getTeacherList().size(); i++){
						UserInfoList teacher = mainData.getTeacherList().get(i);
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("object", teacher);
						list.add(map);
					}
					new MySearch().showSearch(PersonActivity.this, rootView, list, new MySearch.SearchInterface() {

						@Override
						public void selectCell(Object object) {
							if(object instanceof UserInfoList){
								UserInfoList userInfo = (UserInfoList)object;
								Domain_UserName = userInfo.Domain_UserName;
								username_info_editText.setText(userInfo.RealName);
								username_phone_editText.setText(userInfo.Mobile_Tel);
							}
						}

						@Override
						public boolean changedText(Object object, String data) {
							if(object instanceof UserInfoList){
								UserInfoList userInfo = (UserInfoList) object;
								if(userInfo.RealName.contains(data)){
									return true;
								}
							}
							return false;
						}

						@Override
						public View Cell(View cellView, Object object) {
							if(cellView == null){
								cellView = inflater.inflate(android.R.layout.simple_list_item_1, null);
							}
							TextView name = (TextView)cellView.findViewById(android.R.id.text1);
							if(object instanceof UserInfoList){
								UserInfoList userInfo = (UserInfoList)object;
								name.setText(userInfo.RealName);
							}
							return cellView;
						}
					});
				}else{
					showMessage("无老师列表信息");
				}
			}

			@Override
			public void soapError(String error) {
				waittingDialog.dismiss();
				showMessage("错误信息" + error);
			}
		});
	}
}
