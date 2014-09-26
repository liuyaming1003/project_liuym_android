package com.liuym.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import org.apache.http.impl.conn.tsccm.WaitingThread;

import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.MainData.UserInfoList;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.nssyniassisent.WaittingDialog;
import com.liuym.soap.Soap.SoapInterface;

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
				ArrayList<Map<String, Object>> teacherList = new ArrayList<Map<String,Object>>();
				if(mainData.setTeacherList(result)){
					for(int i = 0; i < mainData.getTeacherList().size(); i++){
						UserInfoList teacher = mainData.getTeacherList().get(i);
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("teacher", teacher);
						teacherList.add(map);
					}
					showSearch(teacherList);
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

	private void showSearch(final ArrayList<Map<String, Object>> arrayList){
		View view = inflater.inflate(R.layout.search_view, null);
		final Handler myhandler = new Handler();
		final EditText search_edittext = (EditText)view.findViewById(R.id.search_edittext);
		final ImageView delete_imageview = (ImageView)view.findViewById(R.id.delete_imageview);

		final PopupWindow window = new PopupWindow(view,  
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true); // 实例化PopupWindow
		window.setAnimationStyle(R.style.popwin_anim_style);
		window.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
		window.setFocusable(true);
		window.setOutsideTouchable(true);

		//listView显示数据
		final ArrayList<Map<String, Object>> listData = arrayList;
		//查找内容数据
		final ArrayList<Map<String, Object>> findData = new ArrayList<Map<String,Object>>();
		final ListView listView = (ListView)view.findViewById(R.id.listView);
		
		final MyListViewAdapter myAdapter = new MyListViewAdapter(this, listData, new ListViewInterface() {
			@Override
			public View Cell(MyListViewAdapter adapter, View cellView, int position) {
				if(cellView == null){
					cellView = inflater.inflate(android.R.layout.simple_list_item_1, null);
				}
				Map<String, Object> map = (Map<String, Object>) adapter.getItem(position);
				UserInfoList teacher = (UserInfoList)map.get("teacher");
				TextView name = (TextView)cellView.findViewById(android.R.id.text1);
				name.setText(teacher.RealName);
				return cellView;
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
					long arg3){
				Map<String, Object> map = (Map<String, Object>) myAdapter.getItem(arg2);
				UserInfoList teacher = (UserInfoList)map.get("teacher");
				Domain_UserName = teacher.Domain_UserName;
				username_info_editText.setText(teacher.RealName);
				username_phone_editText.setText(teacher.Mobile_Tel);
				window.dismiss();
			}
		});
		listView.setAdapter(myAdapter);



		final Runnable eChanged = new Runnable() {

			@Override
			public void run() {
				String data = search_edittext.getText().toString();

				listData.clear();//先要清空，不然会叠加
				//if(!data.equals("")){
					for(int i = 0; i < arrayList.size(); i ++){
						Map<String, Object> tMap = arrayList.get(i);
						UserInfoList teacher = (UserInfoList) tMap.get("teacher");
						if(teacher.RealName.contains(data)){
							Map<String, Object> map=new HashMap<String, Object>();
							map.put("teacher", teacher);
							listData.add(map);
						}
					}
				//}
				myAdapter.notifyDataSetChanged();//更新
			}
		};

		search_edittext.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				//这个应该是在改变的时候会做的动作吧，具体还没用到过。
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				//这是文本框改变之前会执行的动作
			}

			@Override
			public void afterTextChanged(Editable s) {
				/**这是文本框改变之后 会执行的动作
				 * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，并且如一的显示在界面上。
				 * 所以这里我们就需要加上数据的修改的动作了。
				 */
				if(s.length() == 0){
					delete_imageview.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
				}
				else {
					delete_imageview.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
				}

				myhandler.post(eChanged);
			}
		});



		delete_imageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				search_edittext.setText("");
			}
		});


		view.findViewById(R.id.cancel_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				window.dismiss();
			}
		});
	}

}
