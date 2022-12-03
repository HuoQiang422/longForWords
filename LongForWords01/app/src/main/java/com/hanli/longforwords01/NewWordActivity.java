package com.hanli.longforwords01;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hanli.longforwords01.mydb.MyWordList;
import com.hanli.longforwords01.mydb.ShareWordsList;

public class NewWordActivity extends AppCompatActivity implements View.OnClickListener {
    int offset=0;
    private EditText etWord;
    private EditText etExp;
    private Button btnLast;
    private Button btnNext;
    private Button btnDel;
    private Button btnUpdate;
    private Button btnShare;
    MyWordList myWordList;
    ShareWordsList shareWordsList=new ShareWordsList(this);

    String account;
    String author;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_word_activity);
        init();
        Intent intent=getIntent();
        account=intent.getStringExtra("account");
        author=intent.getStringExtra("name");
        showWord();
    }

    private void init() {
        etWord=(EditText)findViewById(R.id.et_word);
        etExp=(EditText)findViewById(R.id.et_exp);
        btnLast=(Button)findViewById(R.id.last_word);
        btnNext=(Button)findViewById(R.id.next_word);
        btnDel=(Button)findViewById(R.id.btn_delete);
        btnUpdate=(Button)findViewById(R.id.btn_update);
        btnShare=(Button)findViewById(R.id.btn_share);
        btnNext.setOnClickListener(this);
        btnLast.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SQLiteDatabase db;
        SQLiteDatabase db_w;
        ContentValues values = new ContentValues();
        db=myWordList.getReadableDatabase();
        Cursor cursor=db.query("wordList",null,null,null,null,null,null);
        if(cursor.getCount()==0)
        {
            Toast.makeText(this,"这里还没有单词",Toast.LENGTH_SHORT).show();
        }
        else
        {
            cursor.moveToFirst();
            switch (v.getId())
            {
                case R.id.last_word:
                    offset--;
                    if(offset<=0)
                    {
                        offset=0;
                        Toast.makeText(this,"这是第一个单词了！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.next_word:
                    offset++;
                    if (offset>=cursor.getCount()-1)
                    {
                        offset=cursor.getCount()-1;
                        Toast.makeText(this,"这已经是最后一个单词了！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_delete:
                    db_w=myWordList.getWritableDatabase();
                    db_w.delete("wordList","word=?",new String[]{etWord.getText().toString()});
                    Toast.makeText(this,"删除单词成功！",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_update:
                    db_w=myWordList.getWritableDatabase();
                    values.put("exp",etExp.getText().toString());
                    db_w.update("wordList",values,"word=?",new String[]{etWord.getText().toString()});
                    Toast.makeText(this,"自定义词义成功！",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_share:
                    db_w=shareWordsList.getWritableDatabase();
                    if(isShared(db_w,author,etWord.getText().toString()))
                    {
                        Toast.makeText(this,"该单词已被共享，快去查看吧！",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        values.put("author",author);
                        values.put("word",etWord.getText().toString());
                        values.put("exp",etExp.getText().toString());
                        db_w.insert("share_words",null,values);
                        Toast.makeText(this,"已添加至共享词库",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            System.out.println("位置"+offset);
            System.out.println("单词数量："+cursor.getCount());
            cursor.move(offset);
            etWord.setText(cursor.getString(1));
            etExp.setText(cursor.getString(2));
        }

        cursor.close();
        db.close();
    }

    /**
     * 初始进入生词本时显示单词，并进行相应判断。
     */
    private void showWord()
    {
        SQLiteDatabase db;
        myWordList=new MyWordList(this,account);
        db=myWordList.getReadableDatabase();
        Cursor cursor=db.query("wordList",null,null,null,null,null,null);
        if(cursor.getCount()==0)
        {
            etWord.setText("Hello Android！");
            etExp.setText("生词本中还没有添加单词，快去词库添加吧！");
        }else
        {
            System.out.println(cursor.getCount());
            cursor.move(1);
            etWord.setText(cursor.getString(1));
            etExp.setText(cursor.getString(2));
        }
        cursor.close();
        db.close();
    }

    /**
     * 判断单词是否被分享，如果已经分享过了，则无法继续分享
     * @param db 获取数据库
     * @param author 获取作者（昵称）
     * @param word 获取单词
     * @return
     */
    private boolean isShared(SQLiteDatabase db,String author,String word)
    {
        Cursor cursor=db.query("share_words",null,"word=? and author=?",new String[]{word,author},null,null,null);
        if (cursor.moveToNext())
        {
            return true;
        }
        return false;
    }
}
