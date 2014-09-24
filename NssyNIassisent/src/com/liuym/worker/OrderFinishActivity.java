package com.liuym.worker;

import java.util.ArrayList;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.MainData.Repair_Recode;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.soap.Soap.SoapInterface;

public class OrderFinishActivity extends MainActivity{
	private Navigation navi = null;
	private LayoutInflater inflater = null;
	private View asset_input_view = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_orderfinish);

		inflater = LayoutInflater.from(this); 

		asset_input_view = (View)findViewById(R.id.hardware_replase_linearlayout);
		asset_input_view.setVisibility(View.INVISIBLE);

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
				showMyDialog(1);
			}
		});

		findViewById(R.id.software_fault_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				asset_input_view.setVisibility(View.INVISIBLE);
			}
		});

		findViewById(R.id.hardware_fault_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				asset_input_view.setVisibility(View.INVISIBLE);
			}
		});

		findViewById(R.id.hardware_replace_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				asset_input_view.setVisibility(View.VISIBLE);
			}
		});
	}

	private void showMyDialog(int index){
		final Dialog dialog = new Dialog(this, R.style.MyDialog);
		View view = null;
		switch (index) {
		case 1:
			view = inflater.inflate(R.layout.order_finish, null);
			view.findViewById(R.id.over_cancel_button).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if(dialog.isShowing()){
						dialog.dismiss();
					}
				}
			});
			view.findViewById(R.id.over_sure_button).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if(dialog.isShowing()){
						dialog.dismiss();
					}
					waittingDialog.show(OrderFinishActivity.this, "", "正在结案，请稍等...");
					Repair_Recode repair = mainData.getRepairRecodeArrayList().get(mainData.order_select_index);
					nssySoap.Repair_Closed(repair.Repair_Recode_Num, repair.Device_Barcode, 2, "", "", 10000, new SoapInterface() {
						@Override
						public void soapResult(ArrayList<Object> arrayList) {
							waittingDialog.dismiss();
							String result = arrayList.get(0).toString();
							if(result.equals("s")){
								pop(WorkerActivity.class);
							}else{
								showMessage("错误信息" + result);
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
			break;
		default:
			return;
		}
		dialog.setContentView(view);
		dialog.show();
	}
}