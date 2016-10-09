package github.hellocsl.smartmonitor;


import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import github.hellocsl.smartmonitor.utils.AppUtils;
import github.hellocsl.smartmonitor.utils.Constant;
import github.hellocsl.smartmonitor.utils.Privacy;

import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOWS_CHANGED;
import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;


/**
 * Created by chensuilun on 16-10-8.
 */
public class VideoAccessibilityService extends AccessibilityService {
    private static final String TAG = "VideoAccessibilityServi";
    public static boolean sIsEnable = false;
    public static final int STATE_IDLE = 0;
    /**
     * 来电
     */
    public static final int STATE_PHONE_COMING = 1;
    /**
     * 启动QQ
     */
    public static final int STATE_STARTING_QQ = 2;
    /**
     * 找到并点击工具栏的+号
     */
    public static final int STATE_TRACING_VIDEO_PANEL = 3;
    /**
     * 找到并点击视频聊天按钮
     */
    public static final int STATE_TRACING_VIDEO_BTN = 4;
    /**
     * 正在视频通话
     */
    public static final int STATE_VIDEO_PLAYING = 5;


    private static int mState = STATE_IDLE;


    private void setState(int state) {
        mState = state;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onServiceConnected: ");
        }
        sIsEnable = true;
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = TYPE_WINDOW_CONTENT_CHANGED | TYPE_WINDOWS_CHANGED;

        // If you only want this service to work with specific applications, set their
        // package names here.  Otherwise, when the service is activated, it will listen
        // to events from all applications.
        info.packageNames = new String[]{Constant.QQ_PKG, Constant.DIALER};

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
//        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.notificationTimeout = 100;

        this.setServiceInfo(info);
        setState(STATE_IDLE);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onUnbind: ");
        }
        sIsEnable = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAccessibilityEvent: event:" + accessibilityEvent.getEventType());
        }
        handleWindowChange(getRootInActiveWindow());
    }

    private void handleWindowChange(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            if (BuildConfig.DEBUG) {
                Log.v(TAG, "handleWindowChange: null nodeInfo");
            }
            return;
        }
        if (isCallComing(nodeInfo)) {
            if (mState < STATE_PHONE_COMING || mState == STATE_VIDEO_PLAYING) {
                setState(STATE_PHONE_COMING);
                handOffCall(nodeInfo);
                AppUtils.acquireTimedWakeLock(7000, "SmartMonitor");
                AppUtils.openQQChat(Privacy.QQ_NUMBER);
                setState(STATE_STARTING_QQ);
            }
        } else if (isQQChat(nodeInfo) && isNotVideoChat(nodeInfo) && mState < STATE_TRACING_VIDEO_PANEL) {
            setState(STATE_TRACING_VIDEO_PANEL);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "handleWindowChange: STATE_TRACING_VIDEO_PANEL");
            }
            if (!preStartVideoChat(nodeInfo)) {
                setState(STATE_STARTING_QQ);   //回滚到上个状态
            }
        } else if (isQQChat(nodeInfo) && mState < STATE_TRACING_VIDEO_BTN) {
            setState(STATE_TRACING_VIDEO_BTN);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "handleWindowChange: STATE_VIDEO_PLAYING");
            }
            if (startVideoChat(nodeInfo)) {
                setState(STATE_VIDEO_PLAYING);
            } else {
                setState(STATE_STARTING_QQ);
            }
        }
    }

    /**
     * @param nodeInfo
     * @return
     */
    private boolean isNotVideoChat(AccessibilityNodeInfo nodeInfo) {
        // TODO: 16-10-8
        return true;
    }

    /**
     * 点击,打开视频聊天面板
     * @param nodeInfo
     * @return 是否找到
     */
    private boolean preStartVideoChat(AccessibilityNodeInfo nodeInfo) {
        //并不能直接通过ID或者Text找到+号按钮，应该是动态添加的，先找到输入库，通过输入框找到相同的parent，+号的Index在倒数第二的位置上
        List<AccessibilityNodeInfo> inputNodes = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/inputBar");
        if (!isListEmpty(inputNodes)) {
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


    private boolean isListEmpty(List list) {
        return list == null || list.isEmpty();
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
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onInterrupt: ");
        }
    }

}
