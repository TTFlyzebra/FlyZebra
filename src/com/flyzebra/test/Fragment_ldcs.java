package com.flyzebra.test;

import com.flyzebra.test.customui.RotateImage;
import com.flyzebra.test.tools.DateTools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Fragment_ldcs extends Fragment implements OnClickListener{
	public final String TAG = "com.flyzebra";
	
	@SuppressLint("HandlerLeak")
	public class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if (getActivity() != null) {
				switch (msg.what) {
				case UP_NEEDLS:
					refreshSpeed(current_speed);
					break;
				case UP_DAEMON:
					upDateTextView();
					break;
				}
			}
		}
	}

	private final int UP_NEEDLS = 11;
	private final int UP_DAEMON = 12;
	private RotateImage needls_01;
	private RotateImage iv_direction;
	private ImageView iv_exit;
	
//	private Bitmap needls_bitmap;
//	private Bitmap rotate_bitmap;
	private int old_speed = 120;
	private int current_speed = 0;
	private MyHandler mHandler = new MyHandler();
	private TextView tv_speed;
	private DaemonThread mDaemonThred;

	private TextView tv_time;
	private TextView tv_date;

	// 方向传感器
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private TextView tv_direction;
	
	// GPS
	private LocationManager locationManager = null;
	private LocationListener llistener;
	
	public Fragment_ldcs() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_ldcs, container, false);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		needls_01 = (RotateImage) view.findViewById(R.id.needls_01);
		iv_direction = (RotateImage) view.findViewById(R.id.iv_direction);
//		needls_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.needle02);
//		needls_01.setImageBitmap(needls_bitmap);
		tv_speed = (TextView) view.findViewById(R.id.tv_speed);
		tv_speed.setText(String.valueOf(old_speed));
		
		tv_direction = (TextView) view.findViewById(R.id.tv_direction);

		tv_time = (TextView) view.findViewById(R.id.tv_time);
		tv_date = (TextView) view.findViewById(R.id.tv_date);
		iv_exit = (ImageView) view.findViewById(R.id.iv_exit);
		iv_exit.setOnClickListener(this);
		
		//方向传感器
		mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

		
		//GPS
		getActivity();
		locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		llistener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					double lat = location.getLatitude(); // 纬度
					double lng = location.getLongitude(); // 经度
					long time = location.getTime();
					Log.w(TAG,"纬度->"+lat+"，经度->"+lng+"->时间:"+time);
				}
			}
			@Override
			public void onProviderDisabled(String provider) {
			}
			@Override
			public void onProviderEnabled(String provider) {
			}
			@Override
			public void onStatusChanged(String provider, int status,Bundle extras) {
			}
		};	
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1.0f, llistener);
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshSpeed(current_speed);
		// Matrix matrix = new Matrix();
		// matrix.setRotate(-120);
		// rotate_bitmap = Bitmap.createBitmap(needls_bitmap, 0, 0,
		// needls_bitmap.getWidth(), needls_bitmap.getHeight(),
		// matrix, true);
		// needls_01.setImageBitmap(rotate_bitmap);

		if (mAccelerometer != null) {
			mSensorManager.registerListener(mSensorEventListener, mAccelerometer,SensorManager.SENSOR_DELAY_GAME);
		}
		if (mDaemonThred == null) {
			mDaemonThred = new DaemonThread();
			mDaemonThred.setDaemon(true);
			mDaemonThred.start();
		}
	}

	//Serson
	private SensorEventListener mSensorEventListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event) {
			int direction = (int) event.values[0];
			tv_direction.setText(String.valueOf(direction)+"°");
			iv_direction.updateDirection(direction);
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	// 设置指针速度
	public void refreshSpeed(int speed) {
		current_speed = speed;
		// Animation rotateAnimation = new RotateAnimation(0f, toDegrees,
		// Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
		// 0.5f);
		// rotateAnimation.setDuration(3000);
		// needls_01.startAnimation(rotateAnimation);
		float toDegrees = 0;
		Message msg = new Message();
		msg.what = UP_NEEDLS;
		if (old_speed > current_speed) {
			old_speed -= 5;
			if (old_speed <= current_speed) {
				old_speed = current_speed;
			} else {
				mHandler.sendMessageDelayed(msg, 100);
			}
			toDegrees = old_speed - 120;
		} else {
			old_speed += 5;
			if (old_speed >= current_speed) {
				old_speed = current_speed;
			} else {
				mHandler.sendMessageDelayed(msg, 100);
			}
			toDegrees = old_speed - 120;
		}
		needls_01.updateDirection(toDegrees);
		tv_speed.setText(String.valueOf(old_speed));
	}

	public class DaemonThread extends Thread{
		
		public void run() {
			while (getActivity()!=null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message message = Message.obtain(mHandler, UP_DAEMON);
				message.sendToTarget();
				Log.i(TAG,"Fragment_ldcs-->message.sendToTarget()");
			}
		}
	}
	
	public void upDateTextView() {
		tv_time.setText(DateTools.getCurrentDate("HH:mm"));
		String strDate = DateTools.getCurrentDate("dd/MM/yyyy  ")+DateTools.getCurrentWeek();
		tv_date.setText(strDate);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.iv_exit:
			getActivity().getSupportFragmentManager().popBackStack();
			break;
		}
		
	}
	
	@Override
	public void onStop() {
		super.onStop();
		locationManager.removeUpdates(llistener);
	}
	
}
