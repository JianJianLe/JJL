package com.xy.jjl.utils;

import android.os.Handler;
import android.util.Log;

public class MoneyUnit {

	static {
		System.loadLibrary("money_unit");
	}

	private native int jBAdeviceInit();

	private native void jBAdeviceStartRunning();

	private native int jBAstatusFlagCheck();

	private native void jBAstatusFlagClear(int flag);

	private native byte[] jBAdataReceive();

	private native void jBAdataSend(byte[] buffer);

	String TAG = "BillAcceptor";
	int TIME = 50;
	int BA_UART_RW_LENGTH = 3;

	int BA_fd;
	public byte[] BA_receiveData;
	public byte[] BA_sendData;

	public byte[] BA_startCode;
	public byte[] BA_responseCode;
	public byte[] BA_resetCode;
	public byte[] BA_acceptCode;
	public byte[] BA_rejectCode;

	private int flag;
	private int disReadFlag = 0;

	private byte moneyCode;
	public int rcvdMoney;
	
	String printString;
	
	//保持投币器与板子连接的定时器
	Handler BAstatusNormalFlagHandler = new Handler();
	Runnable BAstatusNarmalFlagRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				//Log.i(TAG, "Send data");
				jBAdataSend(BA_startCode);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			BAstatusNormalFlagHandler.postDelayed(this, 1000);
		}
	};

	//接收投币器数据的定时器
	Handler BAstatusFlagCheckHandler = new Handler();
	Runnable BAstatusFlagCheckRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				flag = jBAstatusFlagCheck();

				if (flag == 1) {
					BA_receiveData = jBAdataReceive();
					
					Log.i(TAG, "rxcd DATA:" + BA_receiveData[0] + BA_receiveData[1] + BA_receiveData[2]);

					switch (BA_receiveData[0]) {
					case 0x3e:
						//Log.i(TAG, "Enable BA");
						jBAstatusFlagClear(disReadFlag);
						break;

					case (byte) 0x80:
						if(BA_receiveData[1] == (byte)0x8f) {
							printString = bytes2HexString(BA_receiveData);
							jBAdataSend(BA_responseCode);
							Log.i(TAG, "read Data:" + printString);
							jBAstatusFlagClear(disReadFlag);
						}
						break;

					case 0x5e:
						printString = bytes2HexString(BA_receiveData);
						Log.i(TAG, "BA busy");
						jBAdataSend(BA_resetCode);
						jBAstatusFlagClear(disReadFlag);
						break;

					case (byte) 0x81:
						printString = bytes2HexString(BA_receiveData);
						if (BA_receiveData[1] == (byte)0x8f) {
							moneyCode = BA_receiveData[2];
							rcvdMoney = moneyCheck(moneyCode);

							if (rcvdMoney == -1) {
								Log.i(TAG, "Get Money ERR!");
								break;
							}

							Log.i(TAG, "Get RMB: " + rcvdMoney);
							jBAstatusFlagClear(disReadFlag);
						}
						break;
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			BAstatusFlagCheckHandler.postDelayed(this, TIME);
		}
	};
	
	
	/***********************************************公共方法***********************************************/
	
	//初始化投币器命令
	public void billAcceptorCmdInit() {
		BA_receiveData = new byte[BA_UART_RW_LENGTH];
		BA_sendData = new byte[BA_UART_RW_LENGTH];

		BA_startCode = new byte[BA_UART_RW_LENGTH];
		BA_responseCode = new byte[BA_UART_RW_LENGTH];
		BA_resetCode = new byte[BA_UART_RW_LENGTH];

		BA_acceptCode = new byte[BA_UART_RW_LENGTH];
		BA_rejectCode = new byte[BA_UART_RW_LENGTH];

		initAllBuffer();

		BA_startCode[0] = 0x0c;
		BA_responseCode[0] = 0x02;
		BA_resetCode[0] = 0x30;
		BA_acceptCode[0] = 0x02;
		BA_rejectCode[0] = 0x0f;
	}
	
	//初始化投币器设备
	public void billAcceptorDeviceInit() {
		BA_fd = jBAdeviceInit();
		if (BA_fd <= 0) {
			Log.i(TAG, "BA device init failed!");
		} else {
			Log.i(TAG, "BA device init success!");
		}
		
		jBAdeviceStartRunning();
	}
	
	//启动定时器循环发送连接命令
	public void billAcceptorConnecting() {
		BAstatusNormalFlagHandler.postDelayed(BAstatusNarmalFlagRunnable, 1000);
	}
	
	//启动定时器循环接收flag
	public void billAcceptorDataGet() {
		BAstatusFlagCheckHandler.postDelayed(BAstatusFlagCheckRunnable, TIME);
	}

	/***********************************************私有方法***********************************************/
	
	private void initAllBuffer() {
		int i;

		for (i = 0; i < BA_UART_RW_LENGTH; i++) {
			BA_receiveData[i] = 0;
			BA_sendData[i] = 0;
			BA_startCode[i] = 0;
			BA_responseCode[i] = 0;
			BA_resetCode[i] = 0;
			BA_acceptCode[i] = 0;
			BA_rejectCode[i] = 0;
		}
	}
	
	//接收纸币
	private void moneyAccept() {
		jBAdataSend(BA_acceptCode);
	}
	
	//拒收纸币
	private void moneyReject() {
		jBAdataSend(BA_rejectCode);
	}

	//金额判断
	private int moneyCheck(byte moneyCode) {
		int tMoney = -1;

		switch (moneyCode) {
		
		case 0x40:
			moneyReject();
			break;
		case 0x41:
			moneyAccept();
			tMoney = 5;
			break;
		case 0x42:
			tMoney = 10;
			moneyAccept();
			break;
		case 0x43:
			tMoney = 20;
			moneyAccept();
			break;
			
		case 0x44:
			moneyReject();
			break;

		default:
			tMoney = -1;
			moneyReject();
			break;
		}

		return tMoney;
	}
	
	//byte数组转String
	private static String bytes2HexString(byte[] b) {
		StringBuffer result = new StringBuffer();
		String hex;
		for (int i = 0; i < b.length; i++) {
			hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			result.append(hex.toUpperCase());
		}
		return result.toString();
	}

}
