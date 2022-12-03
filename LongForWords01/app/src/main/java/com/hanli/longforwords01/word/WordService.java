package com.hanli.longforwords01.word;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hanli.longforwords01.etymon.EtymonWord;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class WordService {
    public static List<WordInfo> getWordFromJson(InputStream is) throws IOException {
        byte[] buffer=new byte[is.available()];
        is.read(buffer);
        String json=new String(buffer,"utf-8");
        Gson gson=new Gson();
        Type listType=new TypeToken<List<WordInfo>>(){}.getType();
        List<WordInfo> wordInfos=gson.fromJson(json,listType);
        return wordInfos;
    }
}
