package com.zhangxu.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEdit, pwdEdit;
    CheckBox rememberPwdCheck;
    Button loginBtn;
    ProgressBar progressBar;

    final int MSG_JUMP=0x111;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setListeners();
        initHandler();
    }

    void initViews() {
        usernameEdit=findViewById(R.id.edit_username);
        pwdEdit=findViewById(R.id.edit_pwd);
        rememberPwdCheck=findViewById(R.id.check_rememberPwd);
        loginBtn=findViewById(R.id.btn_login);
        progressBar=findViewById(R.id.progressbar);

        SharedPreferences pref=getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        Boolean rem=pref.getBoolean("REMBERPWD",false);
        if (rem) {
            pwdEdit.setText(pref.getString("PWD",""));
            usernameEdit.setText(pref.getString("USERNAME",""));
            rememberPwdCheck.setChecked(true);
        }
    }

    void setListeners() {
        loginBtn.setOnClickListener(view -> setListenersdo());
    }

    void setListenersdo() {
        String  username=usernameEdit.getText().toString();
        String pwd=pwdEdit.getText().toString();
        if (username.equals("admin")&&pwd.equals("123456")){
            SharedPreferences pref=getSharedPreferences("userinfo",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=pref.edit();

            if (rememberPwdCheck.isChecked()) {
                editor.putBoolean("REMBERPWD",true);
                editor.putString("USERNAME",username);
                editor.putString("PWD",pwd);
            } else {
                editor.putBoolean("REMBERPWD",false);
                editor.putString("USERNAME","");
                editor.putString("PWD","");
            }
            editor.apply();

            usernameEdit.setEnabled(false);
            pwdEdit.setEnabled(false);
            loginBtn.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Message msg=new Message();
                    msg.what=MSG_JUMP;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            Toast.makeText(LoginActivity.this,"用户名或密码不正确",Toast.LENGTH_SHORT).show();
        }
    }

    void initHandler() {
        handler=new Handler() {
            @Override
            public void handleMessage(Message msg){
                if (msg.what==MSG_JUMP) {
                    Intent intent=new Intent();
                    intent.setClass(LoginActivity.this, DiaryActivity.class);
                    startActivity(intent);
                    finish();
                }
                super.handleMessage(msg);
            }
        };
    }

}