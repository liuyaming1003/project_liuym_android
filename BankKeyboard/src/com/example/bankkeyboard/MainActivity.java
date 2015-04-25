package com.example.bankkeyboard;

import com.samapp.pinkb.SafePinEditText;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		((SafePinEditText)findViewById(R.id.editText1)).setNoteText("正在输入明文密码");
		
		
		((SafePinEditText)findViewById(R.id.editText2)).setNoteText("正在输入密文密码");
	}
	
	/**
	 * 密码键盘 Android 说明
	 * 
	 * 使用系统 KeyboardView 布局键盘 layout             -> pin_keyboard.xml
	 * 使用系统Key 来设置按键          xml                -> safe_numbers.xml
	 * 使用资源                      drawable-xhdpi     -> keyboard_delete.png 
	 *                                                 -> keyboard_number_press.png 
	 *                                                 -> keyboard_number.png 
	 * 设置按键按下效果                                   -> pin_key_bg.xml
	 * 键盘弹出和隐藏动画               anim              -> pop_up_down.xml 
	 *                                                 ->push_down_up.xml
	 *                                                 
	 * 需要使用密码键盘时 使用控件 SafePinEditText , 具体看 activity_main.xml                                                
	 */
}
