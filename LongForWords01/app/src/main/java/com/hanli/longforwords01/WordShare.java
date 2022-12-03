package com.hanli.longforwords01;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hanli.longforwords01.mydb.MyWordList;
import com.hanli.longforwords01.mydb.ShareWordsList;

public class WordShare extends AppCompatActivity implements View.OnClickListener {
    int offset=0;
    private EditText etWord;
    private EditText etExp;
    private Button btnLast;
    private Button btnNext;
    private Button btnDel;
    private Button btnUpdate;

    ShareWordsList shareWordsList=new ShareWordsList(this);
    String author;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_share_activity);
        init();
        init();
        Intent intent=getIntent();
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
        btnNext.setOnClickListener(this);
        btnLast.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        SQLiteDatabase db;
        SQLiteDatabase db_w;
        ContentValues values = new ContentValues();
        db=shareWordsList.getReadableDatabase();
        Cursor cursor=db.query("share_words",null,null,null,null,null,null);
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
                    db_w=shareWordsList.getWritableDatabase();
                    int delete = db_w.delete("share_words", "word=? and author=?", new String[]{etWord.getText().toString(), author});
                    if(delete>0)
                    {
                        Toast.makeText(this,"删除单词成功！",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this,"这不是您添加的共享词！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_update:
                    db_w=shareWordsList.getWritableDatabase();
                    values.put("exp",etExp.getText().toString());
                    int update = db_w.update("share_words", values, "word=? and author=?", new String[]{etWord.getText().toString(), author});
                    if (update>0)
                    {
                        Toast.makeText(this,"修改词义成功！",Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(this,"这不是您添加的共享词！",Toast.LENGTH_SHORT).show();
                    break;
            }
            System.out.println("位置"+offset);
            System.out.println("单词数量："+cursor.getCount());
            cursor.move(offset);
            etWord.setText(cursor.getString(2));
            etExp.setText(cursor.getString(3)+"\n"+"--"+cursor.getString(1));
        }

        cursor.close();
        db.close();
    }
    private void showWord()
    {
        SQLiteDatabase db;
        db=shareWordsList.getReadableDatabase();
        Cursor cursor=db.query("share_words",null,null,null,null,null,null);
        if(cursor.getCount()==0)
        {
            etWord.setText("Hello Android！");
            etExp.setText("共享单词中还没有添加单词，快去生词本添加吧！");
        }else
        {
            System.out.println(cursor.getCount());
            cursor.move(1);
            etWord.setText(cursor.getString(2));
            etExp.setText(cursor.getString(3)+"\n"+"--"+cursor.getString(1));
        }
        cursor.close();
        db.close();
    }
}
