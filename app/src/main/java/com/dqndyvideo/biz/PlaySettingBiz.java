package com.dqndyvideo.biz;

import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;

import com.dqndyvideo.R;
import com.dqndyvideo.interfac.OnPlayProgressListener;

/**
 * 设置页面
 */
public class PlaySettingBiz implements View.OnClickListener {


    private static PlaySettingBiz  sInstance;
    private TextView tvBtnProgress0_2;
    private TextView tvBtnProgress0_4;
    private TextView tvBtnProgress0_6;
    private TextView tvBtnProgress0_8;
    private TextView tvBtnProgress1_0;
    private TextView tvBtnProgress1_2;
    private TextView tvBtnProgress1_4;
    private TextView tvBtnProgress1_6;
    private TextView tvBtnProgress1_8;
    private TextView tvBtnProgress2_0;

    private OnPlayProgressListener mProgresslistener;

    private PlaySettingBiz() {

    }
    public static PlaySettingBiz get() {

        return new PlaySettingBiz();
    }


    public void setView(View navView){

        tvBtnProgress0_2 = navView.findViewById(R.id.tv_btn_progress_0_2);
        tvBtnProgress0_2.setOnClickListener(this);

        tvBtnProgress0_4 = navView.findViewById(R.id.tv_btn_progress_0_4);
        tvBtnProgress0_4.setOnClickListener(this);

        tvBtnProgress0_6 = navView.findViewById(R.id.tv_btn_progress_0_6);
        tvBtnProgress0_6.setOnClickListener(this);

        tvBtnProgress0_8 = navView.findViewById(R.id.tv_btn_progress_0_8);
        tvBtnProgress0_8.setOnClickListener(this);

        tvBtnProgress1_0 = navView.findViewById(R.id.tv_btn_progress_1_0);
        tvBtnProgress1_0.setOnClickListener(this);

        tvBtnProgress1_2 = navView.findViewById(R.id.tv_btn_progress_1_2);
        tvBtnProgress1_2.setOnClickListener(this);

        tvBtnProgress1_4 = navView.findViewById(R.id.tv_btn_progress_1_4);
        tvBtnProgress1_4.setOnClickListener(this);

        tvBtnProgress1_6 = navView.findViewById(R.id.tv_btn_progress_1_6);
        tvBtnProgress1_6.setOnClickListener(this);

        tvBtnProgress1_8 = navView.findViewById(R.id.tv_btn_progress_1_8);
        tvBtnProgress1_8.setOnClickListener(this);

        tvBtnProgress2_0 = navView.findViewById(R.id.tv_btn_progress_2_0);
        tvBtnProgress2_0.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.tv_btn_progress_0_2:
                 if(mProgresslistener!=null){
                     mProgresslistener.setProgress(0.2f);
                 }
                 break;
             case R.id.tv_btn_progress_0_4:
                 if(mProgresslistener!=null){
                     mProgresslistener.setProgress(0.4f);
                 }
                 break;
             case R.id.tv_btn_progress_0_6:
                 if(mProgresslistener!=null){
                     mProgresslistener.setProgress(0.6f);
                 }
                 break;
             case R.id.tv_btn_progress_0_8:
                 if(mProgresslistener!=null){
                     mProgresslistener.setProgress(0.8f);
                 }
                 break;
             case R.id.tv_btn_progress_1_0:
                 if(mProgresslistener!=null){
                     mProgresslistener.setProgress(1.0f);
                 }
                 break;
             case R.id.tv_btn_progress_1_2:
                 if(mProgresslistener!=null){
                     mProgresslistener.setProgress(1.2f);
                 }
                 break;
             case R.id.tv_btn_progress_1_4:
                 if(mProgresslistener!=null){
                     mProgresslistener.setProgress(1.4f);
                 }
                 break;
             case R.id.tv_btn_progress_1_6:
                 if(mProgresslistener!=null){
                     mProgresslistener.setProgress(1.6f);
                 }
                 break;
             case R.id.tv_btn_progress_1_8:
                 if(mProgresslistener!=null){
                     mProgresslistener.setProgress(1.8f);
                 }
                 break;
             case R.id.tv_btn_progress_2_0:
                 if(mProgresslistener!=null){
                     mProgresslistener.setProgress(2.0f);
                 }
                 break;
         }
    }

    public void setPlayProgressChangerListener(OnPlayProgressListener listener){
        this.mProgresslistener =listener;
    }
}
