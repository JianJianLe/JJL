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

	// JNI�ӿ�
	public native int jPrinterInit();

	public native void jPrinterDataSend(byte[] buffer, int data_len);

	private int fd;
	private byte[] pDemo;

	private byte[] p_data;
	private byte[] qrData;

	private String MachineNum;

	// ��һ�����壺�����-�򵥣�����
	private byte[] line1Data = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			0x1d, 0x21, 0x11, (byte) 0xBC, (byte) 0xF2, (byte) 0xBC,
			(byte) 0xF4, (byte) 0xC0, (byte) 0xD6, 0x2D, (byte) 0xBC,
			(byte) 0xF2, (byte) 0xB5, (byte) 0xA5, 0x2C, (byte) 0xBF,
			(byte) 0xEC, (byte) 0xC0, (byte) 0xD6, 0x0a, 0x1d, 0x21, 0x0 };

	// ��ӭ���ټ���ֿ��ר�ŵ�
	private byte[] line2Data = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			(byte) 0xBB, (byte) 0xB6, (byte) 0xD3, (byte) 0xAD, (byte) 0xB9,
			(byte) 0xE2, (byte) 0xC1, (byte) 0xD9, (byte) 0xBC, (byte) 0xF2,
			(byte) 0xBC, (byte) 0xF4, (byte) 0xC0, (byte) 0xD6, (byte) 0xBF,
			(byte) 0xEC, (byte) 0xBC, (byte) 0xF4, (byte) 0xD7, (byte) 0xA8,
			(byte) 0xC3, (byte) 0xC5, (byte) 0xB5, (byte) 0xEA, 0x0a };

	// ��һ������+��������
	private byte[] bigCMD = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01, 0x1d,
			0x21, 0x11 };

	/*
	 * ��һ�����壺����Ʊ �۸�xxxԪ
	 */
	private byte[] lineAdule = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			0x1d, 0x21, 0x11, (byte) 0xB3, (byte) 0xC9, (byte) 0xC8,
			(byte) 0xCB, (byte) 0xC6, (byte) 0xB1, 0x0a };
	private byte[] lineAdultMoney = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			0x1d, 0x21, 0x11, (byte) 0xBC, (byte) 0xDB, (byte) 0xB8,
			(byte) 0xF1, 0x3A, 0x20, 0x31, 0x35, (byte) 0xD4, (byte) 0xAA, 0x0a };

	/*
	 * ��һ�����壺��ͯƱ �۸�xxxԪ
	 */
	private byte[] lineChild = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			0x1d, 0x21, 0x11, (byte) 0xB6, (byte) 0xF9, (byte) 0xCD,
			(byte) 0xAF, (byte) 0xC6, (byte) 0xB1, 0x0a };
	private byte[] lineChildMoney = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			0x1d, 0x21, 0x11, (byte) 0xBC, (byte) 0xDB, (byte) 0xB8,
			(byte) 0xF1, 0x3A, 0x20, 0x31, 0x30, (byte) 0xD4, (byte) 0xAA, 0x0a };

	// ��һ�����壺��xxԪ ����18���ֽ�Ϊ�������
	private byte[] lineBalanceData = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			0x1d, 0x21, 0x11, (byte) 0xD3, (byte) 0xE0, (byte) 0xB6,
			(byte) 0xEE, 0x3A, 0x20, 0x20, 0x35, (byte) 0xD4, (byte) 0xAA,
			0x0a, 0x1d, 0x21, 0x0 };

	// �豸�ţ�JJLXXXXXXXX
	private byte[] MachineNumberData;

	// ���˵绰��020-81002977
	private byte[] line5Data = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			(byte) 0xBC, (byte) 0xD3, (byte) 0xC3, (byte) 0xCB, (byte) 0xB5,
			(byte) 0xE7, (byte) 0xBB, (byte) 0xB0, 0x3A, 0x20, 0x30, 0x32,
			0x30, 0x2D, 0x38, 0x31, 0x30, 0x30, 0x32, 0x39, 0x37, 0x37, 0x0a };

	// ��ַ��www.jianjianle.com
	private byte[] line6Data = { 0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01,
			0x77, 0x77, 0x77, 0x2E, 0x6A, 0x69, 0x61, 0x6E, 0x6A, 0x69, 0x61,
			0x6E, 0x6C, 0x65, 0x2E, 0x63, 0x6F, 0x6D, 0x0a };
	
	//�ָ���
	private byte[] splitLine = {0x1b, 0x40, 0x1c, 0x26, 0x1b, 0x61, 0x01, 
								0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 
								0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 
								0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 
								0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D, 0x2D,
								0x2D, 0x2D, 0x2D, 0x0a};
	
	//��ͯƱע��1����һ��
	private byte[] childNoteOne_1 = {0x1b, 0x40, 0x1c, 0x26, 
								0x31, (byte) 0xA1, (byte) 0xA2, (byte) 0xB4, (byte) 0xCB, (byte) 0xC6, (byte) 0xBE, (byte) 0xCC, 
								(byte) 0xF5, (byte) 0xCE, (byte) 0xAA, (byte) 0xBC, (byte) 0xF2, (byte) 0xBC, (byte) 0xF4, (byte) 0xC0, 
								(byte) 0xD6, (byte) 0xBF, (byte) 0xEC, (byte) 0xBC, (byte) 0xF4, (byte) 0xD7, (byte) 0xA8, (byte) 0xC3, 
								(byte) 0xC5, (byte) 0xB5, (byte) 0xEA, (byte) 0xB6, (byte) 0xF9, (byte) 0xCD, (byte) 0xAF, 0x0a};
	//��ͯƱע��1���ڶ���
	private byte[] childNoteOne_2 = {0x1b, 0x40, 0x1c, 0x26, 
								0x20, 0x20, 0x20,  0x28, (byte) 0xC9, (byte) 0xED, (byte) 0xB8, 
								(byte) 0xDF, 0x31, 0x2E, 0x34, (byte) 0xC3, (byte) 0xD7, (byte) 0xD2, (byte) 0xD4, (byte) 0xCF, 
								(byte) 0xC2, 0x29, (byte) 0xBC, (byte) 0xF4, (byte) 0xB7, (byte) 0xA2, (byte) 0xD7, (byte) 0xA8, 
								(byte) 0xD3, (byte) 0xC3, (byte) 0xC6, (byte) 0xBE, (byte) 0xD6, (byte) 0xA4, (byte) 0x3B, 0x0a};
	
	//����Ʊע��1�� ��һ��
	private byte[] adultNoteOne_1 = {0x1b, 0x40, 0x1c, 0x26,
									0x31, (byte) 0xA1, (byte) 0xA2, (byte) 0xB4, (byte) 0xCB, (byte) 0xC6, (byte) 0xBE, (byte) 0xCC, 
									(byte) 0xF5, (byte) 0xCE, (byte) 0xAA, (byte) 0xBC, (byte) 0xF2, (byte) 0xBC, (byte) 0xF4, (byte) 0xC0, 
									(byte) 0xD6, (byte) (byte) 0xBF, (byte) 0xEC, (byte) 0xBC, (byte) 0xF4, (byte) 0xD7, (byte) 0xA8, 
									(byte) 0xC3, (byte) 0xC5, (byte) 0xB5, (byte) 0xEA, (byte) 0xB3, (byte) 0xC9, (byte) 0xC8, (byte) 0xCB, 0x0a};
	
	//����Ʊע��1�� �ڶ���
	private byte[] adultNoteOne_2 = {0x1b, 0x40, 0x1c, 0x26,
									0x20, 0x20, 0x20, 0x28, (byte) 0xC9, (byte) 0xED, (byte) 0xB8, (byte) 0xDF, 0x31, 0x2E, 0x34, (byte) 0xC3, 
									(byte) 0xD7, (byte) 0xD2, (byte) 0xD4, (byte) 0xC9, (byte) 0xCF, (byte) 0xBA, (byte) 0xAC, 0x31, 0x2E, 0x34, 
									(byte) 0xC3, (byte) 0xD7, 0x29, (byte) 0xBC, (byte) 0xF4, (byte) 0xB7, (byte) 0xA2, 0x0a};
		
	//����Ʊע��1�� ������
	private byte[] adultNoteOne_3 = {0x1b, 0x40, 0x1c, 0x26,
									0x20, 0x20, 0x20, (byte) 0xD7, (byte) 0xA8, (byte) 0xD3, (byte) 0xC3, (byte) 0xC6, (byte) 0xBE, (byte) 0xD6, 
									(byte) 0xA4, 0x3B, 0x0a};
	
	
	
	//ע��2����һ��
	private byte[] noteTwo_1 = {0x1b, 0x40, 0x1c, 0x26, 
								0x32, (byte) 0xA1, (byte) 0xA2, (byte) 0xC6, (byte) 0xBE, (byte) 0xB4, (byte) 0xCB, (byte) 0xC6, 
								(byte) 0xBE, (byte) 0xCC, (byte) 0xF5, (byte) 0xBF, (byte) 0xC9, (byte) 0xD2, (byte) 0xD4, (byte) 0xD4, 
								(byte) 0xDA, (byte) 0xBC, (byte) 0xF2, (byte) 0xBC, (byte) 0xF4, (byte) 0xC0, (byte) 0xD6, (byte) 0xBF, 
								(byte) 0xEC, (byte) 0xBC, (byte) 0xF4, (byte) 0xD7, (byte) 0xA8, (byte) 0xC3, (byte) 0xC5, 0x0a};
	//ע��2���ڶ���
	private byte[] noteTwo_2 = {0x1b, 0x40, 0x1c, 0x26, 
								0x20, 0x20, 0x20, (byte) 0xB5, (byte) 0xEA, (byte) 0xCF, (byte) 0xED, (byte) 0xCA, (byte) 0xDC, (byte) 0xD7, 
								(byte) 0xA8, (byte) 0xD2, (byte) 0xB5, (byte) 0xBC, (byte) 0xF4, (byte) 0xB7, (byte) 0xA2, (byte) 0xD2, (byte) 0xBB, 
								(byte) 0xB4, (byte) 0xCE, (byte) 0xA3, (byte) 0xAC, (byte) 0xB8, (byte) 0xB4, (byte) 0xD3, (byte) 0xA1, (byte) 0xCE, 
								(byte) 0xDE, (byte) 0xD0, (byte) 0xA7, (byte) 0x3B, 0x0a};
	//ע��3
	private byte[] noteThree = {0x1b, 0x40, 0x1c, 0x26,
								0x33, (byte) 0xA1, (byte) 0xA2, (byte) 0xB1, (byte) 0xBE, (byte) 0xC6, (byte) 0xBE, (byte) 0xCC, (byte) 0xF5, 
								(byte) 0xBD, (byte) 0xF6, (byte) 0xBF, (byte) 0xC9, (byte) 0xD4, (byte) 0xDA, (byte) 0xB9, (byte) 0xBA, (byte) 0xC2, 
								(byte) 0xF2, (byte) 0xB7, (byte) 0xD6, (byte) 0xB5, (byte) 0xEA, (byte) 0xCA, (byte) 0xB9, (byte) 0xD3, (byte) 0xC3, 
								0x3B, 0x0a};
	//ע��4����һ��
	private byte[] noteFour_1 = {0x1b, 0x40, 0x1c, 0x26,
								0x34, (byte) 0xA1, (byte) 0xA2, (byte) 0xB4, (byte) 0xCB, (byte) 0xC6, (byte) 0xBE, (byte) 0xCC, (byte) 0xF5, 
								(byte) 0xB2, (byte) 0xBB, (byte) 0xBC, (byte) 0xC7, (byte) 0xC3, (byte) 0xFB, (byte) 0xA1, (byte) 0xA2, (byte) 0xB2, 
								(byte) 0xBB, (byte) 0xB9, (byte) 0xD2, (byte) 0xCA, (byte) 0xA7, (byte) 0xA1, (byte) 0xA2, (byte) 0xB2, (byte) 0xBB, 
								(byte) 0xC4, (byte) 0xDC, (byte) 0xB6, (byte) 0xD2, 0x0a};
	//ע��4���ڶ���
	private byte[] noteFour_2 = {0x1b, 0x40, 0x1c, 0x26,
							  	0x20, 0x20, 0x20, (byte) 0xBB, (byte) 0xBB, (byte) 0xCF, (byte) 0xD6, (byte) 0xBD, (byte) 0xF0, (byte) 0xA3, (byte) 0xAC, 
							  	(byte) 0xD3, (byte) 0xC3, (byte) 0xCD, (byte) 0xEA, (byte) 0xBC, (byte) 0xB4, (byte) 0xD6, (byte) 0xB9, 0x3B, 0x0a};
	
	//ע��5����һ��
	private byte[] noteFive_1 = {0x1b, 0x40, 0x1c, 0x26,
								0x35, (byte) 0xA1, (byte) 0xA2, (byte) 0xB4, (byte) 0xCB, (byte) 0xC6, (byte) 0xBE, (byte) 0xCC, (byte) 0xF5, (byte) 0xB4, 
								(byte) 0xD3, (byte) 0xB9, (byte) 0xBA, (byte) 0xC2, (byte) 0xF2, (byte) 0xD6, (byte) 0xAE, (byte) 0xC8, (byte) 0xD5, (byte) 0xC6, 
								(byte) 0xF0, (byte) 0xA3, (byte) 0xAC, (byte) 0xD3, (byte) 0xD0, (byte) 0xD0, (byte) 0xA7, (byte) 0xC6, (byte) 0xDA, (byte) 0xCE, 
								(byte) 0xAA, 0x0a};
	//ע��5���ڶ���
	private byte[] noteFive_2 = {0x1b, 0x40, 0x1c, 0x26,
								0x20, 0x20, 0x20, 0x37, (byte) 0xCC, (byte) 0xEC, (byte) 0xA3, (byte) 0xAC, (byte) 0xB9, (byte) 0xFD, (byte) 0xC6, 
								(byte) 0xDA, (byte) 0xD7, (byte) 0xF7, (byte) 0xB7, (byte) 0xCF, 0x3B, 0x0a};
	
	//ע��6����һ��
	private byte[] noteSix_1 = {0x1b, 0x40, 0x1c, 0x26,
								0x36, (byte) 0xA1, (byte) 0xA2, (byte) 0xBC, (byte) 0xF2, (byte) 0xBC, (byte) 0xF4, (byte) 0xC0, (byte) 0xD6, 
								(byte) 0xBF, (byte) 0xC9, (byte) 0xC4, (byte) 0xDC, (byte) 0xD4, (byte) 0xDA, (byte) 0xB7, (byte) 0xA8, (byte) 0xC2, 
								(byte) 0xC9, (byte) 0xD4, (byte) 0xCA, (byte) 0xD0, (byte) 0xED, (byte) 0xB7, (byte) 0xB6, (byte) 0xCE, (byte) 0xA7, 
								(byte) 0xC4, (byte) 0xDA, (byte) 0xB6, (byte) 0xD4, 0x0a};
	//ע��6���ڶ���
	private byte[] noteSix_2 = {0x1b, 0x40, 0x1c, 0x26,
								0x20, 0x20, 0x20, (byte) 0xB4, (byte) 0xCB, (byte) 0xCF, (byte) 0xB8, (byte) 0xD4, (byte) 0xF2, (byte) 0xD7, 
								(byte) 0xF7, (byte) 0xB3, (byte) 0xF6, (byte) 0xCA, (byte) 0xCA, (byte) 0xB5, (byte) 0xB1, (byte) 0xB5, 
								(byte) 0xF7, (byte) 0xD5, (byte) 0xFB, (byte) 0xA1, (byte) 0xA3, 0x0a};

	//��ӡ������
	private byte[] initPrinterCMD = { 0x1b, 0x40 };
	private byte[] printDataCMD = { 0x0a };
	private byte[] lineSizeZeroCMD = { 0x1b, 0x33, 0x00 };
	private byte[] lineSizeDefCMD = { 0x1b, 0x32 };
	private byte[] fountSizeUpCMD = { 0x1d, 0x21, 0x1 };
	private byte[] fountSizeDefCMD = { 0x1d, 0x21, 0x0 };
	private byte[] cutPaperCMD = { 0x1d, 0x56, 0x42, 0x00 };

	//λͼ����
	private Bitmap compressPic = null;

	/************************************************ �ⲿ�ӿ� **********************************************/

	//��ʼ����ӡ��
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

	// ������ά��
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

	//��ӡСƱ
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

	//����MD5����
	public final static char[] Md5encrypt(String plaintext) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = plaintext.getBytes();
			// ���MD5ժҪ�㷨�� MessageDigest ����
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// ʹ��ָ�����ֽڸ���ժҪ
			mdInst.update(btInput);
			// �������
			byte[] md = mdInst.digest();
			// ������ת����ʮ�����Ƶ��ַ�����ʽ
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

	/************************************************ �ڲ��ӿ� **********************************************/

	/*
	 * ��ӡ��ͷ�� �����-�򵥣����� ��ӭ���ټ���ֿ��ר�ŵ�
	 */
	private void titlePrint() {
		jPrinterDataSend(initPrinterCMD, initPrinterCMD.length);
		jPrinterDataSend(line1Data, line1Data.length);
		jPrinterDataSend(line2Data, line2Data.length);
	}

	/*
	 * ����Ʊ 
	 * �۸�xxԪ
	 */
	private void adultTicketPrint() {
		jPrinterDataSend(lineAdule, lineAdule.length);
		jPrinterDataSend(lineAdultMoney, lineAdultMoney.length);
	}

	/*
	 * ��ͯƱ 
	 * �۸�xxԪ
	 */
	private void childTicketPrint() {
		jPrinterDataSend(lineChild, lineChild.length);
		jPrinterDataSend(lineChildMoney, lineChildMoney.length);
	}
	
	/*
	 * ���Ʊ
	 * �۸� xxԪ
	 * 
	 * */
	private void balanceTicketPrint() {
		jPrinterDataSend(lineBalanceData, lineBalanceData.length);
	}

	// ��ӡ���
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

	// ��ӡ��ά��
	private void qrCodePrint(InputStream QrData) {

		jPrinterDataSend(lineSizeZeroCMD, lineSizeZeroCMD.length);

		Bitmap mBitmap = BitmapFactory.decodeStream(QrData);

		compressPic = compressPic(mBitmap);

		qrData = draw2PxPoint(compressPic);

		jPrinterDataSend(qrData, qrData.length);

	}

	/*
	 * �豸�ţ�xxxxxx ʱ�� ���˵绰��xxxxx ��ַ
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
	 * ��ӡע����Ϣ
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
	 * ��һ��BitmapͼƬת��Ϊ��ӡ�����Դ�ӡ��bit(��ͼƬѹ��Ϊ360*360) Ч�ʺܸߣ���������棩
	 * 
	 * @param bit
	 * @return
	 */
	public static byte[] draw2PxPoint(Bitmap bit) {
		byte[] data = new byte[8811];
		int k = 0;
		for (int j = 0; j < 11; j++) {
			// ��ӡ����ָ��
			data[k++] = 0x1b;
			data[k++] = 0x61;
			data[k++] = 0x01;

			// λͼ��ӡָ��
			data[k++] = 0x1B;
			data[k++] = 0x2A;
			data[k++] = 0x21; // hex:0x21 = dec:33 m=33ʱ��ѡ��24��˫�ܶȴ�ӡ���ֱ��ʴﵽ200DPI��
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
	 * ͼƬ��ֵ������ɫ��1����ɫ��0
	 * 
	 * @param x
	 *            ������
	 * @param y
	 *            ������
	 * @param bit
	 *            λͼ
	 * @return
	 */
	public static byte px2Byte(int x, int y, Bitmap bit) {
		byte b;
		int pixel = bit.getPixel(x, y);
		int red = (pixel & 0x00ff0000) >> 16; // ȡ����λ
		int green = (pixel & 0x0000ff00) >> 8; // ȡ����λ
		int blue = pixel & 0x000000ff; // ȡ����λ
		int gray = RGB2Gray(red, green, blue);
		if (gray < 128) {
			b = 1;
		} else {
			b = 0;
		}
		return b;
	}

	/**
	 * ͼƬ�Ҷȵ�ת��
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	private static int RGB2Gray(int r, int g, int b) {
		int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b); // �Ҷ�ת����ʽ
		return gray;
	}

	/**
	 * ��ͼƬ����ѹ����ȥ��͸���ȣ�
	 * 
	 * @param bitmapOrg
	 */
	public static Bitmap compressPic(Bitmap bitmapOrg) {
		// ��ȡ���ͼƬ�Ŀ�͸�
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
		// ����Ԥת���ɵ�ͼƬ�Ŀ�Ⱥ͸߶�
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
			returnText = stText.getBytes("GBK"); // �������try�ڲſ���
		} catch (Exception ex) {
			;
		}
		return returnText;
	}

}
