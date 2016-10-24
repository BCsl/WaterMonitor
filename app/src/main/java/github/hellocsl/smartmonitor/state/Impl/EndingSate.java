package github.hellocsl.smartmonitor.state.Impl;

import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RelativeLayout;

import java.util.List;

import github.hellocsl.smartmonitor.BuildConfig;
import github.hellocsl.smartmonitor.state.IMonitorService;
import github.hellocsl.smartmonitor.state.MonitorState;
import github.hellocsl.smartmonitor.utils.AppUtils;
import github.hellocsl.smartmonitor.utils.Constant;
import github.hellocsl.smartmonitor.utils.RootCmd;

/**
 * Created by chensuilun on 16/10/23.
 */

public class EndingSate extends MonitorState {
    private static final String TAG = "EndingSate";

    public EndingSate(IMonitorService contextService) {
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
            Log.d(TAG, "handle: ");
        }
        if (isVideoChatEnded(nodeInfo, accessibilityEvent)) {
            if (!AppUtils.isInLockScreen()) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "handle: close screen");
                }
                //熄屏,等待下次命令
                RootCmd.execRootCmd("sleep 0.1 && input keyevent " + KeyEvent.KEYCODE_POWER);
            }
            mContextService.setState(new IdleState(mContextService));
        }
    }

    /**
     * @param nodeInfo
     * @param accessibilityEvent
     * @return 消息列表的最后一个Item是否为视频通话结束或取消
     */
    private boolean isVideoChatEnded(AccessibilityNodeInfo nodeInfo, AccessibilityEvent accessibilityEvent) {
        String pkg = accessibilityEvent.getPackageName().toString();
        List<AccessibilityNodeInfo> listNode = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/listView1");
        AccessibilityNodeInfo tempNode = null;
        try {
            if (Constant.QQ_PKG.equals(pkg)) {
                if (!AppUtils.isListEmpty(listNode)) {
                    tempNode = listNode.get(0);
                    tempNode = tempNode.getChild(tempNode.getChildCount() - 1);
                    if (tempNode != null && tempNode.getClassName().equals(RelativeLayout.class.getName())) {
                        return !AppUtils.isListEmpty(tempNode.findAccessibilityNodeInfosByText("拒绝"))
                                || !AppUtils.isListEmpty(tempNode.findAccessibilityNodeInfosByText("通话时长"))
                                || !AppUtils.isListEmpty(tempNode.findAccessibilityNodeInfosByText("取消"));
                    }
                }
            }
        } finally {
            if (tempNode != null) {
                tempNode.recycle();
            }
        }
        return false;
    }
}
