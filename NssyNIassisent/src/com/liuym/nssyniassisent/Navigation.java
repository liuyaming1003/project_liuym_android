package com.liuym.nssyniassisent;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;

public class Navigation extends LinearLayout {

	private Button btn_left;
	private Button btn_right;
	private TextView tv_title;
	private String strBtnLeft;
	private String strBtnRight;
	private String strTitle;
	private int left_drawable;
	private int right_drawable;
	private int backgroundColor;
	private int backgroundAlpha;

	public Navigation(Context context) {
		super(context);
		initContent();
	}

	public Navigation(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttributes(attrs);
		initContent();
	}

	private void initAttributes(AttributeSet attributeSet) {

		if (null != attributeSet) {

			final int attrIds[] = new int[] { R.attr.btn_leftText,
					R.attr.btn_rightText, R.attr.tv_title,
					R.attr.left_drawable, R.attr.right_drawable, R.attr.backgroundColor , R.attr.backgroundAlpha};

			Context context = getContext();

			TypedArray array = context.obtainStyledAttributes(attributeSet,
					attrIds);

			CharSequence t1 = array.getText(0);
			CharSequence t2 = array.getText(1);
			CharSequence t3 = array.getText(2);
			left_drawable = array.getResourceId(3, 0);
			right_drawable = array.getResourceId(4, 0);
			backgroundColor = array.getColor(5, 0);
			backgroundAlpha = array.getInt(6, 255);

			array.recycle();

			if (null != t1) {
				strBtnLeft = t1.toString();
			}
			if (null != t2) {
				strBtnRight = t2.toString();

			}
			if (null != t3) {
				strTitle = t3.toString();
			}

			Log.i("coder", "t1 = " + t1);
			Log.i("coder", "t2 = " + t2);
			Log.i("coder", "t3 = " + t3);
			Log.i("coder", "left_res = " + left_drawable);
		}

	}

	private void initContent() {

		Log.i("coder", "-----initContent----");
		// 设置水平方向
		setOrientation(HORIZONTAL);

		setGravity(Gravity.CENTER_VERTICAL);
		// 设置背景
		//setBackgroundResource(R.drawable.navigation_bg);
		setBackgroundColor(backgroundColor);
		if(backgroundAlpha >= 0 && backgroundAlpha <= 255){
			getBackground().setAlpha(backgroundAlpha);
		}

		Context context = getContext();

		btn_left = new Button(context);
		btn_left.setVisibility(View.INVISIBLE);// 设置设置不可�?
		if (left_drawable != 0) {
			btn_left.setBackgroundResource(left_drawable);
		} else {

			//btn_left.setBackgroundResource(R.drawable.backbg);// 设置背景
			btn_left.setBackgroundColor(Color.alpha(0));
		}
		btn_left.setTextColor(Color.WHITE);// 字体颜色

		if (null != strBtnLeft) {
			LayoutParams btnLeftParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			btnLeftParams.setMargins(10, 0, 0, 0);
			btn_left.setLayoutParams(btnLeftParams);
			btn_left.setText(strBtnLeft);
			btn_left.setVisibility(View.VISIBLE);
		} else {
			btn_left.setLayoutParams(new LayoutParams(50, 50));
		}

		// 添加这个按钮
		addView(btn_left);

		//
		tv_title = new TextView(context);

		LayoutParams centerParam = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		centerParam.weight = 1;
		tv_title.setLayoutParams(centerParam);
		tv_title.setTextColor(Color.WHITE);

		if (null != strTitle) {
			tv_title.setText(strTitle);
		}

		tv_title.setGravity(Gravity.CENTER);
		btn_left.setVisibility(View.VISIBLE);
		// 添加这个标题
		addView(tv_title);

		btn_right = new Button(context);
		btn_right.setVisibility(View.INVISIBLE);// 设置设置不可�?
		btn_right.setTextColor(Color.WHITE);// 字体颜色

		if (right_drawable != 0) {
			btn_right.setBackgroundResource(right_drawable);
		} else {
			btn_right.setBackgroundColor(Color.alpha(0));
		}
		if (null != strBtnRight) {

			LayoutParams btnRightParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			btnRightParams.setMargins(0, 0, 10, 0);
			btn_right.setLayoutParams(btnRightParams);

			btn_right.setText(strBtnRight);
			btn_right.setVisibility(View.VISIBLE);
		} else {
			btn_right.setLayoutParams(new LayoutParams(50, 50));
		}

		// 添加这个按钮
		addView(btn_right);

	}

	public Button getBtn_left() {
		return btn_left;
	}

	public Button getBtn_right() {
		return btn_right;
	}

	public TextView getTv_title() {
		return tv_title;
	}

	public String getStrBtnLeft() {
		return strBtnLeft;
	}

	public void setStrBtnLeft(String strBtnLeft) {
		this.strBtnLeft = strBtnLeft;
	}

	public String getStrBtnRight() {
		return strBtnRight;
	}

	public void setStrBtnRight(String strBtnRight) {
		this.strBtnRight = strBtnRight;
	}

	public String getStrTitle() {
		return strTitle;
	}

	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
	}

	public int getLeft_drawable() {
		return left_drawable;
	}

	public void setLeft_drawable(int left_drawable) {
		this.left_drawable = left_drawable;
	}

	public int getRight_drawable() {
		return right_drawable;
	}

	public void setRight_drawable(int right_drawable) {
		this.right_drawable = right_drawable;
	}

	public void pushNavigation(Activity push,  Intent pushIntent){
		push.startActivity(pushIntent);
		push.overridePendingTransition(R.anim.navigation_push_in,R.anim.navigation_push_out);
	}

	public void popNavigation(Activity pop){
		pop.finish();
		pop.overridePendingTransition(R.anim.navigation_pop_in, R.anim.navigation_pop_out);
	}
	
	public void popRootNavigation(Activity push,  Intent pushIntent){
		//pushIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		push.startActivity(pushIntent);
		push.overridePendingTransition(R.anim.navigation_pop_in, R.anim.navigation_pop_out);
	}
}