package com.dqndyvideo.widget.ac;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dqndyvideo.R;
import com.dqndyvideo.base.MyApplication;

public class StartActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 2;

    //相机的权限
    String[] mPermission = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start2);

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

        findViewById(R.id.tv_btn_video_progress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyApplication.isDyType = false;
                //音频
                Intent intent = new Intent(StartActivity.this, FirstVidwoActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_btn_video_dy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isDyType = true;
                //音频
                Intent intent = new Intent(StartActivity.this, FirstVidwoActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_btn_voice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //视频

            }
        });
    }
}
