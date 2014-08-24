package com.liuym.nssyniassisent;

import com.liuym.nssyniassisent.R;
import com.liuym.teacher.TeacherActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class LogonActivity extends MainActivity {
	private Navigation navi = null;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_logon);
        
        navi = (Navigation)findViewById(R.id.navigationView);
		findViewById(R.id.Logon).setOnClickListener(new Button.OnClickListener() {			
			@Override
			public void onClick(View v) {
				push(navi, TeacherActivity.class);
			}
		});
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
			return true;   
		}
		return super.onKeyDown(keyCode, event);
	}
}
