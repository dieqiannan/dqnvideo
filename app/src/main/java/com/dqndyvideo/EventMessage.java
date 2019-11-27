package com.dqndyvideo;



/**
 * eventbus通知类
 */
public class EventMessage {


    //是否刷新商品详情
    public boolean isRefreshShopDetil;


    public EventMessage setRefreshShopDetil(boolean refreshShopDetil) {
        this.isRefreshShopDetil = refreshShopDetil;
        return this;
    }


}
