package com.anysou.as_floatlib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.anysou.as_floatlib.FloatActionController;

/**
 * Author:xishuang
 * Date:2017.08.01
 * Des: 一些Home建 与 切换键 的广播监听，需要动态注册  (广播接收)
 *
 * 在每次点击Home按键时都会发出一个action为Intent.ACTION_CLOSE_SYSTEM_DIALOGS的广播，它是关闭系统Dialog的广播，我们可以通过注册它来监听Home按键消息
 */

public class HomeWatcherReceiver extends BroadcastReceiver {

    private static final String TAG = "HomeWatcherReceiver";

    private static final String SYSTEM_DIALOG_FROM_KEY = "reason";              //按下Home键
    private static final String SYSTEM_DIALOG_FROM_RECENT_APPS = "recentapps";  //系统内置字符串，停止键
    private static final String SYSTEM_DIALOG_FROM_HOME_KEY = "homekey";        //系统内置字符串，表示home键 == 长按Home键
    private static final String SYSTEM_DIALOG_FROM_LOCK = "lock";               //锁屏操作

    private int oldMediaVolume;

    public void init(Context mContext) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        mContext.registerReceiver(this, filter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Log.i(TAG, "onReceive: action: " + action);

        //根据不同的信息进行一些个性操作
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String from = intent.getStringExtra(SYSTEM_DIALOG_FROM_KEY);
            Log.i(TAG, "from: " + from);

            if (SYSTEM_DIALOG_FROM_HOME_KEY.equals(from)) { //短按Home键
                Log.i(TAG, "Home Key");
                //按home键会直接关闭悬浮窗
                FloatActionController.getInstance().stopMonkServer(context);

            } else if (SYSTEM_DIALOG_FROM_RECENT_APPS.equals(from)) { //长按Home键或是Activity切换键
                Log.i(TAG, "long press home key or activity switch");

            } else if (SYSTEM_DIALOG_FROM_LOCK.equals(from)) { //锁屏操作
                Log.i(TAG, "lock");
            }

        } else if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {

//            if (mAudioUtil != null && isMediaVolumePowerSaveSettings) {
//                int currAudioVolume = mAudioUtil.getMediaVolume();
//
//                if (oldMediaVolume == currAudioVolume) {
//                    return;
//                }
//
//                oldMediaVolume = currAudioVolume;
//                Log.d(TAG, "currMediaVolume = " + currAudioVolume);
//
//                if (currAudioVolume == mAudioUtil.getMaxMediaVolume()) {
//                    return;
//                }
//
//                if (AUDIO_ADJ - currAudioVolume > 0) {
//                    Log.d(TAG, "Down volume key resume");
//                    mAudioUtil.setMediaVolume(AUDIO_ADJ);
//                    isMediaVolumePowerSaveSettings = false;
//                }
//
//            }

        }

    }

}
