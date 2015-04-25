package com.samapp.pinkb;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.example.bankkeyboard.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.PaintDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class SafePinKeyBoard {
	private Context context;
	private View mRootView;
	private View mView;
	private KeyboardView keyboardView;
	private TextView keyboardText;
	private EditText normalEditText;
	private EditText landEditText;
	public PopupWindow mPopupWindow = null;
	private Keyboard mNumbersKB = null;
	int orientation;
	@SuppressLint("InflateParams") 
	public SafePinKeyBoard(Context context, EditText editText, String text){
		this.context = context;
		System.out.println("context = " + context);
		if(this.context != null){
			mView = (View)LayoutInflater.from(context).inflate(R.layout.pin_keyboard, null);

			keyboardText = (TextView) mView.findViewById(R.id.keyboard_text);
			keyboardText.setText(text);
			keyboardView = (KeyboardView)mView.findViewById(R.id.keyboard_view);
			if(keyboardView != null){
				keyboardView.setEnabled(true);  
				keyboardView.setPreviewEnabled(false); 
				keyboardView.setOnKeyboardActionListener(listener); 
				this.normalEditText = editText;

				mNumbersKB = new Keyboard(context, R.xml.safe_numbers);
				changedPswdNumber(true);
			}

			Activity activity = (Activity)context;
			mRootView = ((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);

			orientation = context.getResources().getConfiguration().orientation;
			if(orientation == Configuration.ORIENTATION_LANDSCAPE){
				//横屏时需要对横屏的EditText处理
				landEditText = (EditText) mView.findViewById(R.id.landEditText);
				if(landEditText != null){
					landEditText.setInputType(editText.getInputType());

					landEditText.setOnTouchListener(new View.OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub

							EditText editText = (EditText)v;
							editText.setTextIsSelectable(true);

							return false;
						}
					});
				}
			}
		}
	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {

		}

		@Override
		public void swipeRight() {

		}

		@Override
		public void swipeLeft() {

		}

		@Override
		public void swipeDown() {

		}

		@Override
		public void onText(CharSequence text) {

		}

		@Override
		public void onRelease(int primaryCode) {

		}

		@Override
		public void onPress(int primaryCode) {

		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			EditText editText = normalEditText;
			if(orientation == Configuration.ORIENTATION_LANDSCAPE){
				editText = landEditText;
			}
			
			Editable editable = editText.getText();
			
			int start = editText.getSelectionStart();

			switch (primaryCode) {
			case Keyboard.KEYCODE_CANCEL:
				hideKeyboard();
				break;
			case Keyboard.KEYCODE_DELETE:
				if (editable != null && editable.length() > 0) {  
					if (start > 0) {  
						editable.delete(start - 1, start);  
					}  
				} 
				break;
			case Keyboard.KEYCODE_SHIFT:
				break;
			case Keyboard.KEYCODE_MODE_CHANGE:
				break;
			case 501:
				editable.insert(start, Character.toString('￥')); 
				break;
				// 0 ~ 9
			case 48:
			case 49:
			case 50:
			case 51:
			case 52:
			case 53:
			case 54:
			case 55:
			case 56:
			case 57:
				//editable.insert(start, Character.toString((char) primaryCode)); 
				//break;
			case 65: //A ~ Z
			case 66:
			case 67:
			case 68:
			case 69:
			case 70:
			case 71:
			case 72:
			case 73:
			case 74:
			case 75:
			case 76:
			case 77:
			case 78:
			case 79:
			case 80:
			case 81:
			case 82:
			case 83:
			case 84:
			case 85:
			case 86:
			case 87:
			case 88:
			case 89:
			case 90:
				//editable.insert(start, Character.toString((char) primaryCode)); 
				//break;
			case 97: //a ~ z
			case 98:
			case 99:
			case 100:
			case 101:
			case 102:
			case 103:
			case 104:
			case 105:
			case 106:
			case 107:
			case 108:
			case 109:
			case 110:
			case 111:
			case 112:
			case 113:
			case 114:
			case 115:
			case 116:
			case 117:
			case 118:
			case 119:
			case 120:
			case 121:
			case 122:
				//editable.insert(start, Character.toString((char) primaryCode)); 
				//break;
			default:
				editable.insert(start, Character.toString((char) primaryCode)); 
				break;
			}
		}
	};

	/**
	 * 
	 * @Title        SafePinKeyBoard 
	 * @Description  TODO
	 * @param        
	 * @return       void 
	 * @throws       none
	 * 
	 */
	public void showKeyboard(){
		if(keyboardView == null){
			return;
		}

		if(mPopupWindow == null){
			if(orientation == Configuration.ORIENTATION_LANDSCAPE){
				//横屏
				mPopupWindow = new PopupWindow(mView,  
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
				mPopupWindow.setFocusable(true);
				mPopupWindow.setOutsideTouchable(true);
			}else{
				mPopupWindow = new PopupWindow(mView,  
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true); // 实例化PopupWindow
				mPopupWindow.setFocusable(false);
				mPopupWindow.setOutsideTouchable(true);
			}

			mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
			mPopupWindow.setBackgroundDrawable(new PaintDrawable());
			mPopupWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					changedPswdNumber(true);
					
					if(orientation == Configuration.ORIENTATION_LANDSCAPE){
						String text = landEditText.getText().toString();
						if(!text.equals("")){
							normalEditText.setText(text);
							normalEditText.setSelection(text.length());
						}
					}
					
					mRootView.layout(0, 0, mRootView.getWidth(), mRootView.getHeight());
				}
			});
		}


		if(!mPopupWindow.isShowing()){
			if(orientation == Configuration.ORIENTATION_LANDSCAPE){
				mPopupWindow.showAtLocation(mRootView, Gravity.NO_GRAVITY, 0, 0);
			}else{
				mPopupWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);


				mPopupWindow.getContentView().measure(0,0);
				int popHeight = mPopupWindow.getContentView().getMeasuredHeight();

				int[] point = new  int[2] ;
				normalEditText.getLocationInWindow(point);

				final int dY = containY(normalEditText.getHeight() + point[1] + popHeight);
				if(dY + 30 > 0){
					TranslateAnimation moveAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, - dY - 30);
					moveAnimation.setDuration(200);
					moveAnimation.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							int left = mRootView.getLeft();
							int top = mRootView.getTop() - dY - 30;
							int width = mRootView.getWidth();
							int height = mRootView.getHeight();
							mRootView.clearAnimation();
							mRootView.layout(left, top, width, height);
						}
					});
					mRootView.startAnimation(moveAnimation);
				}
			}
		}
	}

	public void hideKeyboard(){
		if(mPopupWindow != null && mPopupWindow.isShowing()){
			mPopupWindow.dismiss();
			changedPswdNumber(true);
		}
	}

	public boolean isShowing(){
		if(mPopupWindow == null){
			return false;
		}
		return mPopupWindow.isShowing();
	}

	public void setKeyboardText(String text){
		if(keyboardText != null){
			keyboardText.setText(text);
		}
	}

	//纯数字的密码键盘随机排列
	private void changedPswdNumber(boolean isUnordered){
		if(mNumbersKB == null){
			return ;
		}
		if(isUnordered){
			List<Key> keylist = mNumbersKB.getKeys();

			Integer[] randInt = getRandomId(9);

			for(int i = 0, j = 0; i < keylist.size(); i++){
				Key key = keylist.get(i);
				if(key.codes[0] >= 48 && key.codes[0] <= 57){
					if(j <= 9){
						key.codes[0] = randInt[j] + 48;
						key.label = String.valueOf(randInt[j]);
					}
					j++;
				}
			}
		}
		keyboardView.setKeyboard(mNumbersKB);
	}

	/**
	 * 数字从0开始到n(包含n)乱序排列
	 * @param 
	 * @return 返回数字0-n乱序排列的数组
	 */
	private Integer[] getRandomId(int n){
		Integer[] arryRandom = new Integer[n + 1];
		for (int i = 0; i <= n; i++){
			arryRandom[i] = i;
		}
		List<Integer> list = Arrays.asList(arryRandom);
		Collections.shuffle(list);
		return arryRandom;
	}

	private int containY(int y){
		int dY = 0;
		if(y > 0){
			DisplayMetrics metrics=new DisplayMetrics();
			((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int heightPixels=metrics.heightPixels;
			dY = y - heightPixels;
		}
		return dY;
	}

}
