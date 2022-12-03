package com.hanli.longforwords01;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hanli.longforwords01.mydb.MyWordList;
import com.hanli.longforwords01.word.WordInfo;
import com.hanli.longforwords01.word.WordService;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class WordActivity extends AppCompatActivity {
    private ListView listView;
    private List<String> words=new ArrayList<String>();
    private List<String> exps=new ArrayList<String>();
    private String account;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_word);
        listView=(ListView) findViewById(R.id.list_word);
        getWords();
        //System.out.println("account"+tv_account.getText().toString());
        MyBaseAdapter adapter=new MyBaseAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                addToDatabase(position,account);
                return false;
            }
        });
    }
    //长按将单词添加到数据库
    private void addToDatabase(int position,String account)
    {
        MyWordList myWordList=new MyWordList(this,account);
        SQLiteDatabase db=myWordList.getWritableDatabase();
        ContentValues values=new ContentValues();
        String s = words.get(position);
        String exp = exps.get(position);
        Cursor cursor=db.query("wordList",null,"word=?",new String[]{s},null,null,null);
        if (cursor.moveToNext())
        {
            Toast.makeText(this,"生词本中已存在该单词",Toast.LENGTH_SHORT).show();
        }
        else {
            values.put("word",s);
            values.put("exp",exp);
            db.insert("wordList",null,values);
            Toast.makeText(this,"已添加至生词本",Toast.LENGTH_SHORT).show();
        }
       cursor.close();
        db.close();
    }
    public void getWords()
    {
        Intent intent=getIntent();
        String name=intent.getStringExtra("etymon");
        account = intent.getStringExtra("account");
        //获取对应文件名的id
        int id = this.getResources().getIdentifier(name, "raw", this.getPackageName());
        //System.out.println(R.raw.state);
        //System.out.println(id);
        InputStream is=this.getResources().openRawResource(id);
        try {
            List<WordInfo> wordInfos= WordService.getWordFromJson(is);
            for (int i = 0; i <wordInfos.size() ; i++) {
                words.add(wordInfos.get(i).getWord());
                exps.add(wordInfos.get(i).getExp());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"解析失败",Toast.LENGTH_SHORT).show();
        }
    }
    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return words.size();
        }

        @Override
        public Object getItem(int position) {
            return words.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null)
            {
                convertView=View.inflate(WordActivity.this,R.layout.activity_word,null);
                viewHolder= new ViewHolder();
                viewHolder.word=(TextView)convertView.findViewById(R.id.word);
                viewHolder.exps=(TextView)convertView.findViewById(R.id.exp);
                convertView.setTag(viewHolder);
            }else
            {
                viewHolder =(ViewHolder) convertView.getTag();
            }
            viewHolder.word.setText(words.get(position));
            viewHolder.exps.setText(exps.get(position));
            return convertView;
        }
    }

    class ViewHolder
    {
        TextView word;
        TextView exps;
    }
}
