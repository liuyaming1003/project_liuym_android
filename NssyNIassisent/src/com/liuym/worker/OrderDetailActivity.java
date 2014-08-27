package com.liuym.worker;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;

public class OrderDetailActivity extends MainActivity{
	private Navigation navi = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_orderdetail);
		
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
	}
}