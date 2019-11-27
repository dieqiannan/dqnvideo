package com.dqndyvideo.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dqndyvideo.R;
import com.dqndyvideo.base.MyApplication;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作UI工具类
 */
public class UiUtils {
    private final static String TAG = "UiUtils";

    public static Context getContext() {
        //有可能 app出现cash 重启, 导致这个为空

        return MyApplication.context;
    }

    //获取主线程的handler对象
    public static Handler getMainThreadHandler() {
        return MyApplication.mHanlder;
    }

    //获取主线程的线程id
    public static int getMainThreadId() {
        return MyApplication.mainThreadId;
    }

    //获取字符串资源
    public static String getString(int resId) {
        return getContext().getResources().getString(resId);
    }

    //获取字符串数组
    public static String[] getStringArray(int resId) {
        return getContext().getResources().getStringArray(resId);
    }

    //获取Drawable
    public static Drawable getDrawable(int resId) {
        return getContext().getResources().getDrawable(resId);

    }

    //获取color
    public static int getColor(int resId) {
        return getContext().getResources().getColor(resId);
    }

    //获取颜色的状态选择器
    public static ColorStateList getColorStateList(int resId) {
        // TODO Auto-generated method stub
        return getContext().getResources().getColorStateList(resId);
    }

    //获取dimen
    public static int getDimen(int resId) {
        return (int) getContext().getResources().getDimension(resId);
    }

    public static float getDimen2(int resId) {
        return getContext().getResources().getDimension(resId);
    }

    public static int getDimenPixelSize(int resId) {
        return getContext().getResources().getDimensionPixelSize(resId);
    }


    //dip2px
    public static int dip2px(int dip) {
        //获取设备密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    /**
     * 将px值转换为sp值
     */
    public static int px2sp(float pxValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics
                ().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值
     */
    public static int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics
                ().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    //px2dip
    public static int px2dip(int px) {
        //获取设备密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    //Android中能操作ui的线程是主线程
    public static boolean isRunOnUiThread() {
        //1、获取当前线程id
        int currentThreadId = android.os.Process.myTid();
        //2、获取主线程的id
        int mainThreadId = getMainThreadId();
        //3、比较
        return currentThreadId == mainThreadId;
    }


    //Thread:线程
    //Runnable：任务

    //保证某一些操作一定运行在主线程中
    public static void runOnUiThread(Runnable r) {

        if (isRunOnUiThread()) {
            //主线程
            //new Thread(r).start();
            r.run();
        } else {
            //把r丢到主线程的消息队列
            getMainThreadHandler().post(r);
        }

    }

    /**
     * 延时
     * @param r
     */
    //保证某一些操作一定运行在主线程中
    public static void postDelayed(Runnable r,long delayMillis) {

        getMainThreadHandler().postDelayed(r,delayMillis);

    }

    public static int getScreenWidth() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }


    //运行在后台子线程
    public static void runOnBackgroundThread(Runnable r) {
        new Thread(r).start();
    }


    /**
     * 获取软件版本号
     *
     * @param
     */
    public static int getVersionCode() {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            PackageInfo info = getContext().getPackageManager()
                    .getPackageInfo(getContext().getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    /**
     * 获取app名字
     *
     * @param
     */
    public static String getAppName() {
        return getString(R.string.app_name);
    }


    public static String getEncode(String conent) {
        // LogUtils.e(TAG,"编码的数据conent = "+conent);
        //LogUtils.e(TAG,"编码的Base64 = "+Base64.encodeToString(conent.getBytes(), Base64.DEFAULT));
        if (TextUtils.isEmpty(conent)) {
            return "";
        }
        return Base64.encodeToString(conent.getBytes(), Base64.DEFAULT);
    }


    public static String getDecode(String conent) {
        //LogUtils.e(TAG,"解码的数据conent = "+conent);
        //LogUtils.e();
        //byte[] decode = Base64.decode(conent.getBytes(), Base64.DEFAULT);

        //LogUtils.e(TAG,"解码的Base64 = "+new String(decode));
        if (TextUtils.isEmpty(conent)) {
            return "";
        }
        return new String(Base64.decode(conent.getBytes(), Base64.DEFAULT));
    }


    /**
     * 根据颜色得到图片
     */
    public static GradientDrawable getShape(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);//设置矩形的类型
        drawable.setColor(color);
        drawable.setSize(UiUtils.dip2px(45), UiUtils.dip2px(45));
        return drawable;
    }

    /**
     * 根据颜色得到图片
     */
    public static GradientDrawable getShapeRectangle(int color, int width, int height) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);//设置矩形的类型
        drawable.setColor(color);
        drawable.setSize(width, height);
        return drawable;
    }

    /**
     * 根据颜色得到图片
     */
    public static GradientDrawable getShapeRectangle02(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);//设置矩形的类型
        drawable.setColor(color);
        return drawable;
    }

    /**
     * 状态选择器
     */
    public static StateListDrawable getSelector(Drawable pressedDrawable,
                                                Drawable normalDrawable) {
        StateListDrawable selector = new StateListDrawable();
        selector.addState(new int[]{android.R.attr.state_pressed},
                pressedDrawable);
        selector.addState(new int[]{}, normalDrawable);
        return selector;
    }


    /**
     * 去打电话
     */
    public static void goCallPhone(Context context, String phoneNum) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNum));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static String getPhoneType() {
        //手机型号
        String brand = Build.MODEL;
        MyLogUtils.e(TAG, "手机型号 =" + brand);
        return brand;
    }

    public static String getPhoneBrand() {
        //品牌
        String brand = Build.BRAND;
        return brand;
    }


    /**
     * 关闭软键盘
     */
    public static void closeKeyboard(View view) {
        /* 隐藏软键盘 */
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService
                (Context
                        .INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(
                    view.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 编辑框获取焦点，并显示软件盘
     */
    public static void showSoftInput(final EditText et) {

        final InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                .getSystemService
                        (Context.INPUT_METHOD_SERVICE);
        et.requestFocus();
        et.post(new Runnable() {
            @Override
            public void run() {
                inputMethodManager.showSoftInput(et, 0);
            }
        });
    }

    /**
     * 隐藏键盘
     */
    public static void hideKeyBoard(Activity activity) {
        //context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 跳转浏览器
     */
    public static void go2Browser(Activity mAc, String mUrl) {
        try {
            Intent intent = new Intent();
            //Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setAction("android.intent.action.VIEW");
            //Uri content_url = Uri.parse("此处填链接");
            Uri content_url = Uri.parse(mUrl);
            intent.setData(content_url);
            mAc.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    /**
     * 隐藏键盘
     */
    public void hideKeyBoard(View view) {
        //context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);


    }


    /**
     * scrollView滚动到顶部
     *
     * @param sv
     */
    public static void scrollViewToTop(final NestedScrollView sv) {
        sv.post(new Runnable() {

            @Override
            public void run() {
                sv.fullScroll(sv.FOCUS_UP);
                sv.scrollTo(0, 0);
            }
        });
    }

    // //需要先测量，PopupWindow还未弹出时，宽高为0
    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }

    /**
     * 手机是否静音
     *
     * @return
     */
    public static boolean isSilent() {
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        int mode = audioManager.getRingerMode();
       /* switch (mode) {
            case AudioManager.RINGER_MODE_NORMAL:
                //普通模式
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                //振动模式
                break;
            case AudioManager.RINGER_MODE_SILENT:
                //静音模式
                break;
        }*/

        return mode == AudioManager.RINGER_MODE_SILENT || mode == AudioManager.RINGER_MODE_VIBRATE;
    }

    /**
     * 手机是否静音
     *
     * @return
     */
    public static boolean isVoice() {
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        int mode = audioManager.getRingerMode();
       /* switch (mode) {
            case AudioManager.RINGER_MODE_NORMAL:
                //普通模式
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                //振动模式
                break;
            case AudioManager.RINGER_MODE_SILENT:
                //静音模式
                break;
        }*/

        return mode == AudioManager.RINGER_MODE_NORMAL;
    }

    /**
     * 判断当前activity是否为全屏
     *
     * @param activity 当前activity
     * @return 是否全屏
     */
    public static boolean isActivityFullscreen(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        if ((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return true;
        } else {
            return false;
        }
    }

    public static void showDialog(Context context, Dialog dialog) {
        if (context != null
                && context instanceof Activity
                && !((Activity) context).isFinishing()) {
            dialog.show();
        }
    }

    public static void showDialog(Activity ac, Dialog dialog) {
        if (ac != null && !ac.isFinishing()) {
            dialog.show();
        }
    }

    /**
     * 手动测量摆放View
     * 对于手动 inflate 或者其他方式代码生成加载的View进行测量，避免该View无尺寸
     *
     * @param v
     * @param width
     * @param height
     */
    public static void layoutView(View v, int width, int height) {
        // validate view.width and view.height
        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        // validate view.measurewidth and view.measureheight
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }


    /**
     * 对ScrollView进行截图
     *
     * @param scrollView
     * @return
     */
    public static Bitmap shotScrollView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }


    /**
     * 对ListView进行截图
     * http://stackoverflow.com/questions/12742343/android-get-screenshot-of-all-listview-items
     */
    public static Bitmap shotListView(ListView listview) {

        ListAdapter adapter = listview.getAdapter();
        int itemscount = adapter.getCount();
        int allitemsheight = 0;
        List<Bitmap> bmps = new ArrayList<Bitmap>();

        for (int i = 0; i < itemscount; i++) {

            View childView = adapter.getView(i, null, listview);
            childView.measure(
                    View.MeasureSpec.makeMeasureSpec(listview.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache();
            bmps.add(childView.getDrawingCache());
            allitemsheight += childView.getMeasuredHeight();
        }

        Bitmap bigbitmap =
                Bitmap.createBitmap(listview.getMeasuredWidth(), allitemsheight, Bitmap.Config.ARGB_8888);
        Canvas bigcanvas = new Canvas(bigbitmap);

        Paint paint = new Paint();
        int iHeight = 0;

        for (int i = 0; i < bmps.size(); i++) {
            Bitmap bmp = bmps.get(i);
            bigcanvas.drawBitmap(bmp, 0, iHeight, paint);
            iHeight += bmp.getHeight();

            bmp.recycle();
            bmp = null;
        }

        return bigbitmap;
    }


    /**
     * 对RecyclerView进行截图
     * https://gist.github.com/PrashamTrivedi/809d2541776c8c141d9a
     */
    public static Bitmap shotRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(
                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
                        holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            Drawable lBackground = view.getBackground();
            if (lBackground instanceof ColorDrawable) {
                ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
                int lColor = lColorDrawable.getColor();
                bigCanvas.drawColor(lColor);
            }

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }
        }
        return bigBitmap;
    }



    public static float getTextWidth(String text, int textSize) {
        TextPaint paint = new TextPaint();
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledDensity * textSize);
        return paint.measureText(text);
    }

    public static float getTextWidth(String text, TextView tv) {
        TextPaint paint = new TextPaint();
        //float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(tv.getTextSize());
        return paint.measureText(text);
    }

    /**
     * 打开google
     */
    public static void openGooglePlay(Activity ac, String appPackageName) {

        if (TextUtils.isEmpty(appPackageName)) {
            return;
        }

        if (ac == null) {
            return;
        }
        try {
            //下载app

            ac.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            MyLogUtils.e(TAG, "启动谷歌play appPackageName=" + appPackageName);
        } catch (android.content.ActivityNotFoundException anfe) {

            try {

                ac.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}
