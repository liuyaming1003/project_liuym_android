package com.liuym.zxing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.view.SurfaceView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.nssyniassisent.MainData.Device_Info;
import com.liuym.soap.Soap.SoapInterface;
import com.liuym.worker.AssetQueryActivity;
import com.liuym.zxing.camera.CameraManager;
import com.liuym.zxing.decoding.CaptureActivityHandler;
import com.liuym.zxing.decoding.InactivityTimer;
import com.liuym.zxing.view.ViewfinderView;

public class CaptureActivity extends MainActivity implements Callback
{

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	// 屏幕方向
	public static boolean screenIsLandscape = true; //是否横屏 默认横屏 false 竖屏

	private Navigation navi = null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zxing);

		navi = (Navigation)findViewById(R.id.navigationView);
		navi.getBtn_left().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				pop(1, null);
			}
		});

		Configuration config = getResources().getConfiguration();
		if(config.orientation == Configuration.ORIENTATION_PORTRAIT){
			screenIsLandscape = false;
		}

		// 初始化 CameraManager
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface)
		{
			initCamera(surfaceHolder);
		}
		else
		{
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
		{
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (handler != null)
		{
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy()
	{
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder)
	{
		try
		{
			CameraManager.get().openDriver(surfaceHolder);
		}
		catch (IOException ioe)
		{
			return;
		}
		catch (RuntimeException e)
		{
			return;
		}
		if (handler == null)
		{
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		if (!hasSurface)
		{
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView()
	{
		return viewfinderView;
	}

	public Handler getHandler()
	{
		return handler;
	}

	public void drawViewfinder()
	{
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(final Result obj, Bitmap barcode)
	{
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("zxingCode", obj.getText());
		if(AssetQueryActivity.asset_query_type == 3){
			waittingDialog.show(CaptureActivity.this, "", "正在查询，请稍等...");
			nssySoap.Device_Info_List(obj.getText(), 1, 10000, new SoapInterface() {
				@Override
				public void soapResult(ArrayList<Object> arrayList) {
					waittingDialog.dismiss();
					String result = arrayList.get(0).toString();
					if(mainData.setDeviceInfoList(result)){
						mainData.device_Info = mainData.getDeviceInfoArrayList().get(0);
						push(AssetQueryActivity.class);
					}else{
						showMessage("错误信息" + result);
						pop(1, null);
						return;
					}
					showMessage("查询完成");
				}

				@Override
				public void soapError(String error) {
					waittingDialog.dismiss();
					showMessage("错误信息" + error);
				}
			});
		}else{
			pop(0, map);
		}
		/*
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		if (barcode == null)
		{
			dialog.setIcon(null);
		}
		else
		{

			Drawable drawable = new BitmapDrawable(barcode);
			dialog.setIcon(drawable);
		}
		dialog.setTitle("扫描结果");
		dialog.setMessage(obj.getText());
		dialog.setNegativeButton("确定", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				//用默认浏览器打开扫描得到的地址
				//Intent intent = new Intent();
				//intent.setAction("android.intent.action.VIEW");
				//Uri content_url = Uri.parse(obj.getText());
				//intent.setData(content_url);
				//startActivity(intent);
				//finish();


				pop();
			}
		});*/
		/*dialog.setPositiveButton("取消", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				//finish();
				hasSurface = false;
				dialog.dismiss();
			}
		});*/
		//dialog.create().show();
	}

	private void initBeepSound()
	{
		if (playBeep && mediaPlayer == null)
		{
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try
			{
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			}
			catch (IOException e)
			{
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate()
	{
		if (playBeep && mediaPlayer != null)
		{
			mediaPlayer.start();
		}
		if (vibrate)
		{
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener()
	{
		public void onCompletion(MediaPlayer mediaPlayer)
		{
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){  
			pop(1, null);
			return true;   
		}
		return super.onKeyDown(keyCode, event);
	}

}