package com.hanli.longforwords01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 时间关系做的比较简单，后续有时间会进行改进
 */
public class Mine_Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_name;
    private TextView tv_account;
    private Button sign_up;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity);
        init();
        Intent intent=getIntent();
        String name = intent.getStringExtra("name");
        System.out.println("name:"+name);
        String account = intent.getStringExtra("account");
        tv_account.setText(account);
        tv_name.setText(name);
    }

    private void init() {
        tv_name=(TextView)findViewById(R.id.info_name);
        tv_account=(TextView)findViewById(R.id.info_account);
        sign_up=(Button)findViewById(R.id.sign_up);
        sign_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.sign_up)
        {
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
    }
}
