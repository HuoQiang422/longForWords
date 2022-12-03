package com.hanli.longforwords01;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hanli.longforwords01.mydb.MyUserList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText re_name;
    private EditText re_account;
    private EditText re_password;
    private EditText cf_password;
    private Button re_submit;

    MyUserList myUserList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        myUserList=new MyUserList(this);
        init();
    }

    private void init() {
        re_name=(EditText)findViewById(R.id.re_name);
        re_account=(EditText)findViewById(R.id.re_account);
        re_password=(EditText)findViewById(R.id.re_password);
        cf_password=(EditText)findViewById(R.id.cf_password);
        re_submit=(Button)findViewById(R.id.btn_re_submit);
        re_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name;
        String account;
        String pwd;
        String cf_pwd;
        SQLiteDatabase db;
        SQLiteDatabase db_r;
        name=re_name.getText().toString();
        account=re_account.getText().toString();
        pwd=re_password.getText().toString();
        cf_pwd=cf_password.getText().toString();
        ContentValues values;
        //将数据添加至数据库
        if(v.getId()==R.id.btn_re_submit)
        {
            db_r=myUserList.getReadableDatabase();
            Cursor cursor = db_r.query("userList", null, "name=? or account=?",
                    new String[]{name, account}, null, null, null);
            db=myUserList.getWritableDatabase();
            values=new ContentValues();
            if (cursor.moveToNext())
            {
                Toast.makeText(this,"该账户已被注册！",Toast.LENGTH_SHORT).show();
            }else
            {
                if(pwd.equals(cf_pwd)&&!account.equals("")&&!pwd.equals(""))
                {
                    values.put("name",name);
                    values.put("account",account);
                    values.put("password",pwd);
                    db.insert("userList",null,values);
                    Toast.makeText(this,"注册成功！",Toast.LENGTH_SHORT).show();
                    db.close();
                }
                else if (name.equals(""))
                {
                    Toast.makeText(this,"请输入您的昵称！",Toast.LENGTH_SHORT).show();
                }
                else if (account.equals(""))
                {
                    Toast.makeText(this,"账户不能为空！",Toast.LENGTH_SHORT).show();
                }
                else if(pwd.equals(""))
                {
                    Toast.makeText(this,"密码不能为空！",Toast.LENGTH_SHORT).show();
                }
                else if(!pwd.equals(cf_pwd))
                {
                    Toast.makeText(this,"两次输入的密码不一致！",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
