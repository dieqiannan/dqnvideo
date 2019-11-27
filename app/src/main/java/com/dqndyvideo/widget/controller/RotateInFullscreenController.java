package com.dqndyvideo.widget.controller;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.dqndyvideo.interfac.OnDownListener;
import com.dqndyvideo.R;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.PlayerUtils;

public class RotateInFullscreenController extends StandardVideoController {

    @Nullable
    private Activity mActivity;
    private OnDownListener onDownListener1;

    //师傅需要全屏
    public boolean isNeedFullScreen = false;

    public RotateInFullscreenController(@NonNull Context context) {
        this(context, null);
    }

    public RotateInFullscreenController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateInFullscreenController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mActivity = PlayerUtils.scanForActivity(context);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {

        int o = mActivity.getRequestedOrientation();
        if (o == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            //横屏

            isNeedFullScreen = true;
            if(!mMediaPlayer.isFullScreen()){
                mMediaPlayer.startFullScreen();
            }

        } else {
            //竖屏

            isNeedFullScreen = false;
            if(mMediaPlayer.isFullScreen()){
                mMediaPlayer.stopFullScreen();
            }

        }

       /* if (!mMediaPlayer.isFullScreen() && isNeedFullScreen) {
            mMediaPlayer.startFullScreen();
            return true;
        }*/

        if (onDownListener1 != null) {
            onDownListener1.onDown();
        }


        if (mShowing) {
            hide();
        } else {
            show();
        }
        return true;
    }

    @Override
    protected void doStartStopFullScreen() {
        if (mActivity == null) return;
        int o = mActivity.getRequestedOrientation();

        if (o == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            //横屏
            //强制为竖屏
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isNeedFullScreen = false;
            mMediaPlayer.stopFullScreen();
        } else {
            //竖屏
            //强制为横屏

            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            isNeedFullScreen = true;
            mMediaPlayer.startFullScreen();
        }
        mFullScreenButton.setSelected(o == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void setPlayerState(int playerState) {
        super.setPlayerState(playerState);
        switch (playerState) {
            case VideoView.PLAYER_FULL_SCREEN:
                mFullScreenButton.setSelected(false);
                getThumb().setVisibility(GONE);
                mOrientationHelper.disable();

                break;
            case VideoView.PLAYER_NORMAL:
            default:
                mOrientationHelper.disable();
                break;
        }
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.fullscreen) {
            doStartStopFullScreen();
        } else if (i == R.id.lock) {
            doLockUnlock();
        } else if (i == R.id.iv_play) {
            doPauseResume();
        } else if (i == R.id.back) {
            stopFullScreenFromUser();
        } else if (i == R.id.thumb) {
            mMediaPlayer.start();
            if(isNeedFullScreen){
                mMediaPlayer.startFullScreen();
            }
        } else if (i == R.id.iv_replay) {
            mMediaPlayer.replay(true);

            if(isNeedFullScreen){
                mMediaPlayer.startFullScreen();
            }

        }
    }

    @Override
    public boolean onBackPressed() {
        if (mIsLocked) {
            show();
            Toast.makeText(getContext(), R.string.dkplayer_lock_tip, Toast.LENGTH_SHORT).show();
            return true;
        }
        if (mMediaPlayer.isFullScreen()) {
            stopFullScreenFromUser();
            return true;
        }
        return super.onBackPressed();
    }


    public void setOnShowListener(OnDownListener onDownListener) {
        onDownListener1 = onDownListener;
    }

    public void setIsStartVideo(boolean isStartVideo) {
        this.isStartVideo = isStartVideo;
    }

    /**
     * 是否需要全屏
     *
     * @param isNeedFullScreen
     */
    public void setIsNeedFullScreen(boolean isNeedFullScreen) {
        this.isNeedFullScreen = isNeedFullScreen;
    }
}