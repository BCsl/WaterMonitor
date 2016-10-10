package github.hellocsl.smartmonitor.state.Impl;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import github.hellocsl.smartmonitor.BuildConfig;
import github.hellocsl.smartmonitor.state.IMonitorService;
import github.hellocsl.smartmonitor.state.MonitorState;
import github.hellocsl.smartmonitor.utils.AppUtils;
import github.hellocsl.smartmonitor.utils.Constant;
import github.hellocsl.smartmonitor.utils.TelephoneHelper;

/**
 * 初始状态，等待来电处理
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
//            AppUtils.acquireTimedWakeLock(5000, "SmartMonitor");
//            AppUtils.openQQChat(Privacy.QQ_NUMBER);
//            mContextService.setState(new QQChatState(mContextService));
        }
    }

    /**
     * 挂断电话
     * @param nodeInfo
     */
    private void handOffCall(AccessibilityNodeInfo nodeInfo) {
        TelephoneHelper.killCall();
    }


    /**
     * @param nodeInfo
     * @return 是否在来电界面 (仅适配魅族)
     */
    private boolean isCallComing(AccessibilityNodeInfo nodeInfo) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "isCallComing: " + nodeInfo.getPackageName());
        }
//        if (Constant.MEIZU_IN_CALL_PKG.equals(nodeInfo.getPackageName())
//                && !AppUtils.isListEmpty(nodeInfo.findAccessibilityNodeInfosByText("右滑接听，左滑挂断"))
//                && !AppUtils.isListEmpty(nodeInfo.findAccessibilityNodeInfosByText(Constant.MONITOR_TAG))) {
        if (!AppUtils.isListEmpty(nodeInfo.findAccessibilityNodeInfosByText(Constant.MONITOR_TAG))) {
            return true;
        }
        return false;
    }
}
