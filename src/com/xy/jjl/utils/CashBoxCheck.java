package com.xy.jjl.utils;

import android.util.Log;

public class CashBoxCheck {
	
	String TAG = "CashBoxCheck";

	static {
		System.loadLibrary("cashbox_unit");
	}
	
	private native int jCashBoxDeviceInit();
	
	private native int jCashBoxStatusGet();
	
	int BoxStatus;
	
	public void CashBoxDeviceInit() {
		int fd = jCashBoxDeviceInit();
		
		if(fd < 0) {
			Log.i(TAG, "CashBox init failed!");
		}
	}
	
	public int CashBoxStatusGet() {
		BoxStatus = jCashBoxStatusGet();
		
		return BoxStatus;
	}
	
}
