package com.liuym.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.soap.Soap.SoapInterface;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ClassActivity extends MainActivity {
	private Navigation navi = null;
	private TextView teacher_info_TextView = null;
	private EditText grage_info_editText = null;
	private EditText class_info_editText = null;
	private EditText phone_info_editText = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_class);

		grage_info_editText = (EditText)findViewById(R.id.grage_info_editText);
		class_info_editText = (EditText)findViewById(R.id.class_info_editText);
		phone_info_editText = (EditText)findViewById(R.id.phone_info_editText);
		phone_info_editText.setText(mainData.getUserInfo().Mobile_Tel);
		String realName = mainData.getUserInfo().RealName;
		teacher_info_TextView = (TextView)findViewById(R.id.teacher_info);
		teacher_info_TextView.setText(realName + ",你的校园网无线网络已连接: 00:30:25");

		grage_info_editText.setInputType(InputType.TYPE_NULL);
		grage_info_editText.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				grage_info_editText.setText("二年级");
			}
		});
		
		class_info_editText.setInputType(InputType.TYPE_NULL);
		class_info_editText.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				class_info_editText.setText("三班");
			}
		});
		
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
							showMessage("班级报修成功");
							pop();
						}else{
							showMessage("班级报修失败" + result);
						}						
					}

					@Override
					public void soapError(String error) {
						showMessage("访问错误:" + error);
					}
				});
			}
		});	
	}
	
	
	/*private void showClassDialog(int index){
		final LayoutInflater inflater = LayoutInflater.from(this); 
		final Dialog dialog = new Dialog(this, R.style.MyDialog);
		ListView listView;
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		switch (index) {
		case 1:			
		{
			for (int i = 0; i < 20; i++) {  
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("cell_info", "20140601 09:30:00 张晓颖"); 
				list.add(map);  
			}  
			view = inflater.inflate(R.layout.repair_queue, null);
			TextView repair_queue_textview = (TextView)view.findViewById(R.id.repair_queue_num);
			repair_queue_textview.setText("您前面还有" + list.size() + "位在等待");
			ListView listView = (ListView)view.findViewById(R.id.myListView);
			MyListViewAdapter infoAdapter = new MyListViewAdapter(this, list,  
					new ListViewInterface(){
				@Override
				public void setCell(MyListViewAdapter adapter, View CellView, int position) {
					TextView repair_queue_cell_info = (TextView)CellView.findViewById(R.id.repair_queue_cell_info);
					Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
					repair_queue_cell_info.setText((String)map.get("cell_info"));
				}

				@Override
				public View getCell(MyListViewAdapter adapter, final int position) {
					System.out.println("getCell index = " + position);
					View CellView = inflater.inflate(R.layout.repair_queue_cell, null);
					TextView repair_queue_cell_info = (TextView)CellView.findViewById(R.id.repair_queue_cell_info);
					Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
					repair_queue_cell_info.setText((String)map.get("cell_info"));
					return CellView;
				}
			});
			listView.setAdapter(infoAdapter);
			break;
		}
		case 2:
			view = inflater.inflate(R.layout.repair_worker_info, null);
			break;
		default:
			return;
		}
		dialog.setContentView(view);
		dialog.show();
	}*/
}
