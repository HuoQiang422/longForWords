package com.hanli.longforwords01;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 主要是各种组件的点击跳转事件
 */
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_wordShare;
    private Button btn_newWordBook;
    private Button btn_lianCi;
    private Button btn_search;
    private Button btn_mine;
    private Button btn_exit;
    private TextView tv_name;
    private TextView tv_account;

    String name,account;
    public static List<Activity> activityList=new ArrayList<Activity>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        init();
        Intent getInfo=getIntent();
        name = getInfo.getStringExtra("name");
        account = getInfo.getStringExtra("account");
        tv_name.setText(name);
        tv_account.setText(account);
        activityList.add(this);
    }

    private void init() {
        tv_name=(TextView)findViewById(R.id.name);
        tv_account=(TextView)findViewById(R.id.account);
        btn_newWordBook=(Button)findViewById(R.id.new_word_book);
        btn_wordShare=(Button)findViewById(R.id.word_share);
        btn_lianCi=(Button)findViewById(R.id.lianci);
        btn_search=(Button)findViewById(R.id.search);
        btn_mine=(Button)findViewById(R.id.mine);
        btn_exit=(Button)findViewById(R.id.exit);

        btn_wordShare.setOnClickListener(this);
        btn_newWordBook.setOnClickListener(this);
        btn_lianCi.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_mine.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent transIntent;
        switch (v.getId())
        {
            case R.id.new_word_book:
                transIntent=new Intent(this,NewWordActivity.class);
                transIntent.putExtra("name",name);
                transIntent.putExtra("account",tv_account.getText().toString());
                startActivity(transIntent);
                break;
            case R.id.word_share:
                transIntent=new Intent(this,WordShare.class);
                transIntent.putExtra("name",name);
                transIntent.putExtra("account",tv_account.getText().toString());
                startActivity(transIntent);
                break;
            case R.id.lianci:
                transIntent=new Intent(this,MainActivity.class);
                transIntent.putExtra("account",tv_account.getText().toString());
                startActivity(transIntent);
                break;
            case R.id.search:
                transIntent=new Intent(this,SearchActivity.class);
                startActivity(transIntent);
                break;
            case R.id.mine:
                transIntent=new Intent(this,Mine_Activity.class);
                transIntent.putExtra("name",tv_name.getText().toString());
                transIntent.putExtra("account",tv_account.getText().toString());
                startActivity(transIntent);
                break;
            case R.id.exit:
                exit();
                break;
        }
    }
    public void exit()
    {
        for (Activity act:activityList
             ) {
            act.finish();
        }
        System.exit(0);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
