package com.hanli.longforwords01;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_search;
    private Button btn_search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        et_search=(EditText)findViewById(R.id.et_search);
        btn_search=(Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        if(v.getId()==R.id.btn_search)
        {
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse("https://cn.bing.com/dict/search?q="+et_search.getText().toString()));
            startActivity(intent);
        }
    }
}
