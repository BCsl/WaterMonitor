package github.hellocsl.smartmonitor.state;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by chensuilun on 16-10-9.
 */
public interface IMonitorService {

    void setState(MonitorState state);

    /**
     * @see AccessibilityService#getRootInActiveWindow()
     * @return The root node if this service can retrieve window content.
     */
    AccessibilityNodeInfo getWindowNode();
}
