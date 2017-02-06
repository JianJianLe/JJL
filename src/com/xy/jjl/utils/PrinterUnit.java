package com.xy.jjl.utils;

import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class PrinterUnit {
	String TAG = "PrinterUnit";

	static {
		System.loadLibrary("printer_unit");
	}

	// JNI接口
	public native int jPrinterInit();

	public native void jPrinterDataSend(byte[] buffer, int data_len);

	private int fd;
	private byte[] pDemo;

	private byte[] p_data;
	private byte[] qrData;

	private String MachineNum;

	// 大一号字体：简剪乐-简单，快乐
	private byte[] line1Data = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			0x1d, 0x21, 0x11, (byte) 0xBC, (byte) 0xF2, (byte) 0xBC,
			(byte) 0xF4, (byte) 0xC0, (byte) 0xD6, 0x2D, (byte) 0xBC,
			(byte) 0xF2, (byte) 0xB5, (byte) 0xA5, 0x2C, (byte) 0xBF,
			(byte) 0xEC, (byte) 0xC0, (byte) 0xD6, 0x0a, 0x1d, 0x21, 0x0 };

	// 欢迎光临简剪乐快剪专门店
	private byte[] line2Data = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			(byte) 0xBB, (byte) 0xB6, (byte) 0xD3, (byte) 0xAD, (byte) 0xB9,
			(byte) 0xE2, (byte) 0xC1, (byte) 0xD9, (byte) 0xBC, (byte) 0xF2,
			(byte) 0xBC, (byte) 0xF4, (byte) 0xC0, (byte) 0xD6, (byte) 0xBF,
			(byte) 0xEC, (byte) 0xBC, (byte) 0xF4, (byte) 0xD7, (byte) 0xA8,
			(byte) 0xC3, (byte) 0xC5, (byte) 0xB5, (byte) 0xEA, 0x0a };

	// 大一号字体+居中命令
	private byte[] bigCMD = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01, 0x1d,
			0x21, 0x11 };

	/*
	 * 大一号字体：成人票 价格：xxx元
	 */
	private byte[] lineAdule = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			0x1d, 0x21, 0x11, (byte) 0xB3, (byte) 0xC9, (byte) 0xC8,
			(byte) 0xCB, (byte) 0xC6, (byte) 0xB1, 0x0a };
	private byte[] lineAdultMoney = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			0x1d, 0x21, 0x11, (byte) 0xBC, (byte) 0xDB, (byte) 0xB8,
			(byte) 0xF1, 0x3A, 0x20, 0x31, 0x35, (byte) 0xD4, (byte) 0xAA, 0x0a };

	/*
	 * 大一号字体：儿童票 价格：xxx元
	 */
	private byte[] lineChild = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			0x1d, 0x21, 0x11, (byte) 0xB6, (byte) 0xF9, (byte) 0xCD,
			(byte) 0xAF, (byte) 0xC6, (byte) 0xB1, 0x0a };
	private byte[] lineChildMoney = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			0x1d, 0x21, 0x11, (byte) 0xBC, (byte) 0xDB, (byte) 0xB8,
			(byte) 0xF1, 0x3A, 0x20, 0x31, 0x30, (byte) 0xD4, (byte) 0xAA, 0x0a };

	// 大一号字体：余额：xx元 （第18个字节为金额数）
	private byte[] lineBalanceData = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			0x1d, 0x21, 0x11, (byte) 0xD3, (byte) 0xE0, (byte) 0xB6,
			(byte) 0xEE, 0x3A, 0x20, 0x20, 0x35, (byte) 0xD4, (byte) 0xAA,
			0x0a, 0x1d, 0x21, 0x0 };

	// 设备号：JJLXXXXXXXX
	private byte[] MachineNumberData;

	// 加盟电话：020-81002977
	private byte[] line5Data = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			(byte) 0xBC, (byte) 0xD3, (byte) 0xC3, (byte) 0xCB, (byte) 0xB5,
			(byte) 0xE7, (byte) 0xBB, (byte) 0xB0, 0x3A, 0x20, 0x30, 0x32,
			0x30, 0x2D, 0x38, 0x31, 0x30, 0x30, 0x32, 0x39, 0x37, 0x37, 0x0a };

	// 网址：www.jianjianle.com
	private byte[] line6Data = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			0x77, 0x77, 0x77, 0x2E, 0x6A, 0x69, 0x61, 0x6E, 0x6A, 0x69, 0x61,
			0x6E, 0x6C, 0x65, 0x2E, 0x63, 0x6F, 0x6D, 0x0a };
	
	//分割线
	private byte[] splitLine = {0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01, 
								0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 
								0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 
								0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 
								0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D,
								0x2D, 0x2D, 0x2D, 0x0a};
	
	//儿童票注释1，第一行
	private byte[] childNoteOne_1 = {0x1b, 0x40, 0x1c, 0x26, 
								0x31, (byte) 0xA1, (byte) 0xA2, (byte) 0xB4, (byte) 0xCB, (byte) 0xC6, (byte) 0xBE, (byte) 0xCC, 
								(byte) 0xF5, (byte) 0xCE, (byte) 0xAA, (byte) 0xBC, (byte) 0xF2, (byte) 0xBC, (byte) 0xF4, (byte) 0xC0, 
								(byte) 0xD6, (byte) 0xBF, (byte) 0xEC, (byte) 0xBC, (byte) 0xF4, (byte) 0xD7, (byte) 0xA8, (byte) 0xC3, 
								(byte) 0xC5, (byte) 0xB5, (byte) 0xEA, (byte) 0xB6, (byte) 0xF9, (byte) 0xCD, (byte) 0xAF, 0x0a};
	//儿童票注释1，第二行
	private byte[] childNoteOne_2 = {0x1b, 0x40, 0x1c, 0x26, 
								0x20, 0x20, 0x20,  0x28, (byte) 0xC9, (byte) 0xED, (byte) 0xB8, 
								(byte) 0xDF, 0x31, 0x2E, 0x34, (byte) 0xC3, (byte) 0xD7, (byte) 0xD2, (byte) 0xD4, (byte) 0xCF, 
								(byte) 0xC2, 0x29, (byte) 0xBC, (byte) 0xF4, (byte) 0xB7, (byte) 0xA2, (byte) 0xD7, (byte) 0xA8, 
								(byte) 0xD3, (byte) 0xC3, (byte) 0xC6, (byte) 0xBE, (byte) 0xD6, (byte) 0xA4, (byte) 0x3B, 0x0a};
	
	//成人票注释1， 第一行
	private byte[] adultNoteOne_1 = {0x1b, 0x40, 0x1c, 0x26,
									0x31, (byte) 0xA1, (byte) 0xA2, (byte) 0xB4, (byte) 0xCB, (byte) 0xC6, (byte) 0xBE, (byte) 0xCC, 
									(byte) 0xF5, (byte) 0xCE, (byte) 0xAA, (byte) 0xBC, (byte) 0xF2, (byte) 0xBC, (byte) 0xF4, (byte) 0xC0, 
									(byte) 0xD6, (byte) (byte) 0xBF, (byte) 0xEC, (byte) 0xBC, (byte) 0xF4, (byte) 0xD7, (byte) 0xA8, 
									(byte) 0xC3, (byte) 0xC5, (byte) 0xB5, (byte) 0xEA, (byte) 0xB3, (byte) 0xC9, (byte) 0xC8, (byte) 0xCB, 0x0a};
	
	//成人票注释1， 第二行
	private byte[] adultNoteOne_2 = {0x1b, 0x40, 0x1c, 0x26,
									0x20, 0x20, 0x20, 0x28, (byte) 0xC9, (byte) 0xED, (byte) 0xB8, (byte) 0xDF, 0x31, 0x2E, 0x34, (byte) 0xC3, 
									(byte) 0xD7, (byte) 0xD2, (byte) 0xD4, (byte) 0xC9, (byte) 0xCF, (byte) 0xBA, (byte) 0xAC, 0x31, 0x2E, 0x34, 
									(byte) 0xC3, (byte) 0xD7, 0x29, (byte) 0xBC, (byte) 0xF4, (byte) 0xB7, (byte) 0xA2, 0x0a};
		
	//成人票注释1， 第三行
	private byte[] adultNoteOne_3 = {0x1b, 0x40, 0x1c, 0x26,
									0x20, 0x20, 0x20, (byte) 0xD7, (byte) 0xA8, (byte) 0xD3, (byte) 0xC3, (byte) 0xC6, (byte) 0xBE, (byte) 0xD6, 
									(byte) 0xA4, 0x3B, 0x0a};
	
	
	
	//注释2，第一行
	private byte[] noteTwo_1 = {0x1b, 0x40, 0x1c, 0x26, 
								0x32, (byte) 0xA1, (byte) 0xA2, (byte) 0xC6, (byte) 0xBE, (byte) 0xB4, (byte) 0xCB, (byte) 0xC6, 
								(byte) 0xBE, (byte) 0xCC, (byte) 0xF5, (byte) 0xBF, (byte) 0xC9, (byte) 0xD2, (byte) 0xD4, (byte) 0xD4, 
								(byte) 0xDA, (byte) 0xBC, (byte) 0xF2, (byte) 0xBC, (byte) 0xF4, (byte) 0xC0, (byte) 0xD6, (byte) 0xBF, 
								(byte) 0xEC, (byte) 0xBC, (byte) 0xF4, (byte) 0xD7, (byte) 0xA8, (byte) 0xC3, (byte) 0xC5, 0x0a};
	//注释2，第二行
	private byte[] noteTwo_2 = {0x1b, 0x40, 0x1c, 0x26, 
								0x20, 0x20, 0x20, (byte) 0xB5, (byte) 0xEA, (byte) 0xCF, (byte) 0xED, (byte) 0xCA, (byte) 0xDC, (byte) 0xD7, 
								(byte) 0xA8, (byte) 0xD2, (byte) 0xB5, (byte) 0xBC, (byte) 0xF4, (byte) 0xB7, (byte) 0xA2, (byte) 0xD2, (byte) 0xBB, 
								(byte) 0xB4, (byte) 0xCE, (byte) 0xA3, (byte) 0xAC, (byte) 0xB8, (byte) 0xB4, (byte) 0xD3, (byte) 0xA1, (byte) 0xCE, 
								(byte) 0xDE, (byte) 0xD0, (byte) 0xA7, (byte) 0x3B, 0x0a};
	//注释3
	private byte[] noteThree = {0x1b, 0x40, 0x1c, 0x26,
								0x33, (byte) 0xA1, (byte) 0xA2, (byte) 0xB1, (byte) 0xBE, (byte) 0xC6, (byte) 0xBE, (byte) 0xCC, (byte) 0xF5, 
								(byte) 0xBD, (byte) 0xF6, (byte) 0xBF, (byte) 0xC9, (byte) 0xD4, (byte) 0xDA, (byte) 0xB9, (byte) 0xBA, (byte) 0xC2, 
								(byte) 0xF2, (byte) 0xB7, (byte) 0xD6, (byte) 0xB5, (byte) 0xEA, (byte) 0xCA, (byte) 0xB9, (byte) 0xD3, (byte) 0xC3, 
								0x3B, 0x0a};
	//注释4，第一行
	private byte[] noteFour_1 = {0x1b, 0x40, 0x1c, 0x26,
								0x34, (byte) 0xA1, (byte) 0xA2, (byte) 0xB4, (byte) 0xCB, (byte) 0xC6, (byte) 0xBE, (byte) 0xCC, (byte) 0xF5, 
								(byte) 0xB2, (byte) 0xBB, (byte) 0xBC, (byte) 0xC7, (byte) 0xC3, (byte) 0xFB, (byte) 0xA1, (byte) 0xA2, (byte) 0xB2, 
								(byte) 0xBB, (byte) 0xB9, (byte) 0xD2, (byte) 0xCA, (byte) 0xA7, (byte) 0xA1, (byte) 0xA2, (byte) 0xB2, (byte) 0xBB, 
								(byte) 0xC4, (byte) 0xDC, (byte) 0xB6, (byte) 0xD2, 0x0a};
	//注释4，第二行
	private byte[] noteFour_2 = {0x1b, 0x40, 0x1c, 0x26,
							  	0x20, 0x20, 0x20, (byte) 0xBB, (byte) 0xBB, (byte) 0xCF, (byte) 0xD6, (byte) 0xBD, (byte) 0xF0, (byte) 0xA3, (byte) 0xAC, 
							  	(byte) 0xD3, (byte) 0xC3, (byte) 0xCD, (byte) 0xEA, (byte) 0xBC, (byte) 0xB4, (byte) 0xD6, (byte) 0xB9, 0x3B, 0x0a};
	
	//注释5，第一行
	private byte[] noteFive_1 = {0x1b, 0x40, 0x1c, 0x26,
								0x35, (byte) 0xA1, (byte) 0xA2, (byte) 0xB4, (byte) 0xCB, (byte) 0xC6, (byte) 0xBE, (byte) 0xCC, (byte) 0xF5, (byte) 0xB4, 
								(byte) 0xD3, (byte) 0xB9, (byte) 0xBA, (byte) 0xC2, (byte) 0xF2, (byte) 0xD6, (byte) 0xAE, (byte) 0xC8, (byte) 0xD5, (byte) 0xC6, 
								(byte) 0xF0, (byte) 0xA3, (byte) 0xAC, (byte) 0xD3, (byte) 0xD0, (byte) 0xD0, (byte) 0xA7, (byte) 0xC6, (byte) 0xDA, (byte) 0xCE, 
								(byte) 0xAA, 0x0a};
	//注释5，第二行
	private byte[] noteFive_2 = {0x1b, 0x40, 0x1c, 0x26,
								0x20, 0x20, 0x20, 0x37, (byte) 0xCC, (byte) 0xEC, (byte) 0xA3, (byte) 0xAC, (byte) 0xB9, (byte) 0xFD, (byte) 0xC6, 
								(byte) 0xDA, (byte) 0xD7, (byte) 0xF7, (byte) 0xB7, (byte) 0xCF, 0x3B, 0x0a};
	
	//注释6，第一行
	private byte[] noteSix_1 = {0x1b, 0x40, 0x1c, 0x26,
								0x36, (byte) 0xA1, (byte) 0xA2, (byte) 0xBC, (byte) 0xF2, (byte) 0xBC, (byte) 0xF4, (byte) 0xC0, (byte) 0xD6, 
								(byte) 0xBF, (byte) 0xC9, (byte) 0xC4, (byte) 0xDC, (byte) 0xD4, (byte) 0xDA, (byte) 0xB7, (byte) 0xA8, (byte) 0xC2, 
								(byte) 0xC9, (byte) 0xD4, (byte) 0xCA, (byte) 0xD0, (byte) 0xED, (byte) 0xB7, (byte) 0xB6, (byte) 0xCE, (byte) 0xA7, 
								(byte) 0xC4, (byte) 0xDA, (byte) 0xB6, (byte) 0xD4, 0x0a};
	//注释6，第二行
	private byte[] noteSix_2 = {0x1b, 0x40, 0x1c, 0x26,
								0x20, 0x20, 0x20, (byte) 0xB4, (byte) 0xCB, (byte) 0xCF, (byte) 0xB8, (byte) 0xD4, (byte) 0xF2, (byte) 0xD7, 
								(byte) 0xF7, (byte) 0xB3, (byte) 0xF6, (byte) 0xCA, (byte) 0xCA, (byte) 0xB5, (byte) 0xB1, (byte) 0xB5, 
								(byte) 0xF7, (byte) 0xD5, (byte) 0xFB, (byte) 0xA1, (byte) 0xA3, 0x0a};

	//打印机命令
	private byte[] initPrinterCMD = { 0x1b, 0x40 };
	private byte[] printDataCMD = { 0x0a };
	private byte[] lineSizeZeroCMD = { 0x1b, 0x33, 0x00 };
	private byte[] lineSizeDefCMD = { 0x1b, 0x32 };
	private byte[] fountSizeUpCMD = { 0x1d, 0x21, 0x1 };
	private byte[] fountSizeDefCMD = { 0x1d, 0x21, 0x0 };
	private byte[] cutPaperCMD = { 0x1d, 0x56, 0x42, 0x00 };

	//位图对象
	private Bitmap compressPic = null;

	/************************************************ 外部接口 **********************************************/

	//初始化打印机
	public void PrinterInit(String machineNum) {
		int i = 0;
		int j;
		
		qrData = new byte[8811];
		MachineNum = machineNum;
		
		byte[] mMachineNumberData = getGbk(MachineNum);
		
		MachineNumberData = new byte[mMachineNumberData.length + 8];
		
		//0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,    0x0a
		
		MachineNumberData[i++] = 0x1b;
		MachineNumberData[i++] = 0x40;
		MachineNumberData[i++] = 0x1c;
		MachineNumberData[i++] = 0x26;
		MachineNumberData[i++] = 0x1b;
		MachineNumberData[i++] = 0x61;
		MachineNumberData[i++] = 0x01;
		
		for(j = 0; j < mMachineNumberData.length; j++) {
			MachineNumberData[i + j] = mMachineNumberData[j];
		}
		
		MachineNumberData[mMachineNumberData.length + 7] = 0x0a;
		
		
		fd = jPrinterInit();
		if (fd < 0) {
			Log.i(TAG, "Device init ERR!");
		}
	}

	// 创建二维码
	public void qrCodeCreate(final String QrData, final String QrImgPath) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				boolean success = QRCodeUtil.createQRImage(QrData, 264, 264,
//						null, QrImgPath);
//
//				if (success) {
//					Log.i("Test", "Success!");
//				}
//			}
//		}).start();
		
		
		boolean success = QRCodeUtil.createQRImage(QrData, 264, 264,
				null, QrImgPath);

		if (success) {
			Log.i("Test", "Success!");
		}
		
		
	}

	//打印小票
	public void PrintTicket(int Money, String Order, InputStream QrData) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd    hh:mm:ss");
		String timeData = sDateFormat.format(new java.util.Date());
		byte[] TimeData = getGbk(timeData);

		titlePrint();

		switch (Money) {
		case 5:
			balanceTicketPrint();
			machineInformationPrint(TimeData, Money);
			break;
			
		case 10:
			childTicketPrint();
			orderPrint(Order);
			qrCodePrint(QrData);
			machineInformationPrint(TimeData, Money);
			break;

		case 15:
			adultTicketPrint();
			orderPrint(Order);
			qrCodePrint(QrData);
			machineInformationPrint(TimeData, Money);
			break;

		default:
			break;

		}

	}

	//进行MD5加密
	public final static char[] Md5encrypt(String plaintext) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = plaintext.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			
			return str;
		} catch (Exception e) {
			return null;
		}
	}

	/************************************************ 内部接口 **********************************************/

	/*
	 * 打印开头： 简剪乐-简单，快乐 欢迎光临简剪乐快剪专门店
	 */
	private void titlePrint() {
		jPrinterDataSend(initPrinterCMD, initPrinterCMD.length);
		jPrinterDataSend(line1Data, line1Data.length);
		jPrinterDataSend(line2Data, line2Data.length);
	}

	/*
	 * 成人票 
	 * 价格：xx元
	 */
	private void adultTicketPrint() {
		jPrinterDataSend(lineAdule, lineAdule.length);
		jPrinterDataSend(lineAdultMoney, lineAdultMoney.length);
	}

	/*
	 * 儿童票 
	 * 价格：xx元
	 */
	private void childTicketPrint() {
		jPrinterDataSend(lineChild, lineChild.length);
		jPrinterDataSend(lineChildMoney, lineChildMoney.length);
	}
	
	/*
	 * 余额票
	 * 价格 xx元
	 * 
	 * */
	private void balanceTicketPrint() {
		jPrinterDataSend(lineBalanceData, lineBalanceData.length);
	}

	// 打印序号
	private void orderPrint(String order) {

		int i;
		int j;

		byte[] orderData = new byte[14];

		byte[] orderDataSource = getGbk(order);

		for (i = 0; i < 10; i++) {
			orderData[i] = bigCMD[i];
		}

		for (j = 0; j < 3; j++) {
			orderData[i++] = orderDataSource[j];
		}

		orderData[13] = 0xa;

		jPrinterDataSend(orderData, orderData.length);
	}

	// 打印二维码
	private void qrCodePrint(InputStream QrData) {

		jPrinterDataSend(lineSizeZeroCMD, lineSizeZeroCMD.length);

		Bitmap mBitmap = BitmapFactory.decodeStream(QrData);

		compressPic = compressPic(mBitmap);

		qrData = draw2PxPoint(compressPic);

		jPrinterDataSend(qrData, qrData.length);

	}

	/*
	 * 设备号：xxxxxx 时间 加盟电话：xxxxx 网址
	 */
	private void machineInformationPrint(byte[] timeData, int money) {
		//jPrinterDataSend(line4Data, line4Data.length);
		
		if(money == 5) {
			jPrinterDataSend(printDataCMD, printDataCMD.length);
		}
		jPrinterDataSend(MachineNumberData, MachineNumberData.length);

		jPrinterDataSend(timeData, timeData.length);

		jPrinterDataSend(printDataCMD, printDataCMD.length);

		//jPrinterDataSend(printDataCMD, printDataCMD.length);

		notePrint(money);
		
		jPrinterDataSend(printDataCMD, printDataCMD.length);
		
		jPrinterDataSend(line5Data, line5Data.length);

		jPrinterDataSend(line6Data, line6Data.length);

		jPrinterDataSend(printDataCMD, printDataCMD.length);
		jPrinterDataSend(printDataCMD, printDataCMD.length);

		jPrinterDataSend(cutPaperCMD, cutPaperCMD.length);
	}
	
	/*
	 * 打印注意信息
	 * 
	 * */
	
	private void notePrint(int money) {
		
		if(money == 10) {
			jPrinterDataSend(splitLine, splitLine.length);
			jPrinterDataSend(childNoteOne_1, childNoteOne_1.length);
			jPrinterDataSend(childNoteOne_2, childNoteOne_2.length);
			jPrinterDataSend(noteTwo_1, noteTwo_1.length);
			jPrinterDataSend(noteTwo_2, noteTwo_2.length);
			jPrinterDataSend(noteThree, noteThree.length);
			jPrinterDataSend(noteFour_1, noteFour_1.length);
			jPrinterDataSend(noteFour_2, noteFour_2.length);
			jPrinterDataSend(noteFive_1, noteFive_1.length);
			jPrinterDataSend(noteFive_2, noteFive_2.length);
			jPrinterDataSend(noteSix_1, noteSix_1.length);
			jPrinterDataSend(noteSix_2, noteSix_2.length);
		}
		
		if(money == 15) {
			jPrinterDataSend(splitLine, splitLine.length);
			jPrinterDataSend(adultNoteOne_1, adultNoteOne_1.length);
			jPrinterDataSend(adultNoteOne_2, adultNoteOne_2.length);
			jPrinterDataSend(adultNoteOne_3, adultNoteOne_3.length);
			jPrinterDataSend(noteTwo_1, noteTwo_1.length);
			jPrinterDataSend(noteTwo_2, noteTwo_2.length);
			jPrinterDataSend(noteThree, noteThree.length);
			jPrinterDataSend(noteFour_1, noteFour_1.length);
			jPrinterDataSend(noteFour_2, noteFour_2.length);
			jPrinterDataSend(noteFive_1, noteFive_1.length);
			jPrinterDataSend(noteFive_2, noteFive_2.length);
			jPrinterDataSend(noteSix_1, noteSix_1.length);
			jPrinterDataSend(noteSix_2, noteSix_2.length);
		}

	}

	/**
	 * 把一张Bitmap图片转化为打印机可以打印的bit(将图片压缩为360*360) 效率很高（相对于下面）
	 * 
	 * @param bit
	 * @return
	 */
	public static byte[] draw2PxPoint(Bitmap bit) {
		byte[] data = new byte[8811];
		int k = 0;
		for (int j = 0; j < 11; j++) {
			// 打印居中指令
			data[k++] = 0x1b;
			data[k++] = 0x61;
			data[k++] = 0x01;

			// 位图打印指令
			data[k++] = 0x1B;
			data[k++] = 0x2A;
			data[k++] = 0x21; // hex:0x21 = dec:33 m=33时，选择24点双密度打印，分辨率达到200DPI。
			data[k++] = 0x08;
			data[k++] = 0x01;

			for (int i = 0; i < 264; i++) {
				for (int m = 0; m < 3; m++) {
					for (int n = 0; n < 8; n++) {
						byte b = px2Byte(i, j * 24 + m * 8 + n, bit);
						data[k] += data[k] + b;
					}
					k++;
				}
			}
			data[k++] = 10;
		}
		return data;
	}

	/**
	 * 图片二值化，黑色是1，白色是0
	 * 
	 * @param x
	 *            横坐标
	 * @param y
	 *            纵坐标
	 * @param bit
	 *            位图
	 * @return
	 */
	public static byte px2Byte(int x, int y, Bitmap bit) {
		byte b;
		int pixel = bit.getPixel(x, y);
		int red = (pixel & 0x00ff0000) >> 16; // 取高两位
		int green = (pixel & 0x0000ff00) >> 8; // 取中两位
		int blue = pixel & 0x000000ff; // 取低两位
		int gray = RGB2Gray(red, green, blue);
		if (gray < 128) {
			b = 1;
		} else {
			b = 0;
		}
		return b;
	}

	/**
	 * 图片灰度的转化
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	private static int RGB2Gray(int r, int g, int b) {
		int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b); // 灰度转化公式
		return gray;
	}

	/**
	 * 对图片进行压缩（去除透明度）
	 * 
	 * @param bitmapOrg
	 */
	public static Bitmap compressPic(Bitmap bitmapOrg) {
		// 获取这个图片的宽和高
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
		// 定义预转换成的图片的宽度和高度
		int newWidth = 264;
		int newHeight = 264;
		Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight,
				Bitmap.Config.ARGB_8888);
		Canvas targetCanvas = new Canvas(targetBmp);
		targetCanvas.drawColor(0xffffffff);
		targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height),
				new Rect(0, 0, newWidth, newHeight), null);
		return targetBmp;
	}

	public static String bytes2HexString(byte[] b) {
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

	public static byte[] getGbk(String stText) {
		byte[] returnText = null;
		try {
			returnText = stText.getBytes("GBK"); // 必须放在try内才可以
		} catch (Exception ex) {
			;
		}
		return returnText;
	}

}
