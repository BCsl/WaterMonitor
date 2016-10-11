package github.hellocsl.smartmonitor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import github.hellocsl.smartmonitor.utils.AppUtils;
import github.hellocsl.smartmonitor.utils.RootCmd;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppApplication.postThreadDelayed(new Runnable() {
            @Override
            public void run() {
                if (!VideoAccessibilityService.sIsEnable && AppUtils.isSupportBoostAccessibilityService()) {
                    AppUtils.gotoAccessibilitySettings(AppApplication.getContext());
                }
            }
        }, 2000);

        if (TextUtils.isEmpty(RootCmd.execRootCmd("echo hello"))) {
            finish();
        }
//        AppUtils.openQQChat(Privacy.QQ_NUMBER);
    }


}
