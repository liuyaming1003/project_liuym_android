package com.liuym.worker;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;

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
					pop(WorkerActivity.class);
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