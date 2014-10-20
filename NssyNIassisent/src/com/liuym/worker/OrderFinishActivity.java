package com.liuym.worker;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.MainData.Device_Info;
import com.liuym.nssyniassisent.MainData.Repair_Recode;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.nssyniassisent.SerializableMap;
import com.liuym.soap.Soap.SoapInterface;
import com.liuym.worker.OrderHandleActivity.FaultObject;
import com.liuym.zxing.CaptureActivity;

public class OrderFinishActivity extends MainActivity{
	private Navigation navi = null;
	private LayoutInflater inflater = null;
	private View asset_input_view = null;
	private Button soft_btn;
	private Button hard_btn;
	private Button hard_changed_btn;
	private FaultObject faultObject;
	private MultiAutoCompleteTextView remark_mult_textview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_orderfinish);

		inflater = LayoutInflater.from(this); 

		asset_input_view = (View)findViewById(R.id.hardware_replase_linearlayout);
		asset_input_view.setVisibility(View.INVISIBLE);

		remark_mult_textview = (MultiAutoCompleteTextView)findViewById(R.id.remark_mult_textview);
		
		//条码扫描
		findViewById(R.id.barcode_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AssetQueryActivity.asset_query_type = 1;
				push(CaptureActivity.class, 100);
			}
		});
		
		soft_btn = (Button)findViewById(R.id.software_fault_button);
		soft_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				asset_input_view.setVisibility(View.INVISIBLE);
				faultObject.Malfunction_type = 1;
				soft_btn.setBackgroundResource(R.drawable.button_press_bg);
				hard_btn.setBackgroundResource(R.drawable.order_to_send_btn_bg);
				hard_changed_btn.setBackgroundResource(R.drawable.order_to_send_btn_bg);
			}
		});

		hard_btn = (Button)findViewById(R.id.hardware_fault_button);
		hard_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				asset_input_view.setVisibility(View.INVISIBLE);
				faultObject.Malfunction_type = 2;
				soft_btn.setBackgroundResource(R.drawable.order_to_send_btn_bg);
				hard_btn.setBackgroundResource(R.drawable.button_press_bg);
				hard_changed_btn.setBackgroundResource(R.drawable.order_to_send_btn_bg);
			}
		});

		hard_changed_btn = (Button)findViewById(R.id.hardware_replace_button);
		hard_changed_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				asset_input_view.setVisibility(View.VISIBLE);
				faultObject.Malfunction_type = 3;
				soft_btn.setBackgroundResource(R.drawable.order_to_send_btn_bg);
				hard_btn.setBackgroundResource(R.drawable.order_to_send_btn_bg);
				hard_changed_btn.setBackgroundResource(R.drawable.button_press_bg);
			}
		});
		
		navi = (Navigation)findViewById(R.id.navigationView);
		navi.getBtn_left().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				pop(2, null);
			}
		});

		faultObject = OrderHandleActivity.g_faultObject;
		if(faultObject.faultStatus == 2){
			remark_mult_textview.setText(faultObject.Malfunction_Handle);
			//修改
			switch(faultObject.Malfunction_type){
			case 1://软件故障
				soft_btn.setBackgroundResource(R.drawable.button_press_bg);
				break;
			case 2://硬件故障
				hard_btn.setBackgroundResource(R.drawable.button_press_bg);
				break;
			case 3://硬件更换
				hard_changed_btn.setBackgroundResource(R.drawable.button_press_bg);
				break;
			}

			navi.getBtn_right().setText("修改");
			navi.getBtn_right().setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					faultObject.Malfunction_Handle = remark_mult_textview.getText().toString();
					pop(1, null);
				}
			}); 
		}else{
			navi.getBtn_right().setText("添加");
			faultObject.faultStatus = 2;
			faultObject.Malfunction_type = 1;
			soft_btn.setBackgroundResource(R.drawable.button_press_bg);
			navi.getBtn_right().setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					faultObject.Malfunction_Handle = remark_mult_textview.getText().toString();
					pop(0, null);
				}
			});
		}
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
	
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
	{  
		//可以根据多个请求代码来作相应的操作  
		if(requestCode == 100){  
			if(resultCode == 0){
				Bundle bundle = data.getExtras();  
				SerializableMap serializableMap = (SerializableMap) bundle  
						.get("map");
				String zxingCode = serializableMap.getMap().get("zxingCode").toString();
				System.out.println("zxing code = " + zxingCode);
			}else{

			} 
		}  
		super.onActivityResult(requestCode, resultCode, data);  
	} 
}