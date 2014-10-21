package com.liuym.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleAdapter;
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
	private LinearLayout changedLayout;
	private ListView software_listView;
	private ArrayList<Map<String, Object>> software_tab_array;
	private SimpleAdapter software_adapter;
	private ListView hardware_listView;
	private ArrayList<Map<String, Object>> hardware_tab_array;
	private SimpleAdapter hardware_adapter;
	private boolean isFirstLaunch = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_orderfinish);

		inflater = LayoutInflater.from(this); 
		
		changedLayout = (LinearLayout)findViewById(R.id.hardware_replase_linearlayout);
		
		software_listView = (ListView)findViewById(R.id.software_ListView);
		
		software_tab_array = new ArrayList<Map<String,Object>>();
		
		software_adapter = new SimpleAdapter(this, software_tab_array, android.R.layout.simple_list_item_multiple_choice, new String[] {"title"}, new int[]{android.R.id.text1});
        //ʹ��ϵͳ���õ�layout
        software_listView.setAdapter(software_adapter);
        software_listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        hardware_listView = (ListView)findViewById(R.id.hardware_ListView);
        hardware_tab_array = new ArrayList<Map<String,Object>>();
        
		
        hardware_adapter = new SimpleAdapter(this, hardware_tab_array, android.R.layout.simple_list_item_multiple_choice, new String[] {"title"}, new int[]{android.R.id.text1});
        //ʹ��ϵͳ���õ�layout
		hardware_listView.setAdapter(hardware_adapter);
        hardware_listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		

		remark_mult_textview = (MultiAutoCompleteTextView)findViewById(R.id.remark_mult_textview);
		
		//����ɨ��
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
				faultObject.Malfunction_type = 1;
				software_listView.setVisibility(View.VISIBLE);
				hardware_listView.setVisibility(View.GONE);
				changedLayout.setVisibility(View.GONE);
				soft_btn.setBackgroundResource(R.drawable.button_press_bg);
				hard_btn.setBackgroundResource(R.drawable.order_to_send_btn_bg);
				hard_changed_btn.setBackgroundResource(R.drawable.order_to_send_btn_bg);
			}
		});

		hard_btn = (Button)findViewById(R.id.hardware_fault_button);
		hard_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				faultObject.Malfunction_type = 2;
				software_listView.setVisibility(View.GONE);
				hardware_listView.setVisibility(View.VISIBLE);
				changedLayout.setVisibility(View.GONE);
				soft_btn.setBackgroundResource(R.drawable.order_to_send_btn_bg);
				hard_btn.setBackgroundResource(R.drawable.button_press_bg);
				hard_changed_btn.setBackgroundResource(R.drawable.order_to_send_btn_bg);
			}
		});

		hard_changed_btn = (Button)findViewById(R.id.hardware_replace_button);
		hard_changed_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				faultObject.Malfunction_type = 3;
				software_listView.setVisibility(View.GONE);
				hardware_listView.setVisibility(View.GONE);
				changedLayout.setVisibility(View.VISIBLE);
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
			//�޸�
			switch(faultObject.Malfunction_type){
			case 1://�������
				soft_btn.setBackgroundResource(R.drawable.button_press_bg);
				software_listView.setVisibility(View.VISIBLE);
				hardware_listView.setVisibility(View.GONE);
				changedLayout.setVisibility(View.GONE);
				break;
			case 2://Ӳ������
				hard_btn.setBackgroundResource(R.drawable.button_press_bg);
				software_listView.setVisibility(View.GONE);
				hardware_listView.setVisibility(View.VISIBLE);
				changedLayout.setVisibility(View.GONE);
				break;
			case 3://Ӳ������
				hard_changed_btn.setBackgroundResource(R.drawable.button_press_bg);
				software_listView.setVisibility(View.GONE);
				hardware_listView.setVisibility(View.GONE);
				changedLayout.setVisibility(View.VISIBLE);
				break;
			}

			navi.getBtn_right().setText("�޸�");
			navi.getBtn_right().setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					faultObject.Malfunction_Handle = remark_mult_textview.getText().toString();
					long[] tab = software_listView.getCheckItemIds();
					String str = "";
					for(int i = 0; i < tab.length; i++){
						str += tab[i] + ",";
					}
					System.out.println("checkout: " + str);
					pop(1, null);
				}
			}); 
		}else{
			navi.getBtn_right().setText("���");
			faultObject.faultStatus = 2;
			faultObject.Malfunction_type = 1;
			soft_btn.setBackgroundResource(R.drawable.button_press_bg);
			navi.getBtn_right().setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					faultObject.Malfunction_Handle = remark_mult_textview.getText().toString();
					long[] tab = software_listView.getCheckItemIds();
					String str = "";
					for(int i = 0; i < tab.length; i++){
						str += tab[i] + ",";
					}
					System.out.println("checkout: " + str);
					pop(0, null);
				}
			});
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){  
			pop(2, null);
			return true;   
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
	{  
		//���Ը��ݶ���������������Ӧ�Ĳ���  
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
	
	//@Override  
	/*public void onWindowFocusChanged(boolean hasFocus){  
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus){  
			if(isFirstLaunch ){
				isFirstLaunch = false;
				
				//��ȡ������ϱ�ǩ
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("title", "word");
				software_tab_array.add(item);
				item = new HashMap<String, Object>();
				item.put("title", "qq");
				software_tab_array.add(item);
				item = new HashMap<String, Object>();
				item.put("title", "chrom");
				software_tab_array.add(item);
				software_adapter.notifyDataSetChanged();
				//��ȡӲ�����ϱ�ǩ
				item = new HashMap<String, Object>();
				item.put("title", "word1");
				hardware_tab_array.add(item);
				item = new HashMap<String, Object>();
				item.put("title", "qq2");
				hardware_tab_array.add(item);
				item = new HashMap<String, Object>();
				item.put("title", "chrom3");
				hardware_tab_array.add(item);
				hardware_adapter.notifyDataSetChanged();
				//��ȡӲ��������Ϣ
			}
		}
	}*/
}