package com.liuym.teacher;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.MainData.Depart_Class;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.soap.Soap.SoapInterface;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ClassActivity extends MainActivity {
	private Navigation navi = null;
	private TextView teacher_info_TextView = null;
	private EditText grade_info_editText = null;
	private EditText class_info_editText = null;
	private EditText phone_info_editText = null;

	//�꼶��Ϣ
	List<Map<String, Object>> listGrade = null;	
	//�༶��Ϣ
	List<Map<String, Object>> listClass = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_class);

		//�����꼶��Ϣ
		listGrade = new ArrayList<Map<String,Object>>();		
		ArrayList<String> gradeArray = new ArrayList<String>();
		for(int i = 0; i < mainData.getRoomArrayList().size(); i++){
			Depart_Class depart_class = mainData.getRoomArrayList().get(i);
			String room_name = depart_class.Room_Name;
			if(!gradeArray.contains(room_name)){
				gradeArray.add(room_name);
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("Room_Name", depart_class.Room_Name);
				listGrade.add(map);
			}			
		}

		//��ʼ���༶
		listClass = new ArrayList<Map<String,Object>>();


		grade_info_editText = (EditText)findViewById(R.id.grade_info_editText);
		class_info_editText = (EditText)findViewById(R.id.class_info_editText);
		phone_info_editText = (EditText)findViewById(R.id.phone_info_editText);
		phone_info_editText.setText(mainData.getUserInfo().Mobile_Tel);
		String realName = mainData.getUserInfo().RealName;
		teacher_info_TextView = (TextView)findViewById(R.id.teacher_info);
		teacher_info_TextView.setText(realName + ",���У԰����������������: 00:30:25");

		if(listGrade.size() != 0){
			grade_info_editText.setInputType(InputType.TYPE_NULL);
			grade_info_editText.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View arg0) {
					showClassDialog(1);
					//grade_info_editText.setText("���꼶");
				}
			});
			
			class_info_editText.setInputType(InputType.TYPE_NULL);
			class_info_editText.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View arg0) {
					showClassDialog(2);
				}
			});
		}

		

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
				String info = "{"+ "phone:" +phone_info_editText.getText().toString() +"}";
				nssySoap.Report_Repair_Recode(mainData.getUserInfo().Domain_UserName, mainData.getUserInfo().DepartID, info, 2, 10000, new SoapInterface() {

					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						String result = arrayList.get(0).toString();
						if(result.equals("s")){
							showMessage("�༶���޳ɹ�");
							pop();
						}else{
							showMessage("�༶����ʧ��" + result);
						}						
					}

					@Override
					public void soapError(String error) {
						showMessage("���ʴ���:" + error);
					}
				});
			}
		});	
	}


	private void showClassDialog(int index){
		final LayoutInflater inflater = LayoutInflater.from(this); 
		final Dialog dialog = new Dialog(this, R.style.MyDialog);
		View view = null;
		switch (index) {
		case 1:			
		{  
			view = inflater.inflate(R.layout.picker_list, null);
			TextView picker_textview = (TextView)view.findViewById(R.id.picker_title);
			picker_textview.setText("��ѡ���꼶");
			ListView listView = (ListView)view.findViewById(R.id.myListView);
			final MyListViewAdapter infoAdapter = new MyListViewAdapter(this, listGrade,  
					new ListViewInterface(){
				@Override
				public void setCell(MyListViewAdapter adapter, View CellView, int position) {
					TextView picker_cell_textview = (TextView)CellView.findViewById(R.id.picker_cell_textview);
					Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
					picker_cell_textview.setText(map.get("Room_Name").toString());
				}

				@Override
				public View getCell(MyListViewAdapter adapter, final int position) {
					System.out.println("getCell index = " + position);
					View CellView = inflater.inflate(R.layout.picker_cell, null);
					TextView picker_cell_textview = (TextView)CellView.findViewById(R.id.picker_cell_textview);
					Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
					picker_cell_textview.setText(map.get("Room_Name").toString());
					return CellView;
				}
			});
			listView.setAdapter(infoAdapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
						long arg3){  
					@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>)infoAdapter.getItem(arg2);
					String roomName = map.get("Room_Name").toString();
					grade_info_editText.setText(roomName);
					dialog.dismiss();	
					listClass.clear();
					for(int i = 0; i < mainData.getRoomArrayList().size(); i++){
						Depart_Class depart_class = mainData.getRoomArrayList().get(i);
						if(roomName.equals(depart_class.Room_Name)){
							Map<String, Object> mapClass=new HashMap<String, Object>();
							mapClass.put("Room_Name", depart_class.Room_Name);
							mapClass.put("Room_ID", depart_class.Room_ID);
							mapClass.put("Room_Num", depart_class.Room_Num);
							mapClass.put("Room_Type", depart_class.Room_Type);
							listClass.add(mapClass);
						}
					}
				}  
			});
			break;
		}
		case 2:
			view = inflater.inflate(R.layout.picker_list, null);
			TextView picker_textview = (TextView)view.findViewById(R.id.picker_title);
			picker_textview.setText("��ѡ��༶");
			ListView listView = (ListView)view.findViewById(R.id.myListView);
			final MyListViewAdapter infoAdapter = new MyListViewAdapter(this, listClass,  
					new ListViewInterface(){
				@Override
				public void setCell(MyListViewAdapter adapter, View CellView, int position) {
					TextView picker_cell_textview = (TextView)CellView.findViewById(R.id.picker_cell_textview);
					Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
					picker_cell_textview.setText(map.get("Room_Num").toString());
				}

				@Override
				public View getCell(MyListViewAdapter adapter, final int position) {
					System.out.println("getCell index = " + position);
					View CellView = inflater.inflate(R.layout.picker_cell, null);
					TextView picker_cell_textview = (TextView)CellView.findViewById(R.id.picker_cell_textview);
					Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
					picker_cell_textview.setText(map.get("Room_Num").toString());
					return CellView;
				}
			});
			listView.setAdapter(infoAdapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
						long arg3){  
					@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>)infoAdapter.getItem(arg2);
					String roomName = map.get("Room_Num").toString();
					class_info_editText.setText(roomName);
					dialog.dismiss();
				}  
			});
			break;
		default:
			return;
		}
		dialog.setContentView(view);
		dialog.show();
	}
}
