package github.hellocsl.smartmonitor.state;

import android.view.accessibility.AccessibilityEvent;

/**
 * Created by chensuilun on 16-10-9.
 */
public abstract class MonitorState {
    protected IMonitorService mContextService;

    public MonitorState(IMonitorService contextService) {
        mContextService = contextService;
    }

    public abstract void handle(AccessibilityEvent accessibilityEvent);
}
