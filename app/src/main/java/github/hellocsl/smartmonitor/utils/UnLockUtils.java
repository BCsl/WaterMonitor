package github.hellocsl.smartmonitor.utils;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by chensuilun on 16/10/14.
 * 解锁工具类,通过辅助服务来解锁屏幕还是有点困难(滑动操作的模拟和一些自定义View的支持)
 * 所以解决方案只能用命令行实现,但需要root权限
 */
public class UnLockUtils {
    private static final String TAG = "UnLockUtils";
    private final static String UNLOCK_FILE = SdUtils.SDCARD + "MonitorUnlock.txt";


    private UnLockUtils() {
    }


    public static void unlock() {
        File file = new File(UNLOCK_FILE);
        if (!file.exists()) {
            Log.e(TAG, "unlock: unlock file not exist!!!");
            return;
        }
        BufferedReader bufferedReader = null;
        try {
            InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file));
            bufferedReader = new BufferedReader(streamReader);
            String input = null;
            while (!(input = bufferedReader.readLine()).equals("quit")) {
                if (TextUtils.isEmpty(input)) {
                    continue;
                }
                RootCmd.execRootCmd(input.trim());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解锁我的Nexus5
     */
    public static void unlockMyNexus5() {
        RootCmd.execRootCmd("input tap 450 450 ");
        //上滑动进入解锁
        RootCmd.execRootCmd("input swipe 655 1774 655 874");
        RootCmd.execRootCmd("sleep 1 && input text 1");
        RootCmd.execRootCmd("sleep 0.1 && input text 2");
        RootCmd.execRootCmd("sleep 0.1 && input text 3");
        RootCmd.execRootCmd("sleep 0.1 && input text 4");
        RootCmd.execRootCmd("sleep 0.1 && input keyevent " + KeyEvent.KEYCODE_ENTER);
    }

    public static void unlockMyMx() {
        RootCmd.execRootCmd("sleep 0.1 && input keyevent" + KeyEvent.KEYCODE_HOME);
        //上滑进入解锁
        RootCmd.execRootCmd("input swipe 655 1774 655 874");
        RootCmd.execRootCmd("sleep 1 && input tap 612 726");
        RootCmd.execRootCmd("sleep 0.1 && input tap 813 1000");
        RootCmd.execRootCmd("sleep 0.1 && input tap 813 1000");
        RootCmd.execRootCmd("sleep 0.1 && input tap 255 1000");
    }
}
