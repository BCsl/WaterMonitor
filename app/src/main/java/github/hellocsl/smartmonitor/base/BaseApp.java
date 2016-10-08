package github.hellocsl.smartmonitor.base;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * Created by chensuilun on 16-10-8.
 */
public class BaseApp extends Application {

    private static HandlerThread sWorkerThread = new HandlerThread("worker-main");

    static {
        sWorkerThread.start();
    }

    private static Handler sWorker = new Handler(sWorkerThread.getLooper());

    private static Handler sHandler = new Handler(Looper.getMainLooper());


    /**
     * 向线程发送数据
     * @param r runnable 对象
     */
    public static void postThread(Runnable r) {
        sWorker.post(r);
    }

    /**
     * 向线程发送数据
     * @param r runnable 对象
     */
    public static void removeThread(Runnable r) {
        sWorker.removeCallbacks(r);
    }

    public static void postThreadDelayed(Runnable r, long delayMillis) {
        sWorker.postDelayed(r, delayMillis);
    }

    /**
     * 向主线程发送任务
     * @param runnable
     */
    public static void post(Runnable runnable) {
        sHandler.post(runnable);
    }

    /**
     * 主线程
     * @param runnable
     */
    public static void remove(Runnable runnable) {
        sHandler.removeCallbacks(runnable);
    }

    /**
     * 主线程
     * @param runnable
     * @param delay
     */
    public static void postDelay(Runnable runnable, long delay) {
        sHandler.postDelayed(runnable, delay);
    }

}
