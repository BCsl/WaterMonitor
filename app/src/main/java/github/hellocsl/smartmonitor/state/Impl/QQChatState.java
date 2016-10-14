package github.hellocsl.smartmonitor.state.Impl;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;

import java.util.List;

import github.hellocsl.smartmonitor.BuildConfig;
import github.hellocsl.smartmonitor.state.IMonitorService;
import github.hellocsl.smartmonitor.state.MonitorState;
import github.hellocsl.smartmonitor.utils.AppUtils;
import github.hellocsl.smartmonitor.utils.Constant;


/**
 * QQ聊天面板状态，监测到+号并点击
 * Created by chensuilun on 16-10-9.
 */
public class QQChatState extends MonitorState {
    private static final String TAG = "QQChatState";

    public QQChatState(IMonitorService contextService) {
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
        if (isQQChat(nodeInfo) && isNotVideoChat(nodeInfo)) {
            if (!preStartVideoChat(nodeInfo)) {
                mContextService.setState(new IdleState(mContextService));
            } else {
                mContextService.setState(new StartVideoState(mContextService));
            }
        } else {
            // TODO: 16-10-9  自动登录等其他处理
        }
    }

    /**
     * 点击,打开视频聊天面板
     *
     * @param nodeInfo
     * @return 是否找到
     */
    private boolean preStartVideoChat(AccessibilityNodeInfo nodeInfo) {
        //并不能直接通过ID或者Text找到+号按钮，应该是动态添加的，先找到输入库，通过输入框找到相同的parent，+号的Index在倒数第二的位置上
        List<AccessibilityNodeInfo> inputNodes = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/inputBar");
        if (!AppUtils.isListEmpty(inputNodes)) {
            AccessibilityNodeInfo node = inputNodes.get(0).getParent();
            node = node.getChild(Math.max(0, node.getChildCount() - 2));
            if (node.getClassName().toString().contains(ImageView.class.getName())) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return true;
            }
        }
        return false;
    }

    /**
     * @param nodeInfo
     * @return
     */
    private boolean isQQChat(AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> sendNodes = nodeInfo.findAccessibilityNodeInfosByText("发送");
        if (!sendNodes.isEmpty() && Constant.QQ_PKG.equals(nodeInfo.getPackageName().toString())) {
            return true;
        }
        return false;
    }

    /**
     * @param nodeInfo
     * @return
     */
    private boolean isNotVideoChat(AccessibilityNodeInfo nodeInfo) {
        // TODO: 16-10-8
        return true;
    }
}
