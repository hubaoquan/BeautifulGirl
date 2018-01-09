package cn.baoquanhu.beautifulgirl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Quincy on 2018/1/1.
 */

public class PicLoader {
    private ImageView loadImg;
    private String picUrl;
    private byte[] picByte;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0x123){
                if (picByte!=null){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(picByte,0,picByte.length);
                    loadImg.setImageBitmap(bitmap);
                }

            }
        }
    };
    //初始化imagview 控件，以及设置图片链接
    public void  loadImg(ImageView loadImg,String imgUrl){
        this.loadImg = loadImg;
        this.picUrl = imgUrl;
        Drawable drawable = loadImg.getDrawable();
        if (drawable!=null&&drawable instanceof BitmapDrawable){
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            if (bitmap!=null&&!bitmap.isRecycled()){
                bitmap.recycle();
            }
        }
        new Thread(runnable).start();
    }

    //此线程用于下载图片并将图片保存到picByte
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

                try {
                    URL url = new URL(picUrl);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(20000);
                    if (connection.getResponseCode()==200){
                        InputStream inputStream = connection.getInputStream();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] bytes = new byte[1024];
                        int length = -1;
                        while ((length=inputStream.read(bytes))!=-1)
                        {
                            byteArrayOutputStream.write(bytes,0,length);
                        }
                        picByte = byteArrayOutputStream.toByteArray();
                        inputStream.close();
                        connection.connect();
                        handler.sendEmptyMessage(0x123);
                        //通知Handler图片下载完成可以设置了
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
    };
}
