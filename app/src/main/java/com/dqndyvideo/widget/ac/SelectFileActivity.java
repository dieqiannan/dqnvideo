package com.dqndyvideo.widget.ac;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dqndyvideo.R;
import com.dqndyvideo.util.DataUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 选择文件
 * 所有视频文件
 */
public class SelectFileActivity extends AppCompatActivity {
    private final static String TAG = "SelectFileActivity";
    private RecyclerView rv;

    private ArrayList<File> list;
    private MyAdapter myAdapter;
    private HashMap<String, List<File>> fileMap;
    private ArrayList<String> fileNameList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);

        rv = findViewById(R.id.rv);

        list = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myAdapter = new MyAdapter();
        rv.setAdapter(myAdapter);
        rv.setLayoutManager(linearLayoutManager);

        final ScannerAnsyTask task = new ScannerAnsyTask();
        task.execute();

        //回退
        findViewById(R.id.tv_btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDownBack();
            }
        });

    }

    /**
     * 回退
     */
    private void onDownBack() {
        if (fileNameList == null || fileNameList.size() == 0) {
            return;
        }

        String lastName = fileNameList.get(fileNameList.size() - 1);
        List<File> files = fileMap.get(lastName);
        if (files != null && files.size() > 0) {
            list.clear();
            list.addAll(files);
        }

        //删除最后一个 地址
        fileNameList.remove(fileNameList.size() - 1);
        //修改selectFile

        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {

        if (fileNameList != null && fileNameList.size() > 0) {
            onDownBack();
            return;
        }

        super.onBackPressed();
    }

    class MyAdapter extends RecyclerView.Adapter<SelectFileActivity.MyHolder> {
        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = View.inflate(SelectFileActivity.this, R.layout.item_file_list, null);
            MyHolder myHolder = new MyHolder(view);
            return myHolder;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
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


                    if (file.isFile()) {
                        Intent intent = new Intent(SelectFileActivity.this, PlayVideoProgressActivity.class);
                        intent.putExtra("file", file);
                        startActivity(intent);

                    } else if (file.isDirectory()) {
                        if (file.listFiles() != null && file.listFiles().length > 0) {

                            if (fileMap == null) {
                                fileMap = new HashMap<>();
                            }
                            if (!fileMap.containsKey(file.getAbsolutePath())) {
                                ArrayList<File> fatherList = new ArrayList<>();
                                if (list.size() > 0) {
                                    fatherList.addAll(list);
                                }
                                fileMap.put(file.getAbsolutePath(), fatherList);
                            }

                            if (fileNameList == null) {
                                fileNameList = new ArrayList<>();
                            }
                            fileNameList.add(file.getAbsolutePath());

                            list.clear();
                            setFile2List(list, file);
                            myAdapter.notifyDataSetChanged();

                        }
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


    /**
     * 任务
     */
    public class ScannerAnsyTask extends AsyncTask<Void, Integer, List<File>> {

        private List<File> videoInfos = new ArrayList<File>();

        @Override
        protected List<File> doInBackground(Void... params) {

            String outSdPath = DataUtil.getOutSdPath(getApplicationContext());
            if (!TextUtils.isEmpty(outSdPath)) {

                File outSD = new File(outSdPath);
                setFile2List(videoInfos, outSD);
            }


            File storageDirectory = Environment.getExternalStorageDirectory();
            setFile2List(videoInfos, storageDirectory);


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
            if (videoInfos.size() > 0) {
                list.addAll(videoInfos);
                myAdapter.notifyDataSetChanged();
            }
        }

    }


    private void setFile2List(List<File> list, File file) {

        if (file != null
                && file.listFiles() != null
                && file.listFiles().length > 0) {

            for (int i = 0; i < file.listFiles().length; i++) {
                list.add(file.listFiles()[i]);
            }
        }

    }
}
