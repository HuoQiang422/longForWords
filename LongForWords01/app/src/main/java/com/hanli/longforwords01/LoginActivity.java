package com.hanli.longforwords01;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hanli.longforwords01.mydb.MyUserList;
import com.hanli.longforwords01.user.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText lg_account;
    private EditText lg_password;
    private Button btn_submit;
    private TextView toRegist;
    User user=new User();
    MyUserList myUserList=new MyUserList(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        init();
    }
    private void init() {
        lg_account=(EditText) findViewById(R.id.account);
        lg_password=(EditText)findViewById(R.id.password);
        btn_submit=(Button)findViewById(R.id.btn_submit);
        toRegist=(TextView)findViewById(R.id.btn_toregist);
        toRegist.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.btn_submit:
                boolean userFromDB = getUserFromDB();
                if (userFromDB)
                {
                    Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
                    intent=new Intent(this,MenuActivity.class);
                    intent.putExtra("name",user.getName());
                    intent.putExtra("account",user.getAccount());
                    startActivity(intent);
                }
                break;
            case R.id.btn_toregist:
                intent=new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_forgetpwd:
                //后续实现
                break;
        }
    }

    /**
     * 从数据库获取用户数据，进行用户验证
     * @return
     */
    public boolean getUserFromDB()
    {
        String password;
        SQLiteDatabase db;
        String[] selectionArgs ;
        user.setAccount(lg_account.getText().toString());
        user.setPassword(lg_password.getText().toString());
        db=myUserList.getReadableDatabase();
        selectionArgs=new  String[]{user.getAccount()};
        Cursor cursor=db.query("userList",null,"account=?",
                selectionArgs,null,null,null);
        if (cursor.moveToNext())
        {
            password=cursor.getString(3);
            user.setName(cursor.getString(1));
            if(user.getPassword().equals(password)) {
                return true;
            }
        }
        else
        {
            Toast.makeText(this,"您输入的密码或账号有误，请重新输入",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * 重写返回键点击事件
     * @param keyCode 键值
     * @param event 事件
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 按下键盘上返回按钮
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            new AlertDialog.Builder(this)
                    .setMessage("确定退出系统吗？")
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            })
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    finish();
                                }
                            }).show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 杀掉进程
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
