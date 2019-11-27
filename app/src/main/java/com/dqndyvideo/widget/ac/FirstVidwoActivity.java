package com.dqndyvideo.widget.ac;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.dqndyvideo.R;
import com.dqndyvideo.util.DataUtil;
import com.dqndyvideo.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频文件选择的第一页
 */
public class FirstVidwoActivity extends AppCompatActivity {

    private final static String TAG = "FirstVidwoActivity";
    private static final int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 2;

    //相机的权限
    String[] mPermission = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private RecyclerView rv;
    private ArrayList<File> list;
    private MyAdapter myAdapter;
    private TextView tvBtnRefresh;

    private TextView tvBtnType;

    //是否是抖音班播放器
    private boolean isDyType = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_vidwo);

        rv = findViewById(R.id.rv);
        tvBtnRefresh = findViewById(R.id.tv_btn_refresh);


        //权限
        if (ContextCompat.checkSelfPermission(this, mPermission[0]) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, mPermission[0])) {

            } else {
                ActivityCompat.requestPermissions(this, mPermission, MY_PERMISSIONS_REQUEST_CALL_CAMERA);
            }
        } else {
            //MyLogUtils.e(TAG, "相机有权限");
            //权限通过了
        }

        //List<TiktokBean> mVideoList = DataUtil.getTiktokDataFromSd(this);

        //list = DataUtil.getDataFromSd(this);
        list = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myAdapter = new MyAdapter();
        rv.setAdapter(myAdapter);
        rv.setLayoutManager(linearLayoutManager);


        tvBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                final ScannerAnsyTask task = new ScannerAnsyTask();
                task.execute();
            }
        });

        //选择所有文件文件
        findViewById(R.id.tv_btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstVidwoActivity.this, SelectFileActivity.class);

                startActivity(intent);
            }
        });

        //选择视频文件
        findViewById(R.id.tv_btn_video_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstVidwoActivity.this, SelectVideoOneActivity.class);

                startActivity(intent);
            }
        });

        final ScannerAnsyTask task = new ScannerAnsyTask();
        task.execute();


        tvBtnType = findViewById(R.id.tv_btn_type);


        //默认抖音版本
        isDyType = true;
        tvBtnType.setText("抖音/进度版  抖音版");

        tvBtnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDyType){
                    isDyType =false;
                    tvBtnType.setText("抖音/进度版  进度版");
                }else {
                    isDyType =true;
                    tvBtnType.setText("抖音/进度版  抖音版");
                }
            }
        });

    }

    class MyAdapter extends RecyclerView.Adapter<MyHolder> {
        @NonNull
        @Override
        public FirstVidwoActivity.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = View.inflate(FirstVidwoActivity.this, R.layout.item_file_list, null);
            MyHolder myHolder = new MyHolder(view);
            return myHolder;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onBindViewHolder(@NonNull FirstVidwoActivity.MyHolder myHolder, int i) {
            final File file = list.get(i);
            if (file == null) {
                myHolder.tvName.setText("");
            }
            int size = 0;
            if (file.list() != null) {
                size = file.list().length;
            }

            if (size > 0) {
                myHolder.tvName.setText("" + file.getName() + "(" + size + ")");
            } else {
                myHolder.tvName.setText("" + file.getName());
            }


            myHolder.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(isDyType){
                        Intent intent = new Intent(FirstVidwoActivity.this, PlayVideoActivity.class);
                        intent.putExtra("file", file);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(FirstVidwoActivity.this, PlayVideoProgressActivity.class);
                        intent.putExtra("file", file);
                        startActivity(intent);
                    }

                }
            });
        }


    }

    class MyHolder extends RecyclerView.ViewHolder {
        public TextView tvName;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }


    public class ScannerAnsyTask extends AsyncTask<Void, Integer, List<File>> {

        private ArrayList<File> videoInfos = new ArrayList<File>();

        @Override
        protected List<File> doInBackground(Void... params) {

            videoInfos.clear();
            Log.e(TAG, "videoInfos 大小" + videoInfos.size());
            String outSdPath = DataUtil.getOutSdPath(getApplicationContext());

            if (!TextUtils.isEmpty(outSdPath)) {
                 Utils.getVideoFile(videoInfos, new File(outSdPath));
            }

            Log.e(TAG, "videoInfos 大小" + videoInfos.size());
            File storageDirectory = Environment.getExternalStorageDirectory();
            if (storageDirectory != null) {
                Utils.getVideoFile(videoInfos, storageDirectory);
            }


            //videoInfos = filterVideo(videoInfos);
            Log.e(TAG, "最后的大小" + videoInfos.size());
            return videoInfos;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<File> videoInfos) {
            super.onPostExecute(videoInfos);

            list.clear();
            if (videoInfos.size() > 0) {
                list.addAll(videoInfos);
            }

            myAdapter.notifyDataSetChanged();
        }




        /**
         * 10M=10485760 b,小于10m的过滤掉
         * 过滤视频文件
         *
         * @param videoInfos
         * @return
         */
        /*private List<TiktokBean> filterVideo(List<TiktokBean> videoInfos) {
            List<TiktokBean> newVideos = new ArrayList<TiktokBean>();
            for (VideoInfo videoInfo : videoInfos) {
                File f = new File(videoInfo.getPath());
                if (f.exists() && f.isFile() && f.length() > 10485760) {
                    newVideos.add(videoInfo);
                    Log.i("TGA", "文件大小" + f.length());
                } else {
                    Log.i("TGA", "文件太小或者不存在");
                }
            }
            return newVideos;
        }*/
    }


}
