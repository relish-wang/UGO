package wang.relish.ugo;

import android.app.Activity;
import android.app.Application;

import java.util.WeakHashMap;

/**
 * <pre>
 *     author : 王鑫
 *     e-mail : wangxin@souche.com
 *     time   : 2017/05/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class UGOApp extends Application {

    public static UGOApp CONTEXT;
    private static WeakHashMap<String, Activity> mActivities = new WeakHashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
    }


    /**
     * Activity入栈
     *
     * @param activity activity
     */
    public static synchronized void addActivity(Activity activity) {
        mActivities.put(activity.getClass().getName(), activity);
    }

    /**
     * Activity出栈
     *
     * @param activityNames activity名
     */
    public static synchronized void removeActivities(String... activityNames) {
        for (String activityClassName : activityNames) {
            Activity activity = mActivities.get(activityClassName);
            if (activity != null) {
                activity.finish();
            }
            mActivities.remove(activityClassName);
        }
    }

    /**
     * 退出Activity
     */
    public static void exitApp() {
        Object[] allActivityNames = mActivities.keySet().toArray();
        String[] names = new String[allActivityNames.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = allActivityNames[i].toString();
        }
        removeActivities(names);
    }

}
