package com.liuym.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.soap.Soap;

public class OrderHandleActivity extends MainActivity{
	private LayoutInflater inflater = null;
	private Navigation navi = null;
	//tableview
	private ListView listView = null;
	private List<Map<String, Object>> faultListArray;
	private MyListViewAdapter faultAdapter = null;
	public static FaultObject g_faultObject = null;
	float x = 0, ux = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_orderhandle);
		inflater = LayoutInflater.from(this); 

		navi = (Navigation)findViewById(R.id.navigationView);
		navi.getBtn_left().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				pop();
			}
		});

		navi.getBtn_right().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				if(faultListArray.size() <= 1){
					showMessage("请添加故障单");
					return;
				}
				
				AlertDialog.Builder builder = new AlertDialog.Builder(OrderHandleActivity.this);
				builder.setMessage("此单即将结案，请确认？");
				builder.setTitle("");
				builder.setPositiveButton("结案", new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						waittingDialog.show(OrderHandleActivity.this, "", "正在结案中...");
						JSONArray jsonArray = new JSONArray();
						for(int i = 0 ; i < faultListArray.size(); i++){
							final Map<String, Object> map = (Map<String, Object>)faultListArray.get(i);
							FaultObject fault = (FaultObject)map.get("fault");
							if(fault.faultStatus == 2){
								JSONObject jsonObject = new JSONObject();
								try {
									jsonObject.put("Device_Barcode", fault.Device_Barcode);
									jsonObject.put("Malfunction_type", fault.Malfunction_type);
									jsonObject.put("Malfunction_Handle", fault.Malfunction_Handle);
									jsonObject.put("Tab_Info", fault.Tab_Info);
									//jsonObject.put("Tab_Info", fault.Tab_Info);
									jsonObject.put("Accessory_Con", fault.Accessory_Con);
									jsonArray.put(jsonObject);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}
						
						System.out.println("array = " + jsonArray.toString());

						int repair_num = mainData.getRepairRecodeArrayList().get(mainData.order_select_index).Repair_Recode_Num;
						nssySoap.Handle_Repair(jsonArray.toString(), repair_num, 10000, new Soap.SoapInterface() {
							@Override
							public void soapResult(ArrayList<Object> arrayList) {
								waittingDialog.dismiss();
								String result = arrayList.get(0).toString();
								if(result.equals("s")){
									showMessage("结案成功");
									push(WorkerActivity.class);
								}else{
									showMessage("错误: " + result);
								}
							}

							@Override
							public void soapError(String error) {
								waittingDialog.dismiss();
								showMessage("错误信息: " + error);
							}
						});

					}
				});
				builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});

		initFaultListView();
	}

	void initFaultListView(){
		listView = (ListView)findViewById(R.id.listView);

		faultListArray = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();

		FaultObject faultObject = new FaultObject();
		faultObject.faultStatus = 1;
		map.put("fault", faultObject);
		faultListArray.add(0, map);
		faultAdapter = new MyListViewAdapter(OrderHandleActivity.this, faultListArray, new ListViewInterface() {
			@Override
			public View Cell(MyListViewAdapter adapter, View cellView, final int position) {
				if(cellView == null){
					cellView = inflater.inflate(R.layout.fault_info_cell, null);
				}
				RelativeLayout add_layout = (RelativeLayout)cellView.findViewById(R.id.fault_add);
				RelativeLayout info_layout = (RelativeLayout)cellView.findViewById(R.id.fault_info);
				TextView title = (TextView)cellView.findViewById(R.id.fault_title_textview);
				TextView msg = (TextView)cellView.findViewById(R.id.fault_msg_textview);
				final Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
				FaultObject fault = (FaultObject)map.get("fault");
				msg.setText(fault.Malfunction_Handle);
				if(fault.faultStatus == 1){
					title.setText("");
					add_layout.setVisibility(View.VISIBLE);
					info_layout.setVisibility(View.GONE);
				}else{
					title.setText("故障 " + (position + 1));
					add_layout.setVisibility(View.GONE);
					info_layout.setVisibility(View.VISIBLE);

					TextView fault_type_textview = (TextView)cellView.findViewById(R.id.fault_type_textview);
					TextView fault_msg_textview = (TextView)cellView.findViewById(R.id.fault_msg_textview);
					TextView fault_barcode_textview = (TextView)cellView.findViewById(R.id.fault_barcode_textview);
					TextView fault_tab_textview = (TextView)cellView.findViewById(R.id.fault_tab_textview);
					fault_tab_textview.setText("信息: " + fault.Tab_Info);
					fault_msg_textview.setText("备注: " + fault.Malfunction_Handle);
					fault_barcode_textview.setText("条码: " + fault.Device_Barcode);
					switch(fault.Malfunction_type){
					case 1://软件故障
						fault_type_textview.setText("故障类型: 软件故障");
						break;
					case 2://硬件故障
						fault_type_textview.setText("故障类型: 硬件故障");
						break;
					case 3://硬件更换
						fault_type_textview.setText("故障类型: 硬件更换");
						break;
					}
				}
				cellView.findViewById(R.id.del_fault_button).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						faultListArray.remove(position);
						faultAdapter.notifyDataSetChanged();
					}
				});
				return cellView;
			}
		});
		listView.setAdapter(faultAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
					long arg3){
				final Map<String, Object> map = (Map<String, Object>)faultListArray.get(arg2);
				FaultObject fault = (FaultObject)map.get("fault");
				if(fault.faultStatus == 1){
					OrderHandleActivity.g_faultObject = new FaultObject();
				}else{
					OrderHandleActivity.g_faultObject = fault;
				}
				push(OrderFinishActivity.class, 102);
			}  
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				final Map<String, Object> map = (Map<String, Object>)faultListArray.get(arg2);
				FaultObject fault = (FaultObject)map.get("fault");
				if(fault.faultStatus == 1){

				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(OrderHandleActivity.this);
					builder.setMessage("是否删除该故障单？");
					builder.setTitle("确认");
					builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							faultListArray.remove(arg2);
							faultAdapter.notifyDataSetChanged();
						}
					});
					builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.create().show();
				}

				return false;
			}
		});
	}

	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
	{
		if(requestCode == 102){
			if(resultCode == 0){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("fault", OrderHandleActivity.g_faultObject);
				faultListArray.add(0, map);
				faultAdapter.notifyDataSetChanged();
			}else if(resultCode == 1){
				faultAdapter.notifyDataSetChanged();
			}else{

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public class FaultObject{
		public int faultStatus = 1;          //故障添加状态，1 为添加，2 为修改
		public String Device_Barcode = "";        //设备条码
		public int Malfunction_type;         //故障类型，1 软件故障， 2 硬件故障，3 硬件更换
		public String Malfunction_Handle = "";    //处理内容
		public String Tab_Info = "";              //相关标签
		public String Accessory_type = "";        //配件类型
		public String Accessory_Con = "";         //配件内容
	}
}