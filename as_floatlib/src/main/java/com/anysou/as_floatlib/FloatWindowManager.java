package com.anysou.as_floatlib;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.anysou.as_floatlib.util.Constants_LM;
import com.anysou.as_floatlib.util.SpUtil;
import com.anysou.as_floatlib.view.FloatLayout;

/**
 * Author:xishuang
 * Date:2017.08.01
 * Des:悬浮窗统一管理，与悬浮窗交互的真正实现 修改了8.0以后的显示问题
 */
public class FloatWindowManager {
    /**
     * 悬浮窗
     */
    private static FloatLayout mFloatLayout;
    private static WindowManager mWindowManager;
    private static WindowManager.LayoutParams wmParams;
    private static boolean mHasShown;

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createFloatWindow(Context context) {
        wmParams = new WindowManager.LayoutParams();
        WindowManager windowManager = getWindowManager(context);
        Toast.makeText(context,"1a", Toast.LENGTH_LONG).show();
        mFloatLayout = new FloatLayout(context);

        final int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt < Build.VERSION_CODES.KITKAT) {  //19
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        } else if (sdkInt < Build.VERSION_CODES.N_MR1) { //25
            if ("Xiaomi".equalsIgnoreCase(Build.MANUFACTURER)) {
                wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
        } else if (sdkInt < Build.VERSION_CODES.O) {  //26
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        } else {//8.0以后
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.START | Gravity.TOP;

        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        int screenWidth = dm.widthPixels;
        //窗口高度
        int screenHeight = dm.heightPixels;
        //以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = screenWidth;
        wmParams.y = screenHeight;
        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        mFloatLayout.setParams(wmParams);
        windowManager.addView(mFloatLayout, wmParams);
        mHasShown = true;

        Toast.makeText(context,"2", Toast.LENGTH_LONG).show();
        //是否展示小红点展示
        checkRedDot(context);
    }

    /**
     * 移除悬浮窗
     */
    public static void removeFloatWindowManager() {
        //移除悬浮窗口
        boolean isAttach = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isAttach = mFloatLayout.isAttachedToWindow();
        }
        if (mHasShown && isAttach && mWindowManager != null)
            mWindowManager.removeView(mFloatLayout);
    }

    /**
     * 返回当前已创建的WindowManager。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 小红点展示
     */
    public static void checkRedDot(Context context) {
        if (mFloatLayout == null) return;
        //是否展示小红点展示
        int num = getObtainNumber(context);
        if (num > 0) {
            mFloatLayout.setDragFlagViewVisibility(View.VISIBLE);
            mFloatLayout.setDragFlagViewText(getObtainNumber(context));
        } else {
            mFloatLayout.setDragFlagViewVisibility(View.GONE);
        }
    }

    /**
     * 添加小红点
     */
    public static void addObtainNumer(Context context) {
        int number = (int) SpUtil.get(context, Constants_LM.OBTAIN_NUMBER, 0);
        if (number < 0) {
            number = 0;
        }
        number = number + 1;
        SpUtil.put(context, Constants_LM.OBTAIN_NUMBER, number);
        if (mFloatLayout != null) {
            mFloatLayout.setDragFlagViewVisibility(View.VISIBLE);
            mFloatLayout.setDragFlagViewText(number);
        }
    }

    /**
     * 获取小红点展示的数量
     */
    private static int getObtainNumber(Context context) {
        return (int) SpUtil.get(context, Constants_LM.OBTAIN_NUMBER, 0);
    }

    /**
     * 设置小红点数字
     */
    public static void setObtainNumber(Context context, int number) {
        if (number < 0) {
            number = 0;
        }
        SpUtil.put(context, Constants_LM.OBTAIN_NUMBER, number);
        FloatWindowManager.checkRedDot(context);
    }

    /**
     * 隐藏对话栏，是否小红点
     */
    public static void updataRedAndDialog(Context context) {
        mFloatLayout.setDragFlagViewVisibility(View.VISIBLE);
        //是否展示小红点展示
        checkRedDot(context);
    }

    public static void hide() {
        if (mHasShown)
            mWindowManager.removeViewImmediate(mFloatLayout);
        mHasShown = false;
    }

    public static void show() {
        if (!mHasShown)
            mWindowManager.addView(mFloatLayout, wmParams);
        mHasShown = true;
    }
}
