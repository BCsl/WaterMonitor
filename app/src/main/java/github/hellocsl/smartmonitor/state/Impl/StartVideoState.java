package github.hellocsl.smartmonitor.state.Impl;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RelativeLayout;

import java.util.List;

import github.hellocsl.smartmonitor.BuildConfig;
import github.hellocsl.smartmonitor.state.IMonitorService;
import github.hellocsl.smartmonitor.state.MonitorState;

import static github.hellocsl.smartmonitor.utils.AppUtils.isListEmpty;

/**
 * 视频聊天状态，找到视频电话按钮并点击
 * Created by chensuilun on 16-10-9.
 */
public class StartVideoState extends MonitorState {
    private static final String TAG = "StartVideoState";

    public StartVideoState(IMonitorService contextService) {
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
        if (startVideoChat(nodeInfo)) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "handle: start suc");
            }
            mContextService.setState(new EndingSate(mContextService));
        } else {
            mContextService.setState(new QQChatState(mContextService));
        }
    }

    /**
     * 点击视频聊天按钮
     * @param nodeInfo
     * @return 是否成功
     */
    private boolean startVideoChat(AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> videoTextNodes = nodeInfo.findAccessibilityNodeInfosByText("视频电话");
        if (!isListEmpty(videoTextNodes)) {
            for (AccessibilityNodeInfo textNode : videoTextNodes) {
                if (textNode.getClassName().toString().contains(RelativeLayout.class.getName())) { //contextDesc
                    textNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                }
            }
        }
        return false;
    }

}
