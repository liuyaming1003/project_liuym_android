package com.liuym.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.MainData.Depart_Class;
import com.liuym.nssyniassisent.MainData.Repair_Recode;
import com.liuym.nssyniassisent.MainData.UserInfoList;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.soap.Soap.SoapInterface;

public class OrderDetailActivity extends MainActivity{
	private Navigation navi = null;
	private EditText order_name_edittext = null;
	private TextView phone_info_textview = null;
	private TextView short_number_info_textview = null;
	private TextView code_info_textview = null;
	private TextView repair_username_textview = null;
	private TextView repair_date_info_textview = null;
	private EditText repair_info_edittext = null;
	private TextView message_textview  = null;
	private Button send_info_button = null;
	private LayoutInflater inflater = null;
	private Repair_Recode repair = null;
	private UserInfoList toWorkInfo = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_orderdetail);
		inflater = LayoutInflater.from(this); 

		repair = mainData.getRepairRecodeArrayList().get(mainData.order_select_index);

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
				push(OrderFinishActivity.class);
			}
		});

		order_name_edittext = (EditText)findViewById(R.id.order_name_edittext);	
		order_name_edittext.setInputType(InputType.TYPE_NULL);
		order_name_edittext.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				showMyDialog(2);
			}
		});
		findViewById(R.id.order_button).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				if(toWorkInfo == null || order_name_edittext.getText().toString().equals("")){
					showMessage("请选择接单人");
					return;
				}
				showMyDialog(1);
			}
		});

		phone_info_textview = (TextView)findViewById(R.id.phone_info_textview);
		phone_info_textview.setText(repair.Mobile_Tel);
		findViewById(R.id.phone_call_button).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				//传入服务， parse（）解析号码
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +phone_info_textview.getText().toString()));
				//通知activtity处理传入的call服务
				startActivity(intent);
			}
		});

		short_number_info_textview = (TextView)findViewById(R.id.short_number_info_textview);
		short_number_info_textview.setText(repair.Group_Tel);
		findViewById(R.id.short_number_button).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				//传入服务， parse（）解析号码
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +short_number_info_textview.getText().toString()));
				//通知activtity处理传入的call服务
				startActivity(intent);
			}
		});



		code_info_textview = (TextView)findViewById(R.id.code_info_textview);
		code_info_textview.setText("" + repair.Repair_Recode_Num);
		repair_username_textview = (TextView)findViewById(R.id.repair_username_textview);
		repair_username_textview.setText(repair.Repair_RealName);
		repair_date_info_textview = (TextView)findViewById(R.id.repair_date_info_textview);
		repair_date_info_textview.setText(repair.Repair_time);

		message_textview = (TextView)findViewById(R.id.message_textview);

		repair_info_edittext = (EditText)findViewById(R.id.repair_info_edittext);
		send_info_button = (Button)findViewById(R.id.send_info_button);
		send_info_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String msg = repair_info_edittext.getText().toString();
				if(!msg.equals("")){
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
					imm.hideSoftInputFromWindow(repair_info_edittext.getWindowToken(),0);
					waittingDialog.show(OrderDetailActivity.this, "", "发送信息中...");
					nssySoap.Edit_Feedback_info(repair.Repair_Recode_Num, 1, 0, 0, msg, mainData.getUserInfo().Domain_UserName, 1, 10000, new SoapInterface() {
						@Override
						public void soapResult(ArrayList<Object> arrayList) {
							waittingDialog.dismiss();
							String result = arrayList.get(0).toString();
							if(result.equals("s")){
								showMessage("发送成功");
								message_textview.setText(message_textview.getText().toString() + "\n" + repair_info_edittext.getText().toString());
								repair_info_edittext.setText("");
							}else{
								showMessage("发送失败");
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
		});

	}

	private void showMyDialog(int index){
		final Dialog dialog = new Dialog(this, R.style.MyDialog);
		View view = null;
		switch (index) {
		case 1:
			/*view = inflater.inflate(R.layout.order_to, null);
			TextView order_name = (TextView)view.findViewById(R.id.order_to_textview);
			order_name.setText(order_name_edittext.getText().toString());
			view.findViewById(R.id.order_cancel_button).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if(dialog.isShowing()){
						dialog.dismiss();
					}
				}
			});
			view.findViewById(R.id.order_sure_button).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if(dialog.isShowing()){
						dialog.dismiss();
					}	

					waittingDialog.show(OrderDetailActivity.this, "", "正在转单，请稍等...");
					nssySoap.Repair_Recode_Transfer(repair.Repair_Recode_Num, mainData.getUserName(), toWorkInfo.Domain_UserName, "转单" ,10000, new SoapInterface() {
						@Override
						public void soapResult(ArrayList<Object> arrayList) {
							waittingDialog.dismiss();
							String result = arrayList.get(0).toString();
							if(result.equals("s")){
								showMessage("转单完成");
								//push(OrderFinishActivity.class);
								pop();
							}else{
								showMessage("转单失败" + result);
							}
						}

						@Override
						public void soapError(String error) {
							waittingDialog.dismiss();
							showMessage("错误信息" + error);
						}
					});	
				}
			});*/

		{
			AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
			builder.setMessage("此单即将转给 " + order_name_edittext.getText().toString());
			builder.setTitle("转单确认");
			builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					dialog.dismiss();
					waittingDialog.show(OrderDetailActivity.this, "", "正在转单，请稍等...");
					nssySoap.Repair_Recode_Transfer(repair.Repair_Recode_Num, mainData.getUserName(), toWorkInfo.Domain_UserName, "转单" ,10000, new SoapInterface() {
						@Override
						public void soapResult(ArrayList<Object> arrayList) {
							waittingDialog.dismiss();
							String result = arrayList.get(0).toString();
							if(result.equals("s")){
								showMessage("转单完成");
								//push(OrderFinishActivity.class);
								pop();
							}else{
								showMessage("转单失败" + result);
							}
						}

						@Override
						public void soapError(String error) {
							waittingDialog.dismiss();
							showMessage("错误信息" + error);
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
			break;
		}
		case 2:
			waittingDialog.show(OrderDetailActivity.this, "", "获取工作人员列表，请稍等");
			nssySoap.Worker_List(mainData.getUserInfo().DepartID, 10000, new SoapInterface() {
				@Override
				public void soapResult(ArrayList<Object> arrayList) {
					waittingDialog.dismiss();
					String result = arrayList.get(0).toString();
					if(mainData.setWorkerInfoList(result)){
						ArrayList<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
						for(int i = 0; i < mainData.getWorkInfoArrayList().size(); i++){
							UserInfoList userInfo =  mainData.getWorkInfoArrayList().get(i);
							Map<String, Object> map=new HashMap<String, Object>();
							map.put("work_info", userInfo);
							list.add(map);
						}
						View view = inflater.inflate(R.layout.picker_list, null);
						TextView picker_textview = (TextView)view.findViewById(R.id.picker_title);
						picker_textview.setText("请选择工作人员");
						ListView listView = (ListView)view.findViewById(R.id.myListView);
						final MyListViewAdapter infoAdapter = new MyListViewAdapter(OrderDetailActivity.this, list,  
								new ListViewInterface(){
							@Override
							public View Cell(MyListViewAdapter adapter, View cellView, int position) {
								if(cellView == null){
									cellView = inflater.inflate(R.layout.picker_cell, null);
								}
								TextView picker_cell_textview = (TextView)cellView.findViewById(R.id.picker_cell_textview);
								Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
								UserInfoList userInfo = (UserInfoList)map.get("work_info");
								picker_cell_textview.setText(userInfo.RealName);
								return cellView;
							}
						});
						listView.setAdapter(infoAdapter);
						listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
									long arg3){  
								@SuppressWarnings("unchecked")
								Map<String, Object> map = (Map<String, Object>)infoAdapter.getItem(arg2);
								UserInfoList userInfo = (UserInfoList)map.get("work_info");
								order_name_edittext.setText(userInfo.RealName);
								toWorkInfo = userInfo;
								dialog.dismiss();
							}  
						});
						dialog.setContentView(view);
					}else{
						showMessage("无工作人员信息" + result);
					}
				}
				@Override
				public void soapError(String error) {
					waittingDialog.dismiss();
					showMessage("错误信息" + error);
				}
			});
			dialog.show();
			break;
		default:
			return;
		}
		
	}
}