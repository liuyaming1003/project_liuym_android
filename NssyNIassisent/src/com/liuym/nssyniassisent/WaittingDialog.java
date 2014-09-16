package com.liuym.nssyniassisent;

import android.app.ProgressDialog;
import android.content.Context;


public class WaittingDialog{
	private static WaittingDialog waittingDialog = null;
	private ProgressDialog progressDialog = null;
	private WaittingDialog() {
	}
	
	public static WaittingDialog getDefault(){
		if(waittingDialog == null){
			waittingDialog = new WaittingDialog();;
		}
		return waittingDialog;
	}
	
	public void show(Context context, String title, String message){
		if(context != null){
			progressDialog = ProgressDialog.show(context, title, message);
		}
	}
	
	public void dismiss(){
		if(progressDialog != null && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
	}
}