package com.dqndyvideo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dqndyvideo.R;
import com.dqndyvideo.TiktokBean;
import com.dqndyvideo.interfac.OnDownListener;
import com.dqndyvideo.util.MyLogUtils;
import com.dqndyvideo.util.cache.PreloadManager;
import com.dqndyvideo.widget.controller.RotateInFullscreenController;
import com.dueeeke.videoplayer.player.VideoView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Tiktok2ProgressAdapter extends PagerAdapter {

    private final static String TAG = "Tiktok2ProgressAdapter";

    /**
     * View缓存池，从ViewPager中移除的item将会存到这里面，用来复用
     */
    private List<View> mViewPool = new ArrayList<>();

    /**
     * 数据源
     */
    private List<TiktokBean> mVideoBeans;
    public float speed = 1f;

    public Tiktok2ProgressAdapter(List<TiktokBean> videoBeans) {
        this.mVideoBeans = videoBeans;
    }

    @Override
    public int getCount() {
        return mVideoBeans == null ? 0 : mVideoBeans.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Context context = container.getContext();
        View view = null;

        MyLogUtils.e(TAG,"instantiateItem");

        if (mViewPool.size() > 0) {//取第一个进行复用
            view = mViewPool.get(0);
            mViewPool.remove(0);
        }

        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_tik_tok_2_progress, container, false);
            viewHolder = new ViewHolder(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        TiktokBean item = mVideoBeans.get(position);
        Log.e(TAG, "instantiateItem");
        //开始预加载
        PreloadManager.getInstance(context).addPreloadTask(item, item.videoDownloadUrl, position);
        if (!TextUtils.isEmpty(item.coverImgUrl)) {
            Glide.with(context)
                    .load(item.coverImgUrl)
                    .placeholder(android.R.color.white)
                    .into(viewHolder.mThumb);

        } else if (item.bt != null) {
            Log.e(TAG, "有bt图片");
            //viewHolder.mThumb.setImageBitmap(item.bt);
        }
        viewHolder.mTitle.setVisibility(View.GONE);
        MyLogUtils.e(TAG, "item.title=" + item.title);
        viewHolder.mTitle.setText(item.title);
        viewHolder.mTitle.setVisibility(View.VISIBLE);
        viewHolder.mPosition = position;
        //不需要全屏
        viewHolder.mTikTokController.setIsNeedFullScreen(false);
        viewHolder.mTikTokController.setTitle(item.title);
        viewHolder.mTikTokController.setOnShowListener(new MyOnDownListener(viewHolder));

        viewHolder.mVideoView.setMediaPlayerSpeed(speed);



        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View itemView = (View) object;
        container.removeView(itemView);
        TiktokBean item = mVideoBeans.get(position);
        //取消预加载
        PreloadManager.getInstance(container.getContext()).removePreloadTask(item.videoDownloadUrl);
        //保存起来用来复用
        mViewPool.add(itemView);
    }


    /**
     * 播放速度
     *
     * @param speed
     */
    public void setMediaPlayerSpeed(float speed) {
        this.speed = speed;

    }

    /**
     * 借鉴ListView item复用方法
     */
    public static class ViewHolder {

        public RotateInFullscreenController mTikTokController;
        public VideoView mVideoView;
        public int mPosition;
        public TextView mTitle;//标题
        public ImageView mThumb;//封面图

        ViewHolder(View itemView) {
            mVideoView = itemView.findViewById(R.id.video_view);
            //mVideoView.setScreenScaleType(1);
            //mVideoView.setLooping(true);
            //mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_CENTER_CROP);
            mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_DEFAULT);
            //mTikTokController = new TikTokController(itemView.getContext());
            mTikTokController = new RotateInFullscreenController(itemView.getContext());
            mVideoView.setVideoController(mTikTokController);
            mTitle = itemView.findViewById(R.id.tv_file_name);
            mThumb = mTikTokController.findViewById(R.id.iv_thumb);
            itemView.setTag(this);
        }
    }

    class MyOnDownListener implements OnDownListener {
        ViewHolder viewHolder;

        public MyOnDownListener(ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        public void onDown() {
            if (viewHolder.mVideoView != null) {
                //VideoViewManager.instance().pause();
                if (viewHolder.mVideoView.isPlaying()) {
                    viewHolder.mVideoView.pause();
                    viewHolder.mTikTokController.show();
                    viewHolder.mTikTokController.setIsStartVideo(false);
                } else {
                    //mVideoView.resume();
                    //viewHolder.mTikTokController.show();
                    viewHolder.mVideoView.start();
                    viewHolder.mTikTokController.setIsStartVideo(true);
                }

            }
        }

        @Override
        public void onDownShow() {

        }

        @Override
        public void onDownHide() {

        }
    }
}
