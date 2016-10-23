package github.hellocsl.smartmonitor.state.Impl;

import android.app.Notification;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import github.hellocsl.smartmonitor.BuildConfig;
import github.hellocsl.smartmonitor.state.IMonitorService;
import github.hellocsl.smartmonitor.state.MonitorState;
import github.hellocsl.smartmonitor.utils.AppUtils;
import github.hellocsl.smartmonitor.utils.Constant;
import github.hellocsl.smartmonitor.utils.Privacy;
import github.hellocsl.smartmonitor.utils.RootCmd;
import github.hellocsl.smartmonitor.utils.TelephoneHelper;
import github.hellocsl.smartmonitor.utils.UnLockUtils;

import static android.view.accessibility.AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
import static github.hellocsl.smartmonitor.utils.Constant.MONITOR_TAG;

/**
 * 初始状态，等待来电处理
 * change to monitor QQ new message (LockScreen, Notification , QQ App)
 * Created by chensuilun on 16-10-9.
 */
public class IdleState extends MonitorState {
    private static final String TAG = "IdleState";

    public IdleState(IMonitorService contextService) {
        super(contextService);
    }

    @Override
    public void handle(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo nodeInfo = mContextService.getWindowNode();
        if (nodeInfo == null) {
            if (BuildConfig.DEBUG) {
                Log.v(TAG, "handle: null nodeInfo");
            }
            return;
        }
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "handle:");
        }

        if (isCallComing(nodeInfo)) {
            if (BuildConfig.DEBUG) {
                Log.v(TAG, "handle: call");
            }
            handOffCall(nodeInfo);
            if (AppUtils.isInLockScreen()) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "handle: unlock screen");
                }
                unlockScreen(nodeInfo);
            }
            AppUtils.openQQChat(Privacy.QQ_NUMBER);
            mContextService.setState(new QQChatState(mContextService));

        } else if (isLockScreenMonitorMsg(nodeInfo, accessibilityEvent) || isNotificationMonitorMsg(nodeInfo, accessibilityEvent)) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "handle: monitor msg");
            }
            if (AppUtils.isInLockScreen()) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "handle: unlock screen");
                }
                //back press
                RootCmd.execRootCmd("sleep 0.1 && input keyevent " + KeyEvent.KEYCODE_BACK);
                unlockScreen(nodeInfo);
            }
            AppUtils.openQQChat(retrieveQQNumber(nodeInfo, accessibilityEvent));
            mContextService.setState(new QQChatState(mContextService));
        }
    }

    /**
     * retract monitor cmd from notification
     *
     * @param nodeInfo
     * @param accessibilityEvent
     * @return
     */
    private boolean isNotificationMonitorMsg(AccessibilityNodeInfo nodeInfo, AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == TYPE_NOTIFICATION_STATE_CHANGED) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "isNotificationMonitorMsg: ");
            }
            Parcelable data = accessibilityEvent.getParcelableData();
            if (data instanceof Notification) {
                if (((Notification) data).tickerText != null) {
                    return (((Notification) data).tickerText.toString().startsWith(MONITOR_TAG)
                            && ((Notification) data).tickerText.toString().endsWith(Constant.MONITOR_CMD));
                }
            }
        }
        return false;
    }

    /**
     * @param nodeInfo
     * @param accessibilityEvent
     * @return If from notification ,msg format :{@link Constant#MONITOR_TAG} + ":real QQ No: "+{@link Constant#MONITOR_CMD}
     */
    private String retrieveQQNumber(AccessibilityNodeInfo nodeInfo, AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == TYPE_NOTIFICATION_STATE_CHANGED) {
            Parcelable data = accessibilityEvent.getParcelableData();
            if (data instanceof Notification) {
                if (((Notification) data).tickerText != null) {
                    return ((Notification) data).tickerText.toString().split(":")[1];
                }
            }
        } else {
            List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(MONITOR_TAG);
            if (!AppUtils.isListEmpty(nodeInfos)) {
                String tag;
                for (AccessibilityNodeInfo info : nodeInfos) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "retrieveQQNumber: " + info.getText());
                    }
                    tag = (String) info.getText();
                    if (!TextUtils.isEmpty(tag) && tag.contains(MONITOR_TAG)) {
                        return tag.substring(Constant.MONITOR_TAG.length());
                    }
                }
            }
        }
        return Privacy.QQ_NUMBER;
    }

    /**
     * receive monitor cmd in LockScreen
     *
     * @param nodeInfo
     * @param accessibilityEvent
     * @return
     */
    private boolean isLockScreenMonitorMsg(AccessibilityNodeInfo nodeInfo, AccessibilityEvent accessibilityEvent) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "isMonitorMsg: pkg:" + nodeInfo.getPackageName());
        }
        if (AppUtils.isInLockScreen() && Constant.QQ_PKG.equals(nodeInfo.getPackageName()) && TYPE_WINDOW_CONTENT_CHANGED == accessibilityEvent.getEventType()) {
            if (!AppUtils.isListEmpty(nodeInfo.findAccessibilityNodeInfosByText(MONITOR_TAG))
                    && !AppUtils.isListEmpty(nodeInfo.findAccessibilityNodeInfosByText(Constant.MONITOR_CMD))) {
                return true;
            }
        }
        return false;
    }


    /**
     * 挂断电话
     *
     * @param nodeInfo
     */

    private void handOffCall(AccessibilityNodeInfo nodeInfo) {
        TelephoneHelper.killCall();
    }

    /**
     * 解锁魅族
     *
     * @param nodeInfo
     */
    private void unlockScreen(AccessibilityNodeInfo nodeInfo) {
        UnLockUtils.unlock();
    }


    /**
     * @param nodeInfo
     * @return 是否在来电界面 (仅适配魅族)
     */
    private boolean isCallComing(AccessibilityNodeInfo nodeInfo) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "isCallComing pkg: " + nodeInfo.getPackageName());
        }
        if (Build.MODEL.equals(Constant.N5_MODEL)) {
            if (!AppUtils.isListEmpty(nodeInfo.findAccessibilityNodeInfosByText(MONITOR_TAG))
                    && !AppUtils.isListEmpty(nodeInfo.findAccessibilityNodeInfosByText("来电"))) {
                return true;
            }
        } else if (Build.DEVICE.toLowerCase().contains(Constant.MX_MODEL)) {
            if (Constant.MEIZU_IN_CALL_PKG.equals(nodeInfo.getPackageName())
                    && !AppUtils.isListEmpty(nodeInfo.findAccessibilityNodeInfosByText("右滑接听，左滑挂断"))
                    && !AppUtils.isListEmpty(nodeInfo.findAccessibilityNodeInfosByText(MONITOR_TAG))) {
                return true;
            }
        }
        return false;
    }
}
