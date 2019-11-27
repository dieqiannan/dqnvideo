package com.dqndyvideo.widget.ac;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;


import com.dqndyvideo.R;
import com.dqndyvideo.TiktokBean;
import com.dqndyvideo.VerticalViewPager;
import com.dqndyvideo.adapter.Tiktok2Adapter;
import com.dqndyvideo.base.MyBaseActivity;
import com.dqndyvideo.util.DataUtil;
import com.dqndyvideo.util.MyLogUtils;
import com.dqndyvideo.util.cache.PreloadManager;
import com.dqndyvideo.util.cache.ProxyVideoCacheManager;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.player.VideoViewManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayVideoActivity extends MyBaseActivity {
private final static String TAG = "PlayVideoActivity";
    private int mCurrentPosition;
    private int mPlayingPosition;
    private List<TiktokBean> mVideoList;
    private Tiktok2Adapter mTiktok2Adapter;
    private VerticalViewPager mViewPager;

    private PreloadManager mPreloadManager;

    /**
     * VerticalViewPager是否反向滑动
     */
    private boolean mIsReverseScroll;
    private static final int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 2;

    //相机的权限
    String[] mPermission = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //StatusBarUtil.setTranslucent(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play);


        mPreloadManager = PreloadManager.getInstance(this);

        //mVideoList = DataUtil.getTiktokDataFromAssets(this);
        mVideoList = new ArrayList<>();

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
            MyLogUtils.e(TAG,"位置 mPosition="+mPosition);
            if(mPosition>0){
                mViewPager.setCurrentItem(mPosition);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }


    private void initViewPager() {
        mViewPager = findViewById(R.id.vvp);

        mViewPager.setOffscreenPageLimit(4);

        mTiktok2Adapter = new Tiktok2Adapter(mVideoList);
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
            Tiktok2Adapter.ViewHolder viewHolder = (Tiktok2Adapter.ViewHolder) itemView.getTag();
            if (viewHolder.mPosition == position) {
                VideoView videoView = itemView.findViewById(R.id.video_view);
                TiktokBean tiktokBean = mVideoList.get(position);
                String playUrl = mPreloadManager.getPlayUrl(tiktokBean.videoDownloadUrl);

                Log.e("", "startPlay: " + "position: " + position + "  url: " + playUrl);
                videoView.setUrl(playUrl);
                videoView.start();
                mPlayingPosition = position;
                break;
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoViewManager.instance().release();
        mPreloadManager.removeAllPreloadTask();

        //清除缓存，实际使用可以不需要清除，这里为了方便测试
        ProxyVideoCacheManager.clearAllCache(this);
    }

    public void addData(View view) {
        mVideoList.addAll(DataUtil.getTiktokDataFromAssets(this));
        mTiktok2Adapter.notifyDataSetChanged();
    }

}
