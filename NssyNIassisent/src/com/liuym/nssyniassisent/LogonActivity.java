package com.liuym.nssyniassisent;

import java.util.HashMap;
import java.util.Map;
import com.liuym.nssyniassisent.R;
import com.liuym.teacher.TeacherActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogonActivity extends MainActivity {
	private Navigation navi = null;
	private EditText username = null;
	private EditText password = null;
	private Map<String, Object> map = null;
	private long mExitTime;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_logon);

		map=new HashMap<String, Object>();

		navi = (Navigation)findViewById(R.id.navigationView);
		findViewById(R.id.Logon).setOnClickListener(new Button.OnClickListener() {			
			@Override
			public void onClick(View v) {					  
				map.put("username", username.getText().toString());
				map.put("password", password.getText().toString());
				push(navi, TeacherActivity.class, "LogonActivity", map);				
			}
		});

		username = (EditText)findViewById(R.id.username);
		password = (EditText)findViewById(R.id.password);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){   
			//弹出确定退出对话框
			/*ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);  

	        List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);  

	        RunningTaskInfo rti = runningTasks.get(0);  
	        ComponentName component = rti.topActivity;  

	        Log.i("tracy", component.getClassName());  */
			if ((System.currentTimeMillis() - mExitTime) > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();// 更新mExitTime
			} else {
				System.exit(0);// 否则退出程序
			}
			return true;   
		}
		return super.onKeyDown(keyCode, event);
	}
}
