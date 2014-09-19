package com.liuym.worker;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.MainData.Repair_Recode;
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
	private LayoutInflater inflater = null;
	private Repair_Recode repair = null;
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
		findViewById(R.id.order_button).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				showHistoryDialog(1);
			}
		});
		
		phone_info_textview = (TextView)findViewById(R.id.phone_info_textview);
		phone_info_textview.setText(repair.Mobile_Tel);
		findViewById(R.id.phone_call_button).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				 //������� parse������������
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +phone_info_textview.getText().toString()));
                //֪ͨactivtity�������call����
                startActivity(intent);
			}
		});
		
		short_number_info_textview = (TextView)findViewById(R.id.short_number_info_textview);
		short_number_info_textview.setText(repair.Group_Tel);
		findViewById(R.id.short_number_button).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				 //������� parse������������
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +short_number_info_textview.getText().toString()));
                //֪ͨactivtity�������call����
                startActivity(intent);
			}
		});
		
		code_info_textview = (TextView)findViewById(R.id.code_info_textview);
		code_info_textview.setText("" + repair.Repair_Recode_Num);
		repair_username_textview = (TextView)findViewById(R.id.repair_username_textview);
		repair_username_textview.setText(repair.Repair_Domain_UserName);
		repair_date_info_textview = (TextView)findViewById(R.id.repair_date_info_textview);
		repair_date_info_textview.setText(repair.Repair_time);
	}
	
	private void showHistoryDialog(int index){
		final Dialog dialog = new Dialog(this, R.style.MyDialog);
		View view = null;
		switch (index) {
		case 1:
			view = inflater.inflate(R.layout.order_to, null);
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
					waittingDialog.show(OrderDetailActivity.this, "", "����ת�������Ե�...");
					nssySoap.Repair_Recode_Transfer(repair.Repair_Recode_Num, mainData.getUserName(), order_name_edittext.getText().toString(), "ת��" ,10000, new SoapInterface() {
						@Override
						public void soapResult(ArrayList<Object> arrayList) {
							waittingDialog.dismiss();
							showMessage("��Ϣ" + arrayList.get(0).toString());
							push(OrderFinishActivity.class);
						}

						@Override
						public void soapError(String error) {
							waittingDialog.dismiss();
							showMessage("������Ϣ" + error);
						}
					});	
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