package github.hellocsl.smartmonitor.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import java.util.List;

import github.hellocsl.smartmonitor.AppApplication;

import static android.content.Context.KEYGUARD_SERVICE;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static github.hellocsl.smartmonitor.AppApplication.sAppContext;

/**
 * Created by chensuilun on 16-10-8.
 */
public class AppUtils {
    private static final String TAG = "AppUtils";

    /**
     * 打开QQ聊天界面
     *
     * @param qqNumber
     */
    public static void openQQChat(String qqNumber) {
        String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqNumber + "&version=1";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            AppApplication.getContext().startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "openQQChat: QQ not install!!!");
            return;
        }
    }

    public static void acquireTimedWakeLock(long timeout, String tag) {
        PowerManager pm = (PowerManager) sAppContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ON_AFTER_RELEASE | PowerManager.ACQUIRE_CAUSES_WAKEUP, tag);
        wakeLock.acquire(timeout);
    }


    /**
     * 跳转到系统辅助功能设置页面.<br>
     *
     * @param context
     */
    public static boolean gotoAccessibilitySettings(Context context) {
        Intent settingsIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        if (!(context instanceof Activity)) {
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        boolean isOk = true;
        try {
            context.startActivity(settingsIntent);
        } catch (ActivityNotFoundException e) {
            isOk = false;
        }
        return isOk;
    }


    public static boolean isInLockScreen() {
        KeyguardManager keyguardManager = (KeyguardManager) AppApplication.getContext().getSystemService(KEYGUARD_SERVICE);
        return keyguardManager.inKeyguardRestrictedInputMode();
    }

    /**
     * 是否支持使用辅助服务.<br>
     *
     * @return
     */
    public static boolean isSupportBoostAccessibilityService() {
        return Build.VERSION.SDK_INT >= JELLY_BEAN;
    }


    public static boolean isListEmpty(List list) {
        return list == null || list.isEmpty();
    }
}
