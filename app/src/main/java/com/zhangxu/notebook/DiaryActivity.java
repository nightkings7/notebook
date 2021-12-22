package com.zhangxu.notebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DiaryActivity extends AppCompatActivity {

    EditText mydiaryEditText;
    Button saveButton;
    private long lastBackKeyTime=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        initViews();
        setListeners();
    }

    @Override
    public void onBackPressed() {
        long timenow=System.currentTimeMillis();
        if (lastBackKeyTime==-1) {
            lastBackKeyTime = timenow;
            Toast.makeText(DiaryActivity.this, "再按一下【返回】键，退出‘我的日记’", Toast.LENGTH_SHORT).show();
        } else {
            long INTERVAL = 2000;
            if ((timenow-lastBackKeyTime)<= INTERVAL) {
                finish();
            } else {
                lastBackKeyTime=timenow;
                Toast.makeText(DiaryActivity.this, "再按一下【返回】键，退出‘我的日记’", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void initViews() {
        mydiaryEditText=findViewById(R.id.edit_mydiary);
        saveButton=findViewById(R.id.btn_save);
        SharedPreferences pref_text=getSharedPreferences("diary_text", Context.MODE_PRIVATE);
        String diary_txt=pref_text.getString("DIARY","");
        Time time=new Time("GMT+8");
        time.setToNow();
        diary_txt=diary_txt+"\r\n"+time.year+"年-"+(time.month+1)+"月-"+time.monthDay+"日\n";
        mydiaryEditText.setText(diary_txt);
    }

    void setListeners() {
        saveButton.setOnClickListener(view -> {
            SharedPreferences pref_text=getSharedPreferences("diary_text",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=pref_text.edit();
            editor.putString("DIARY",mydiaryEditText.getText().toString());
            editor.apply();
            Toast.makeText(DiaryActivity.this,"保存成功",Toast.LENGTH_LONG).show();
        });
    }

}
