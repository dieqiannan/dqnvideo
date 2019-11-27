package com.dqndyvideo.widget.ac;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dqndyvideo.R;
import com.dqndyvideo.TiktokBean;
import com.dqndyvideo.VerticalViewPager;
import com.dqndyvideo.adapter.Tiktok2ProgressAdapter;
import com.dqndyvideo.base.MyBaseActivity;
import com.dqndyvideo.biz.PlaySettingBiz;
import com.dqndyvideo.interfac.OnPlayProgressListener;
import com.dqndyvideo.util.DataUtil;
import com.dqndyvideo.util.MyLogUtils;
import com.dqndyvideo.util.cache.PreloadManager;
import com.dqndyvideo.widget.controller.RotateInFullscreenController;
import com.dueeeke.videoplayer.BuildConfig;
import com.dueeeke.videoplayer.ijk.IjkPlayerFactory;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.player.VideoViewConfig;
import com.dueeeke.videoplayer.player.VideoViewManager;

import java.io.File;
import java.util.List;

/**
 * Created by Devlin_n on 2017/5/31.
 */

public class PlayVideoProgressActivity extends MyBaseActivity {
private final static String TAG = "PlayVideoProgressActivity";
    private int mCurrentPosition;
    private int mPlayingPosition;
    private List<TiktokBean> mVideoList;
    private Tiktok2ProgressAdapter mTiktok2Adapter;
    private VerticalViewPager mViewPager;

    private PreloadManager mPreloadManager;

    /**
     * VerticalViewPager是否反向滑动
     */
    private boolean mIsReverseScroll;

    private RotateInFullscreenController controller;
    private int mPosition;
    private TextView tvBtnSetting;
    public DrawerLayout drawer;
    private PlaySettingBiz playSettingBiz;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_progress);

        tvBtnSetting = findViewById(R.id.tv_btn_setting);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mPreloadManager = PreloadManager.getInstance(this);

        controller = new RotateInFullscreenController(this);


        playSettingBiz = PlaySettingBiz.get();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navHeaderView = navigationView.getHeaderView(0);
        playSettingBiz.setView(navHeaderView);



        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                .setLogEnabled(BuildConfig.DEBUG)
//                .setPlayerFactory(IjkPlayerFactory.create())
                .setPlayerFactory(IjkPlayerFactory.create())
//                .setEnableOrientation(true)
//                .setEnableMediaCodec(true)
//                .setUsingSurfaceView(true)
//                .setEnableParallelPlay(true)
//                .setEnableAudioFocus(false)
//                .setScreenScaleType(VideoView.SCREEN_SCALE_MATCH_PARENT)
                .build());

        Intent intent = getIntent();
        if (intent.hasExtra("file")) {
            File data = (File) intent.getSerializableExtra("file");

            mVideoList = DataUtil.getTiktokData(data);
            initViewPager();

        }

        if (intent.hasExtra("fileList")) {
            List<File> data = (List<File>) intent.getSerializableExtra("fileList");
            mPosition = intent.getIntExtra("position",0);
            mVideoList = DataUtil.getTiktokData(data);
            initViewPager();

            MyLogUtils.e(TAG,"位置 mPosition="+ mPosition);
            if(mPosition >0){
                mViewPager.setCurrentItem(mPosition);
            }
        }



        setLisener();
    }

    private void setLisener(){
        tvBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawer.isDrawerOpen(GravityCompat.START)){
                    closeDrawerLayout();
                    return;
                }
                openDrawerLayout();
            }
        });


        /**
         * 调整播放速度
         */
        playSettingBiz.setPlayProgressChangerListener(new OnPlayProgressListener() {
            @Override
            public void setProgress(float speed) {
                if(mTiktok2Adapter != null){
                    mTiktok2Adapter.setMediaPlayerSpeed(speed);
                    setPlaySpeed(speed);

                    MyLogUtils.e(TAG,"修改播放速度 speed="+speed);

                    //mViewPager.notifyAll();
                }
            }
        });
    }

    /**
     * 关闭侧边栏
     */
    public void closeDrawerLayout() {
        if (drawer == null) {
            return;
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    /**
     * 打开侧边栏
     */
    public void openDrawerLayout() {
        if (drawer == null) {
            return;
        }
        drawer.openDrawer(GravityCompat.START);
    }

    private void initViewPager() {
        mViewPager = findViewById(R.id.vvp);
        mViewPager.setOffscreenPageLimit(4);
        mTiktok2Adapter = new Tiktok2ProgressAdapter(mVideoList);
        mViewPager.setAdapter(mTiktok2Adapter);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (position > mPlayingPosition) {
                    mIsReverseScroll = false;
                } else if (position < mPlayingPosition) {
                    mIsReverseScroll = true;
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentPosition = position;
                if (position == mPlayingPosition) return;
                startPlay(position);

                if(mTiktok2Adapter.speed != 1.0f){
                    mViewPager.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setPlaySpeed(mTiktok2Adapter.speed);
                        }
                    },1000);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (mCurrentPosition == mPlayingPosition) return;
                if (state == VerticalViewPager.SCROLL_STATE_IDLE) {
                    mPreloadManager.resumePreload(mCurrentPosition, mIsReverseScroll);
                } else {
                    mPreloadManager.pausePreload(mCurrentPosition, mIsReverseScroll);
                }
            }
        });


        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                startPlay(mPosition);
            }
        });


    }

    private void startPlay(int position) {
        int count = mViewPager.getChildCount();
        for (int i = 0; i < count; i++) {
            View itemView = mViewPager.getChildAt(i);
            Tiktok2ProgressAdapter.ViewHolder viewHolder = (Tiktok2ProgressAdapter.ViewHolder) itemView.getTag();
            if (viewHolder.mPosition == position) {
                VideoView videoView = itemView.findViewById(R.id.video_view);
                TiktokBean tiktokBean = mVideoList.get(position);
                String playUrl = mPreloadManager.getPlayUrl(tiktokBean.videoDownloadUrl);

                MyLogUtils.e(TAG, "startPlay: " + "position: " + position + "  url: " + playUrl);
                videoView.setUrl(playUrl);
                videoView.setMediaPlayerSpeed(mTiktok2Adapter.speed);
                videoView.start();

                mPlayingPosition = position;
                break;
            }
        }
    }

    /**
     * 设置加载进度
     * @param speed
     */
    private void setPlaySpeed(float speed) {

        int count = mViewPager.getChildCount();
        for (int i = 0; i < count; i++) {
            View itemView = mViewPager.getChildAt(i);
            Tiktok2ProgressAdapter.ViewHolder viewHolder = (Tiktok2ProgressAdapter.ViewHolder) itemView.getTag();
            if (viewHolder.mPosition == mPlayingPosition) {
                //Tiktok2ProgressAdapter.ViewHolder viewHolder = (Tiktok2ProgressAdapter.ViewHolder) itemView.getTag();
                VideoView videoView = itemView.findViewById(R.id.video_view);
                videoView.setSpeed(speed);
                break;
            }
        }
    }
}
