package github.hellocsl.smartmonitor.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import github.hellocsl.smartmonitor.BuildConfig;
import github.hellocsl.smartmonitor.R;
import github.hellocsl.smartmonitor.ui.widget.SettingItem;
import github.hellocsl.smartmonitor.utils.AppUtils;
import github.hellocsl.smartmonitor.utils.Constant;
import github.hellocsl.smartmonitor.utils.RootCmd;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.setting_start_service)
    SettingItem mSettingService;
    @BindView(R.id.setting_root)
    SettingItem mSettingRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        AppUtils.openQQChat(Privacy.QQ_NUMBER);
        if (!TextUtils.isEmpty(RootCmd.execRootCmd("echo hello"))) {
            mSettingRoot.setCheck(true);
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onResume: ");
        }
        mSettingService.setCheck(AppUtils.checkAccessibility(Constant.ACCESSIBILITY_SERVICE));

    }

    @OnClick(R.id.setting_start_service)
    public void onClickService() {
        boolean pending = !mSettingService.isCheck();
        AppUtils.gotoAccessibilitySettings(this);
        mSettingService.setCheck(pending);
    }

    @OnClick(R.id.setting_root)
    public void onClickRoot() {

    }
}
