package com.samapp.pinkb;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class SafePinEditText extends EditText implements EditText.OnTouchListener, EditText.OnFocusChangeListener{
	private SafePinKeyBoard pinKeyBoard = null;
	private String keyboardText = "";
	public SafePinEditText(Context context){
		super(context);

		init();
	}

	public SafePinEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public SafePinEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	private void init(){
		setOnTouchListener(this);
		setOnFocusChangeListener(this);
	}

	// 隐藏系统键盘
	private void hideSoftInputMethod(){
		InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
		if(imm.isActive()){
			imm.hideSoftInputFromWindow(this.getWindowToken(),0);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();

		System.out.println("onDetachedFromWindow ");

		if(pinKeyBoard != null){		
			pinKeyBoard.hideKeyboard();
			pinKeyBoard = null;
		}
	}

	private void isShowKeyBoard(boolean flag){
		hideSoftInputMethod();
		if(pinKeyBoard == null){
			pinKeyBoard = new SafePinKeyBoard(getContext(), this, keyboardText);
		}
		if(flag){
			pinKeyBoard.showKeyboard();
		}else{
			pinKeyBoard.hideKeyboard();
		}
	}

	public void setNoteText(String text){
		this.keyboardText = text;
		if(pinKeyBoard != null){
			pinKeyBoard.setKeyboardText(keyboardText);
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		System.out.println("onFocusChange " + hasFocus);
		if(hasFocus){
			isShowKeyBoard(true);
		}else{
			isShowKeyBoard(false);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			EditText editText = (EditText)v;

			editText.setTextIsSelectable(true);
			isShowKeyBoard(true);
			if(!editText.getText().toString().equals("")){
				int start = editText.getText().toString().length();
				editText.setSelection(start);
			}
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub


		int orientation = getResources().getConfiguration().orientation;

		if(orientation == Configuration.ORIENTATION_PORTRAIT){
			//竖屏
			isShowKeyBoard(false);
			pinKeyBoard = null;
		}

		if(orientation == Configuration.ORIENTATION_LANDSCAPE){
			//横屏
			isShowKeyBoard(false);
			pinKeyBoard = null;
		}

		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(pinKeyBoard.isShowing() == true){
			pinKeyBoard.hideKeyboard();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
