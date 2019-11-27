package com.dqndyvideo.widget.ac;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.dqndyvideo.R;
import com.dqndyvideo.interfac.OnDownListener;
import com.dqndyvideo.widget.controller.RotateInFullscreenController;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.player.VideoViewManager;

import java.io.File;

/**
 * Created by Devlin_n on 2017/5/31.
 */

public class FullScrellVideoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VideoView mVideoView;
    private RotateInFullscreenController controller;

    @Override
    protected void onPause() {
        super.onPause();
        VideoViewManager.instance().pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        VideoViewManager.instance().resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoViewManager.instance().release();
    }

    @Override
    public void onBackPressed() {
        if (!VideoViewManager.instance().onBackPressed()){
            super.onBackPressed();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screll_video);
        mVideoView = findViewById(R.id.video_player);
        controller = new RotateInFullscreenController(this);


        Intent intent = getIntent();
        if (intent.hasExtra("file")) {
            File data = (File) intent.getSerializableExtra("file");

            if (mVideoView != null ) {
                mVideoView.setVideoController(controller);
                mVideoView.setUrl(data.getAbsolutePath());
                mVideoView.start();
            }
        }

        controller.setOnShowListener(new OnDownListener() {
            @Override
            public void onDown() {
                if (mVideoView != null ) {
                    //VideoViewManager.instance().pause();
                    if(mVideoView.isPlaying()){
                        mVideoView.pause();
                        controller.show();
                        controller.setIsStartVideo(false);
                    }else {
                        //mVideoView.resume();
                        mVideoView.start();
                        controller.setIsStartVideo(true);
                    }

                }
            }

            @Override
            public void onDownShow() {




            }

            @Override
            public void onDownHide() {

            }
        });

    }
}
