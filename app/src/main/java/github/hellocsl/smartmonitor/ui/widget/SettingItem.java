package github.hellocsl.smartmonitor.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import github.hellocsl.smartmonitor.R;

/**
 * Created by chensuilun on 16-10-20.
 */

public class SettingItem extends RelativeLayout {
    public static final int STYLE_CHECK = 0;
    @BindView(R.id.setting_iv)
    ImageView mSettingIv;
    @BindView(R.id.setting_checkbox)
    AppCompatCheckBox mSettingCheckbox;
    @BindView(R.id.setting_title)
    TextView mSettingTitle;
    @BindView(R.id.setting_desc)
    TextView mSettingDesc;

    public SettingItem(Context context) {
        super(context);
        init(context, null);
    }

    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SettingItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(getContext(), R.layout.widget_setting_item, this);
        ButterKnife.bind(this);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SettingItem);
            boolean isCheck = ta.getBoolean(R.styleable.SettingItem_check, false);
            int titleId = ta.getResourceId(R.styleable.SettingItem_settingTitle, -1);
            int descId = ta.getResourceId(R.styleable.SettingItem_settingDesc, -1);
            Drawable icon = ta.getDrawable(R.styleable.SettingItem_settingIcon);
            int style = ta.getInt(R.styleable.SettingItem_style, 0);
            if (icon == null) {
                mSettingIv.setVisibility(View.GONE);
            } else {
                mSettingIv.setVisibility(View.VISIBLE);
                mSettingIv.setImageDrawable(icon);
            }
            if (titleId == -1) {
                mSettingTitle.setVisibility(GONE);
            } else {
                mSettingTitle.setVisibility(VISIBLE);
                mSettingTitle.setText(titleId);
            }
            if (descId == -1) {
                mSettingDesc.setVisibility(GONE);
            } else {
                mSettingDesc.setVisibility(VISIBLE);
                mSettingDesc.setText(descId);
            }
            if (style == STYLE_CHECK) {
                mSettingCheckbox.setVisibility(View.VISIBLE);
                mSettingCheckbox.setChecked(isCheck);
            } else {
                mSettingCheckbox.setVisibility(GONE);
            }

        }
    }

    public void setCheck(boolean check) {
        if (mSettingCheckbox != null) {
            mSettingCheckbox.setChecked(check);
        }
    }

    public boolean isCheck() {
        if (mSettingCheckbox != null) {
            return mSettingCheckbox.isChecked();
        }
        return false;
    }


}
