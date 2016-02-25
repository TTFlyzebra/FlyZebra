package com.flyzebra.test.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class GetMobileStateTools {
	/**
	 * @param context
	 * @return 1ΪWIFO����2ΪSIM������0Ϊ�Ͽ�
	 */
	public static int getNetworkConnection(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		if (activeInfo != null && activeInfo.isConnected()) {
			boolean wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
			boolean mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
			if (wifiConnected) {
				return 1;
			} else if (mobileConnected) {
				return 2;
			}
		} 
		return 0;
	}

}
