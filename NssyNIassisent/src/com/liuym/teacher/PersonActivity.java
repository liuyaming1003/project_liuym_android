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
					showMessage("�����뱨��������");
					return;
				}else if(phone.equals("")){
					showMessage("�����뱨���˵绰");
					return;
				}
				if(phone.equals(repari_phone)){
					phone = "";
				}
				waittingDialog.show(PersonActivity.this, "", "�����ύ��, ��ȴ�...");
				nssySoap.Report_Repair_Recode(Domain_UserName, mainData.getUserInfo().DepartID, info, 1, phone, 10000, new SoapInterface() {

					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						waittingDialog.dismiss();
						String result = arrayList.get(0).toString();
						if(result.equals("s")){
							showMessage("���޳ɹ�");
							if(!repari_phone.equals(username_phone_editText.getText().toString())){
								mainData.getUserInfo().Mobile_Tel = username_phone_editText.getText().toString();
							}
							pop();
						}else{
							showMessage("����ʧ��" + result);
						}						
					}

					@Override
					public void soapError(String error) {
						waittingDialog.dismiss();
						showMessage("���ʴ���:" + error);
					}
				});

			}
		});		
		username_info_editText = (EditText)findViewById(R.id.username_info);
		username_phone_editText = (EditText)findViewById(R.id.username_phone);
		teacher_info_TextView = (TextView)findViewById(R.id.teacher_info);
		String realName = mainData.getUserInfo().RealName;
		Domain_UserName = mainData.getUserInfo().Domain_UserName;
		teacher_info_TextView.setText(realName + ",���У԰����������������: 00:30:25");
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
		waittingDialog.show(PersonActivity.this, "", "��ȡ��ʦ�б���...");
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
					showMessage("����ʦ�б���Ϣ");
				}
			}

			@Override
			public void soapError(String error) {
				waittingDialog.dismiss();
				showMessage("������Ϣ" + error);
			}
		});
	}

	private void showSearch(final ArrayList<Map<String, Object>> arrayList){
		View view = inflater.inflate(R.layout.search_view, null);
		final Handler myhandler = new Handler();
		final EditText search_edittext = (EditText)view.findViewById(R.id.search_edittext);
		final ImageView delete_imageview = (ImageView)view.findViewById(R.id.delete_imageview);

		final PopupWindow window = new PopupWindow(view,  
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true); // ʵ����PopupWindow
		window.setAnimationStyle(R.style.popwin_anim_style);
		window.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
		window.setFocusable(true);
		window.setOutsideTouchable(true);

		//listView��ʾ����
		final ArrayList<Map<String, Object>> listData = arrayList;
		//������������
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

				listData.clear();//��Ҫ��գ���Ȼ�����
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
				myAdapter.notifyDataSetChanged();//����
			}
		};

		search_edittext.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				//���Ӧ�����ڸı��ʱ������Ķ����ɣ����廹û�õ�����
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				//�����ı���ı�֮ǰ��ִ�еĶ���
			}

			@Override
			public void afterTextChanged(Editable s) {
				/**�����ı���ı�֮�� ��ִ�еĶ���
				 * ��Ϊ����Ҫ���ľ��ǣ����ı���ı��ͬʱ�����ǵ�listview������Ҳ������Ӧ�ı䶯��������һ����ʾ�ڽ����ϡ�
				 * �����������Ǿ���Ҫ�������ݵ��޸ĵĶ����ˡ�
				 */
				if(s.length() == 0){
					delete_imageview.setVisibility(View.GONE);//���ı���Ϊ��ʱ��������ʧ
				}
				else {
					delete_imageview.setVisibility(View.VISIBLE);//���ı���Ϊ��ʱ�����ֲ��
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
