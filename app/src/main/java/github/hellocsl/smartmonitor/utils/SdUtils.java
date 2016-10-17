package github.hellocsl.smartmonitor.utils;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by chensuilun on 16-10-17.
 */

public class SdUtils {
    /**
     * sdcard head
     */
    public final static String SDCARD = Environment.getExternalStorageDirectory().getPath() + File.separator;


    private static final Pattern DIR_SEPORATOR = Pattern.compile("/");

    private static final String ENV_EXTERNAL_STORAGE = "EXTERNAL_STORAGE";
    private static final String ENV_EMULATED_STORAGE_TARGET = "EMULATED_STORAGE_TARGET";
    private static final String ENV_SECONDARY_STORAGE = "SECONDARY_STORAGE";


    /**
     * Return all available SD-Cards in the system (include emulated)
     * @return paths to all available SD-Cards in the system (include emulated)
     */
    public static String[] getStorageDirectories() {
        // 最终的输出路径集合，用set保证不重复
        final Set<String> dirs = new HashSet<String>();
        // 所有SD卡路径
        final String rawExternalStorage = System.getenv(ENV_EXTERNAL_STORAGE);
        // 所有二级SD卡路径
        final String rawSecondaryStoragesStr = System
                .getenv(ENV_SECONDARY_STORAGE);
        // 模拟SD卡路径
        final String rawEmulatedStorageTarget = System
                .getenv(ENV_EMULATED_STORAGE_TARGET);
        if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
            if (TextUtils.isEmpty(rawExternalStorage)) {
                // 如果都没有路径，直接写死用下面默认路径（源码）
                dirs.add("/storage/sdcard0");
            } else {
                dirs.add(rawExternalStorage);
            }
        } else {
            final String rawUserId;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                rawUserId = "";
            } else {
                final String path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath();
                final String[] folders = DIR_SEPORATOR.split(path);
                final String lastFolder = folders[folders.length - 1];
                boolean isDigit = false;
                try {
                    Integer.valueOf(lastFolder);
                    isDigit = true;
                } catch (NumberFormatException ignored) {
                }
                rawUserId = isDigit ? lastFolder : "";
            }
            // 各种模拟路径 /storage/emulated/0[1,2,...]
            if (TextUtils.isEmpty(rawUserId)) {
                dirs.add(rawEmulatedStorageTarget);
            } else {
                dirs.add(rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        }
        // 二级SD卡路径
        if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
            final String[] rawSecondaryStorages = rawSecondaryStoragesStr
                    .split(File.pathSeparator);
            Collections.addAll(dirs, rawSecondaryStorages);
        }

        // 所有在默认SD卡下的默认路径父目录下的文件夹
        File innerDir = Environment.getExternalStorageDirectory();
        File rootDir = innerDir.getParentFile();
        File[] files = rootDir.listFiles();
        if (files != null) {
            for (File file : files) {
                dirs.add(file.getPath());
            }
        }

        return dirs.toArray(new String[dirs.size()]);
    }

    /**
     * <br>功能简述: 判断该目录能否写入
     * <br>功能详细描述:
     * <br>注意:
     * @param path
     * @return
     */
    public static boolean canWrite(String path) {
        File file = new File(path);

        // 判断file能否读取写入，并且不是一个 USB drive
        // 分开写用于debug
        if (file != null) {
            if (file.isDirectory() && file.canRead() && file.canWrite()) {
                if (file.listFiles() != null) {
                    if (file.listFiles().length > 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * <br>功能简述:获取合适的存储目录
     * <br>功能详细描述:
     * <br>注意: 暂时取第一个可以写的目录
     * TODO 可以优化增加条件
     * @return
     */
    public static String getSuitableStorage() {
        String[] dirs = getStorageDirectories();
        for (String dir : dirs) {
            if (canWrite(dir)) {
                return dir;
            }
        }
        return Environment.getExternalStorageDirectory().getPath();
    }
}
