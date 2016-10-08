package github.hellocsl.smartmonitor;


import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import github.hellocsl.smartmonitor.utils.AppUtils;
import github.hellocsl.smartmonitor.utils.Privacy;

/**
 * Created by chensuilun on 16-10-8.
 */
public class VideoAccessibilityService extends AccessibilityService {

    public static boolean sIsEnable = false;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        sIsEnable = true;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sIsEnable = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        handleWindowChange(accessibilityEvent.getSource());
    }

    private void handleWindowChange(AccessibilityNodeInfo nodeInfo) {
        if (isCallComing(nodeInfo)) {
            handOffCall(nodeInfo);
            AppUtils.acquireTimedWakeLock(7000, "SmartMonitor");
            AppUtils.openQQChat(Privacy.QQ_NUMBER);
        } else if (isQQChat(nodeInfo) && isNotVideoChat(nodeInfo)) {
            startVideoChat(nodeInfo);
        }
    }

    /**
     * @param nodeInfo
     * @return
     */
    private boolean isNotVideoChat(AccessibilityNodeInfo nodeInfo) {
        // TODO: 16-10-8
        return false;
    }

    /**
     * 打开视频聊天
     * @param nodeInfo
     */
    private void startVideoChat(AccessibilityNodeInfo nodeInfo) {
        // TODO: 16-10-8
    }

    /**
     * @param nodeInfo
     * @return
     */
    private boolean isQQChat(AccessibilityNodeInfo nodeInfo) {
        // TODO: 16-10-8
        return false;
    }


    /**
     * 挂断电话
     * @param nodeInfo
     */
    private void handOffCall(AccessibilityNodeInfo nodeInfo) {
        // TODO: 16-10-8  关闭电话电话
    }

    /**
     * @param nodeInfo
     * @return
     */
    private boolean isCallComing(AccessibilityNodeInfo nodeInfo) {
        // TODO: 16-10-8
        return false;
    }


    @Override
    public void onInterrupt() {

    }
}
