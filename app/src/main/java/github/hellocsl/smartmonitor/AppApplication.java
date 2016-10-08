package github.hellocsl.smartmonitor;

import android.content.Context;

import github.hellocsl.smartmonitor.base.BaseApp;

/**
 * Created by chensuilun on 16-10-8.
 */
public class AppApplication extends BaseApp {

    public static Context sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;
    }

    public static Context getContext() {
        return sAppContext;
    }
}
