package github.hellocsl.smartmonitor.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import github.hellocsl.smartmonitor.AppApplication;
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
        if (!AppUtils.isRooted()) {
            Toast.makeText(AppApplication.getContext(), R.string.can_not_run_without_root, Toast.LENGTH_SHORT).show();
            finish();
        }
        mSettingRoot.setCheck(RootCmd.haveRoot());
    }

    private boolean mProtected;

    @Override
    protected void onResume() {
        super.onResume();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onResume: ");
        }
        mSettingRoot.setCheck(RootCmd.sHaveRoot);
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
        if (!mSettingRoot.isCheck()) {
            mSettingRoot.setCheck(RootCmd.haveRoot());
        }
    }
}
