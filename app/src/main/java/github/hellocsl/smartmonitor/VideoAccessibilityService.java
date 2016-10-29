package github.hellocsl.smartmonitor;


import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import github.hellocsl.smartmonitor.state.IMonitorService;
import github.hellocsl.smartmonitor.state.Impl.IdleState;
import github.hellocsl.smartmonitor.state.MonitorState;
import github.hellocsl.smartmonitor.utils.Constant;

import static android.view.accessibility.AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOWS_CHANGED;
import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;


/**
 * Created by chensuilun on 16-10-8.
 */
public class VideoAccessibilityService extends AccessibilityService implements IMonitorService {
    private static final String TAG = "VideoAccessibilityServi";
    public static boolean sIsEnable = false;

    private MonitorState mCurState;

    @Override
    public void setState(MonitorState state) {
        mCurState = state;
    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        registerScreenReceiver();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onServiceConnected: ");
        }
        sIsEnable = true;
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = TYPE_WINDOW_CONTENT_CHANGED | TYPE_WINDOWS_CHANGED | TYPE_WINDOW_STATE_CHANGED | TYPE_NOTIFICATION_STATE_CHANGED;

        // If you only want this service to work with specific applications, set their
        // package names here.  Otherwise, when the service is activated, it will listen
        // to events from all applications.
        info.packageNames = new String[]{Constant.QQ_PKG};

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
//        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.notificationTimeout = 100;

        this.setServiceInfo(info);
        setState(new IdleState(this));
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onUnbind: ");
        }
        unRegisterScreenReceiver();
        sIsEnable = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAccessibilityEvent: event:" + accessibilityEvent.getEventType());
        }
        if (mCurState != null) {
            mCurState.handle(accessibilityEvent);
        }
    }


    @Override
    public void onInterrupt() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onInterrupt: ");
        }
    }


    @Override
    public AccessibilityNodeInfo getWindowNode() {
        return getRootInActiveWindow();
    }


    private ScreenReceiver mScreenReceiver;

    private void registerScreenReceiver() {
        if (mScreenReceiver == null) {
            mScreenReceiver = new ScreenReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(mScreenReceiver, intentFilter);
        }
    }

    private void unRegisterScreenReceiver() {
        if (mScreenReceiver != null) {
            unregisterReceiver(mScreenReceiver);
        }
    }

    /**
     * @author chensuilun
     */
    public class ScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_SCREEN_OFF:
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "onReceive: Screen off");
                    }
                    setState(new IdleState(VideoAccessibilityService.this));
                    break;
                default:
                    break;
            }
        }
    }

}
