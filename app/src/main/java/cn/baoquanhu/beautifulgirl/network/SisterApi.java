package cn.baoquanhu.beautifulgirl.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cn.baoquanhu.beautifulgirl.bean.entity.Sister;

/**
 * Created by Quincy on 2018/1/9.
 */

public class SisterApi {
    private static final String BASE_URL="http://gank.io/api/data/福利/";

    public ArrayList<Sister> fetchSister(int count, int page){
        ArrayList<Sister> sisters = new ArrayList<Sister>();
        String fetchurl = BASE_URL+count+"/"+ page;
        try {
            URL url = new URL(fetchurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            int rescode = connection.getResponseCode();
            if(rescode ==200){
                InputStream inputStream = connection.getInputStream();
                byte[] data = readFromStream(inputStream);
                String result = new String(data,"UTF-8");
                sisters = parseSister(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sisters;
    }

    private ArrayList<Sister> parseSister(String result) throws Exception{
        ArrayList<Sister> sisters = new ArrayList<Sister>();
        JSONObject object = new JSONObject(result);
        JSONArray jsonArray = object.getJSONArray("results");
        for (int i = 0;i<jsonArray.length();i++)
        {
            JSONObject results = (JSONObject)jsonArray.get(i);
            Sister sister = new Sister();
            sister.setUrl(results.getString("url"));
            sisters.add(sister);
            Log.e("MAin", "parseSister: url"+results.getString("url"));
        }

        return sisters;
    }

    private byte[] readFromStream (InputStream inputStream) throws Exception{
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len;
        while((len = inputStream.read(buffer))!=-1){
            outputStream.write(buffer,0,len);
        }
        inputStream.close();

        return outputStream.toByteArray();
    }

}
