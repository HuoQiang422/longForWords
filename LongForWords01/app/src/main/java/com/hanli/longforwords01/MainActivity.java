package com.hanli.longforwords01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanli.longforwords01.etymon.EtymonService;
import com.hanli.longforwords01.etymon.EtymonWord;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //创建一个listView进行关联
    private ListView listView;
    //创建母词列表
    private List<String> etymon=new ArrayList<String>();
    //创建释义列表
    private List<String> exp =new ArrayList<String>();
    //创建背景视图，用于动态变换背景颜色
    //private RelativeLayout rl;
    private TextView tv_account;
    //预设背景色
    public String[] color={"#D32F2F","#0288D1","#FFEB3B","#FF5722","#00796B"};
    Random random=new Random();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置启动视图
        setContentView(R.layout.activity_main);

        tv_account=(TextView)findViewById(R.id.account);
        Intent intent=getIntent();
        String account = intent.getStringExtra("account");
        tv_account.setText(account);

        //获取到关联表单
        listView=(ListView)findViewById(R.id.lv);
        getEtymons();
        MyBaseAdapter adapter=new MyBaseAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击跳转到派生词列表
                passData(position);
                //System.out.println("文件编码为："+R.raw.state);
                //System.out.println("文件编码为："+Integer.parseInt(String.valueOf(R.raw.state)));
                //Toast.makeText(MainActivity.this,position+" ",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 传递母词数据
     * @param position 获取到母词id
     */
    public void passData(int position)
    {
        Intent intent=new Intent(this,WordActivity.class);
        intent.putExtra("account",tv_account.getText().toString());
        intent.putExtra("etymon",etymon.get(position));
        startActivity(intent);
    }

    /**
     * 适配器类
     */
    class MyBaseAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return etymon.size();
        }

        @Override
        public Object getItem(int position) {
            return etymon.get(position);
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
                convertView=View.inflate(MainActivity.this,R.layout.showetmon,null);
                convertView.setBackgroundColor(Color.parseColor(color[random.nextInt(5)]));
                viewHolder=new ViewHolder();
                viewHolder.word=(TextView)convertView.findViewById(R.id.etymon);
                viewHolder.exps=(TextView)convertView.findViewById(R.id.exp);
                viewHolder.rl=findViewById(R.id.show_etymon);

                convertView.setTag(viewHolder);
            }else
            {
                viewHolder =(ViewHolder) convertView.getTag();
            }
            viewHolder.word.setText(etymon.get(position));
            viewHolder.exps.setText(exp.get(position));
            //viewHolder.rl.setBackgroundColor(Color.parseColor(color[random.nextInt(4)]));
            return convertView;
        }
    }

    class ViewHolder
    {
        TextView word;
        TextView exps;
        RelativeLayout rl;
    }
    /*
    解析xml文件，获取到母词
     */
    public void getEtymons()
    {
        InputStream is=this.getResources().openRawResource(R.raw.words);
        try {
            List<EtymonWord> etymonWords= EtymonService.getEtymonFromJson(is);
            for (int i = 0; i <etymonWords.size() ; i++) {
                etymon.add(etymonWords.get(i).getEtymon());
                exp.add(etymonWords.get(i).getExp());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"解析失败",Toast.LENGTH_SHORT).show();
        }
    }
}
