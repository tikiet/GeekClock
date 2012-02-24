package edu.crabium.android;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import android.app.Activity;
import android.view.KeyEvent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class GeonamesRegisterActivity extends Activity {
	
	private Button m_BackButton, m_ConfirmButton;
	private EditText m_UsernameExitText;
	private TextView m_ShowUsernameTextView, m_HintTextView;
	private ImageView m_RegisterImageView;
	private String m_InputRecord;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.geonames_register);
		
		m_BackButton = (Button)findViewById(R.id.backButton);
		m_BackButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				/* 新建一个Intent对象 */
				Intent intent = new Intent();
				/* 指定intent要启动的类 */
				intent.setClass(GeonamesRegisterActivity.this, SetActivity.class);
				/* 启动一个新的Activity */
				startActivity(intent);
				/* 关闭当前的Activity */
				GeonamesRegisterActivity.this.finish();
			}
		});
		
		m_ShowUsernameTextView = (TextView)findViewById(R.id.showUsernameTextView);
		if(TimeProvider.GeoNamesUserName == "tikiet") {
			m_ShowUsernameTextView.setText("\t现在使用的账户是公用账户, 查询次数有限, 建议注册私人账号.");
		} else {
			m_ShowUsernameTextView.setText("正在使用的账户是:\n" + TimeProvider.GeoNamesUserName);
		}
		
		
		m_UsernameExitText = (EditText)findViewById(R.id.usernameEditText);
		m_UsernameExitText.setHint("输入用户名");
		m_UsernameExitText.setOnKeyListener(new EditText.OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				m_InputRecord = m_UsernameExitText.getText().toString();
				if(m_InputRecord.length() >= 25) {
					m_HintTextView.setText("用户名输入过长！！");
				} else {
					m_HintTextView.setText(m_InputRecord);
				}
				return false;
			}
		});
		
		m_HintTextView = (TextView) findViewById(R.id.hintTextView);
		m_HintTextView.setTextColor(Color.GRAY);
		
		m_ConfirmButton = (Button)findViewById(R.id.confirmButton);
		m_ConfirmButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (m_InputRecord == null) {
					m_HintTextView.setText("骚年，请正确输入！！");
				} else {			
					TimeProvider.GeoNamesUserName = m_InputRecord;
					m_HintTextView.setText("输入成功！！");
					m_ShowUsernameTextView.setText("正在使用的账户是:：\n" + TimeProvider.GeoNamesUserName);
					m_UsernameExitText.setFocusable(false);
					m_UsernameExitText.setFocusableInTouchMode(false);
					
					
					//写入设置
			        SAXBuilder saxBuilder = new SAXBuilder();
			        try 
			        {
			        	FileInputStream fileInputStream  = new FileInputStream("/data/data/edu.crabium.android/files/geekclock.xml");
						Document document = saxBuilder.build(fileInputStream);
						fileInputStream.close();
						
						Element root = document.getRootElement();
						Element GeoNamesConfig = root.getChild("GeoNames");
						Element GeoNamesUserName = GeoNamesConfig.getChild("UserName");
						GeoNamesUserName.setText(TimeProvider.GeoNamesUserName);
						XMLOutputter out = new XMLOutputter();
						FileOutputStream fileOutputStream = new FileOutputStream("/data/data/edu.crabium.android/files/geekclock.xml");
						out.output(document,fileOutputStream);  
						
						fileOutputStream.flush();
						fileOutputStream.close();
						
					} catch(IOException e){
					} catch (JDOMException e){
						e.printStackTrace();
					}
				}
			}
		});
		
		m_RegisterImageView = (ImageView)findViewById(R.id.registerImageView);
		m_RegisterImageView.setOnClickListener(new View.OnClickListener(){
	           @Override
			public void onClick(View v){
	               Intent intent = new Intent();
	               intent.setAction(Intent.ACTION_VIEW);
	               intent.addCategory(Intent.CATEGORY_BROWSABLE);
	               intent.setData(Uri.parse("http://www.geonames.org/login"));
	               startActivity(intent);
	           }
	       });  
	}
}
