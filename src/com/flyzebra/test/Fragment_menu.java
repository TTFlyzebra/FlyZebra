package com.flyzebra.test;

import com.flyzebra.test.tools.DateTools;
import com.flyzebra.test.tools.GetMobileStateTools;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Fragment_menu extends Fragment implements OnClickListener {
	public final String TAG = "com.flyzebra";

	@SuppressLint("HandlerLeak")
	public class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if (getActivity() != null) {
				switch (msg.what) {
				case UP_DAEMON:
					upDateTextView();
					upDateStateIcon();
					break;
				}
			}
		}
	}

	private final int UP_DAEMON = 12;
	private MyHandler mHandler = new MyHandler();
	private ImageView menu_line01;
	private ImageView menu_line02;
	private ImageView menu_line03;
	private ImageView menu_line04;
	private ImageView menu_line05;
	private ImageView menu_line06;
	private ImageView menu_line07;
	private ImageView menu_line08;
	private ImageView iv_icon;
	private TextView tv_topdate;

	// private List<ImageView> line_list= new ArrayList<ImageView>();

	private ImageView menu_iv_click_01;
	private ImageView menu_iv_click_02;
	private ImageView menu_iv_click_03;
	private ImageView menu_iv_click_04;
	private ImageView menu_iv_click_05;
	private ImageView menu_iv_click_06;
	private ImageView menu_iv_click_07;
	private ImageView menu_iv_click_08;

	private int select_line = -1;

	private DaemonThread mDaemonThred;
	
	//×´Ì¬Í¼±ê¹ÜÀí
	private int usb_state=0;
	private LinearLayout ll_add_state_iv;

	public Fragment_menu() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_menu, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		menu_line01 = (ImageView) view.findViewById(R.id.menu_line01);
		menu_line02 = (ImageView) view.findViewById(R.id.menu_line02);
		menu_line03 = (ImageView) view.findViewById(R.id.menu_line03);
		menu_line04 = (ImageView) view.findViewById(R.id.menu_line04);
		menu_line05 = (ImageView) view.findViewById(R.id.menu_line05);
		menu_line06 = (ImageView) view.findViewById(R.id.menu_line06);
		menu_line07 = (ImageView) view.findViewById(R.id.menu_line07);
		menu_line08 = (ImageView) view.findViewById(R.id.menu_line08);

		iv_icon = (ImageView) view.findViewById(R.id.iv_icon);

		// line_list.add( (ImageView) view.findViewById(R.id.menu_line01));
		// line_list.add( (ImageView) view.findViewById(R.id.menu_line02));
		// line_list.add( (ImageView) view.findViewById(R.id.menu_line03));
		// line_list.add( (ImageView) view.findViewById(R.id.menu_line04));
		// line_list.add( (ImageView) view.findViewById(R.id.menu_line05));
		// line_list.add( (ImageView) view.findViewById(R.id.menu_line06));
		// line_list.add( (ImageView) view.findViewById(R.id.menu_line07));
		// line_list.add( (ImageView) view.findViewById(R.id.menu_line08));
		menu_line01.setVisibility(View.GONE);
		menu_line02.setVisibility(View.GONE);
		menu_line03.setVisibility(View.GONE);
		menu_line04.setVisibility(View.GONE);
		menu_line05.setVisibility(View.GONE);
		menu_line06.setVisibility(View.GONE);
		menu_line07.setVisibility(View.GONE);
		menu_line08.setVisibility(View.GONE);
		menu_iv_click_01 = (ImageView) view.findViewById(R.id.imageView1);
		menu_iv_click_02 = (ImageView) view.findViewById(R.id.imageView2);
		menu_iv_click_03 = (ImageView) view.findViewById(R.id.imageView3);
		menu_iv_click_04 = (ImageView) view.findViewById(R.id.imageView4);
		menu_iv_click_05 = (ImageView) view.findViewById(R.id.imageView5);
		menu_iv_click_06 = (ImageView) view.findViewById(R.id.imageView6);
		menu_iv_click_07 = (ImageView) view.findViewById(R.id.imageView7);
		menu_iv_click_08 = (ImageView) view.findViewById(R.id.imageView8);

		menu_iv_click_01.setOnClickListener(this);
		menu_iv_click_02.setOnClickListener(this);
		menu_iv_click_03.setOnClickListener(this);
		menu_iv_click_04.setOnClickListener(this);
		menu_iv_click_05.setOnClickListener(this);
		menu_iv_click_06.setOnClickListener(this);
		menu_iv_click_07.setOnClickListener(this);
		menu_iv_click_08.setOnClickListener(this);
		tv_topdate = (TextView) view.findViewById(R.id.tv_topdate);
		ll_add_state_iv = (LinearLayout) view.findViewById(R.id.ll_add_state_iv);
	}

	@Override
	public void onResume() {
		super.onResume();
		showLine(select_line);
		if (mDaemonThred == null) {
			mDaemonThred = new DaemonThread();
			mDaemonThred.setDaemon(true);
			mDaemonThred.start();
		}
		//×¢²áUSB¼àÌý¹ã²¥
		IntentFilter mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		getActivity().registerReceiver(BtStatusReceiver, mIntentFilter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView1:
			select_line = 1;
			showLine(select_line);
			break;
		case R.id.imageView2:
			select_line = 2;
			showLine(select_line);
			break;
		case R.id.imageView3:
			Fragment_ldcs fragment = new Fragment_ldcs();
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.container, fragment);
			ft.addToBackStack("Fragment_ldcs");
			ft.commit();
			select_line = 3;
			showLine(select_line);
			break;
		case R.id.imageView4:
			select_line = 4;
			showLine(select_line);
			break;
		case R.id.imageView5:
			select_line = 5;
			showLine(select_line);
			break;
		case R.id.imageView6:
			select_line = 6;
			showLine(select_line);
			break;
		case R.id.imageView7:
			select_line = 7;
			showLine(select_line);
			break;
		case R.id.imageView8:
			select_line = 8;
			showLine(select_line);
			break;
		}
	}

	private void showLine(int select) {
		menu_line01.setVisibility(View.GONE);
		menu_line02.setVisibility(View.GONE);
		menu_line03.setVisibility(View.GONE);
		menu_line04.setVisibility(View.GONE);
		menu_line05.setVisibility(View.GONE);
		menu_line06.setVisibility(View.GONE);
		menu_line07.setVisibility(View.GONE);
		menu_line08.setVisibility(View.GONE);
		switch (select) {
		case 1:
			menu_line01.setVisibility(View.VISIBLE);
			iv_icon.setImageResource(R.drawable.icon_1);
			break;
		case 2:
			menu_line02.setVisibility(View.VISIBLE);
			iv_icon.setImageResource(R.drawable.icon_2);
			break;
		case 3:
			menu_line03.setVisibility(View.VISIBLE);
			iv_icon.setImageResource(R.drawable.icon_3);
			break;
		case 4:
			menu_line04.setVisibility(View.VISIBLE);
			iv_icon.setImageResource(R.drawable.icon_4);
			break;
		case 5:
			menu_line05.setVisibility(View.VISIBLE);
			iv_icon.setImageResource(R.drawable.icon_5);
			break;
		case 6:
			menu_line06.setVisibility(View.VISIBLE);
			iv_icon.setImageResource(R.drawable.icon_6);
			break;
		case 7:
			menu_line07.setVisibility(View.VISIBLE);
			iv_icon.setImageResource(R.drawable.icon_7);
			break;
		case 8:
			menu_line08.setVisibility(View.VISIBLE);
			break;
		}
	}

	private class DaemonThread extends Thread {
		public void run() {
			while (getActivity()!=null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message message = Message.obtain(mHandler, UP_DAEMON);
				message.sendToTarget();
				Log.i(TAG, "Fragment_menu-->message.sendToTarget()");
			}
		}
	}

	public void upDateTextView() {
		String strDate = DateTools.getCurrentDate("HH:mm  ")
				+ DateTools.getCurrentDate("dd/MM/yyyy  ")
				+ DateTools.getCurrentWeek();
		tv_topdate.setText(strDate);
	}	
	
	public void upDateStateIcon() {
		ll_add_state_iv.removeAllViews();
		if(usb_state!=0){
			addImageView(R.drawable.icon_1);
		}
		int connect = GetMobileStateTools.getNetworkConnection(getActivity());
		if(connect ==1){
			addImageView(R.drawable.icon_2);
		}else if (connect==2){
			addImageView(R.drawable.icon_3);
		}
	}
	
	public void addImageView(int resId){
		ImageView iv = new ImageView(getActivity());
		LinearLayout.LayoutParams LP_WW = new LinearLayout.LayoutParams(30, 30);
		iv.setLayoutParams(LP_WW);
		iv.setImageResource(resId);
		ll_add_state_iv.addView(iv);
	}
	
	 
	public BroadcastReceiver BtStatusReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
				usb_state = intent.getIntExtra("plugged", 0);
				switch(usb_state){
				case 0://¶Ï¿ª
					break;
				case 1://USB
					break;
				case 2://³äµç
					break;
				}
			}
		}
	};
}
