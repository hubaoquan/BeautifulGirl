package cn.baoquanhu.beautifulgirl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView imgShow;

    Button butPre;

    Button butNext;
    private ArrayList<String> urlsBank;
    private PicLoader loader;
    int cur = 1;

    int picNumbers=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setUrlsBank();
        picNumbers =urlsBank.size();
        Log.e("Mainactivity", "onCreate: "+urlsBank.get(2) );
        loader = new PicLoader();
        loader.loadImg(imgShow,urlsBank.get(picNumbers-1));

    }

    public void initView(){
        imgShow = findViewById(R.id.img_show);
        butPre = findViewById(R.id.but_pre);
        butNext = findViewById(R.id.but_next);
        butNext.setOnClickListener(this);
        butPre.setOnClickListener(this);
    }
    public void setUrlsBank() {
        urlsBank = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            getApplicationContext()
                            .getAssets()
                            .open("susanphoto.txt")
                    ));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
               urlsBank.add(s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_pre:
                --cur;
                Log.e("Mainactivity", "url ---"+cur );
                loader.loadImg(imgShow,urlsBank.get(cur%picNumbers));

                break;
            case R.id.but_next:
                ++cur;
                Log.e("Mainactivity", "url ---"+cur );
                loader.loadImg(imgShow,urlsBank.get(cur%picNumbers));

                break;
            default:
                break;
        }
    }
}
