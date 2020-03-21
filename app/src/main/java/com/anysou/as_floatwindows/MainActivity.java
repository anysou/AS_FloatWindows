package com.anysou.as_floatwindows;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.anysou.as_floatlib.FloatActionController;
import com.anysou.as_floatlib.permission.FloatPermissionManager;

/**
 * 浮动 窗口 + 小红点
* 小米 安卓9.0 闪退，不知道什么问题。之前另编译的不会闪退
* */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openw(View view) {
        //判断，并去开启权限。      浮动窗口权限类名        接口            确定是否有权限
        boolean isPermission = FloatPermissionManager.getInstance().applyFloatWindow(this);

        //有对应权限 或者 系统版本小于7.0
        if (isPermission || Build.VERSION.SDK_INT < 24) {
            //开启悬浮窗
            FloatActionController.getInstance().startMonkServer(this);
        }
    }

    public void openhd(View view) {
        //开启小红点
        FloatActionController.getInstance().setObtainNumber(100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭悬浮窗
        //FloatActionController.getInstance().stopMonkServer(this);
    }

}
