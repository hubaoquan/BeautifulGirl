package cn.baoquanhu.beautifulgirl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button showBtn;
    private Button refreshBtn;
    private ImageView showImg;

    private ArrayList<Sister> data;
    private int curPos = 0; //当前显示的是哪一张
    private int page = 1;   //当前页数
    private PicLoader loader;
    private SisterApi sisterApi;
    private SisterTask sisterTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sisterApi = new SisterApi();
        loader = new PicLoader();
        Log.e("", "initUI: ");
        initUI();

        initData();

    }

    private void initData() {
        data = new ArrayList<>();
        sisterTask = new SisterTask();
        sisterTask.execute();

    }

    private void initUI() {
        showBtn = (Button) findViewById(R.id.but_next);
        refreshBtn = (Button) findViewById(R.id.but_pre);
        showImg = (ImageView) findViewById(R.id.img_show);
        showBtn.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
        Log.e("", "initUI: ");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_next:

                if(data != null && !data.isEmpty()) {
                    if (curPos > 9) {
                        curPos = 0;
                    }
                    Log.e("Main",data.get(curPos).getUrl());
                    loader.loadImg(showImg, data.get(curPos).getUrl());
                    curPos++;
                }
                break;
            case R.id.but_pre:
                sisterTask = new SisterTask();
                sisterTask.execute();
                curPos = 0;
                break;
            default:
                break;
        }
    }

    private class SisterTask extends AsyncTask<Void,Void,ArrayList<Sister>> {

        public SisterTask() { }

        @Override
        protected ArrayList<Sister> doInBackground(Void... params) {
            ArrayList<Sister> sisters  = new ArrayList<Sister>();


            sisters =  sisterApi.fetchSister(10,page);

            return sisters;
        }

        @Override
        protected void onPostExecute(ArrayList<Sister> sisters) {
            super.onPostExecute(sisters);
            data.clear();

            data.addAll(sisters);
            Log.e("g", "onPostExecute: "+data.get(0).getUrl() );
            page++;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            sisterTask = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sisterTask.cancel(true);
    }
}
