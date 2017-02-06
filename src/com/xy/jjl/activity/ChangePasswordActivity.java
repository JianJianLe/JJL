package com.xy.jjl.activity;

 
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.xy.jjl.R;
import com.xy.jjl.application.ApplicationInfo;
import com.xy.jjl.common.APPConfig;
import com.xy.jjl.utils.OkHttpUtils;
import com.xy.jjl.utils.OkHttpUtils.Param;
import com.xy.jjl.utils.OkHttpUtils.ResultCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity implements OnClickListener {
	
	private ImageButton changePasswordBack;// ����
	private Button changePasswordConfirm;// ȷ��
	private EditText oldPasswordEditText;// ��ǰ����
	private EditText newPasswordEditText;// ������
	private EditText newPasswordConfirmEditText;// ȷ������
	private String oldPassword;
	private String newPassword;
	
	private String flag;
	private Dialog dialog;
	private Context mContext=ChangePasswordActivity.this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		ApplicationInfo.getInstance().addActivity(this);
		initView();
	}
	
	public void initView() {
		
		changePasswordBack = (ImageButton) findViewById(R.id.changePasswordBackImageButton);
		changePasswordConfirm = (Button) findViewById(R.id.changePasswordConfirmButton);
		
		oldPasswordEditText = (EditText) findViewById(R.id.oldPasswordEditText);
		newPasswordEditText = (EditText) findViewById(R.id.newPasswordEditText);
		newPasswordConfirmEditText = (EditText) findViewById(R.id.newPasswordConfirmEditText);
		
		changePasswordConfirm.setOnClickListener(this);
		changePasswordBack.setOnClickListener(this);
	}
	
	private void ChangePassword(){
		//��ȡ����
		List<Param> list = new ArrayList<OkHttpUtils.Param>();
		
		String myname=ApplicationInfo.getInstance().getUserName();
		list.add(new Param("username", myname));
		list.add(new Param("oldpassword", oldPassword));
		list.add(new Param("newpassword", newPassword));
		OkHttpUtils.post(APPConfig.changePassword, new ResultCallback<String>() {
			public void onSuccess(String response) {
				
				try{					
					JSONObject obj= new JSONObject(response);
					flag=obj.getString("flag");//0: ��ʾ�������û�  1:��ʾ��ȡ�û��б�ɹ�
					if(flag.equals("1")){
						new AlertDialog.Builder(mContext).setTitle("��ʾ")
						.setMessage("�޸�����ɹ���")
						.setPositiveButton("OK",new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent=new Intent();
								intent.setClass(ChangePasswordActivity.this, SettingsActivity.class);
								startActivity(intent);
								ChangePasswordActivity.this.finish();
								overridePendingTransition(R.anim.slide_left_in,
										                  R.anim.slide_right_out);
								return;
							}
						}).create().show();
						
					}else{
						InitDialog("��ʾ","�޸�����ʧ�ܣ�");
						dialog.show();
					}
				}catch(JSONException e){
					e.printStackTrace();
					InitDialog("��ʾ","�޸�����ʧ�ܣ�");
					dialog.show();
				}
				
			};			
			public void onFailure(Exception e) {
				InitDialog("��ʾ","�޸�����ʧ�ܣ�");
				dialog.show();
				return;
			};
		}, list);
	}
	
	public void InitDialog(String title,String msg){
		
		dialog=new AlertDialog.Builder(mContext).setTitle(title)
			.setMessage(msg)
			.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			}).create();

	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		int id = v.getId();
		switch (id) {
		
		case R.id.changePasswordBackImageButton:
			intent=new Intent();
			intent.setClass(ChangePasswordActivity.this, PersonalCenterActivity.class);
			startActivity(intent);
			ChangePasswordActivity.this.finish();
			overridePendingTransition(R.anim.slide_left_in,
					                  R.anim.slide_right_out);
			break;
			
		
		case R.id.changePasswordConfirmButton:
			oldPassword = oldPasswordEditText.getText().toString();
			newPassword = newPasswordEditText.getText().toString();
			String newPasswordConfirm = newPasswordConfirmEditText.getText()
					.toString();
			if (oldPassword == null || oldPassword.equals(""))
				Toast.makeText(ChangePasswordActivity.this, "��ǰ���벻��Ϊ��",
						Toast.LENGTH_LONG).show();
			else if (newPassword == null || newPassword.equals(""))
				Toast.makeText(ChangePasswordActivity.this, "�����벻��Ϊ��",
						Toast.LENGTH_LONG).show();
			else if (newPasswordConfirmEditText == null
					|| newPasswordConfirmEditText.equals(""))
				Toast.makeText(ChangePasswordActivity.this, "�����벻��Ϊ��",
						Toast.LENGTH_LONG).show();
			else if (!newPasswordConfirm.equals(newPassword))
				Toast.makeText(ChangePasswordActivity.this, "ȷ�������������벻һ��",
						Toast.LENGTH_LONG).show();
			else {
				
				ChangePassword();
	
			}
			break;
		default:
			break;
		}
	}

}
