package mobi.yiyin;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.util.Stack;

/**
 * @author wlk
 * @date 2020/9/2
 */

public class MyApplication extends Application {
    private static MyApplication mApplication;

    public static TextToSpeech textToSpeech;
    public static final boolean DEBUG = true;
    private NotificationManager mNotificationManager;
    /**
     * 判断是否是生产环境
     */
    private final boolean environmentTag = true;

    public synchronized static MyApplication getInstance() {
        return mApplication;
    }

    private static Stack<Activity> activityStack;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5efc3da4");
    }

    /**
     * add Activity 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                return;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 下线
     */
    public void finishOutActivity() {
        for (int i=0;i<activityStack.size();i++){
            try {
                if (!(activityStack.get(i).toString().contains("LoginActivity"))) {
                    finishActivity(activityStack.get(i));
                    i--;
                }
            }catch (Exception e){
                break;
            }

        }
    }



    public int getActivityNum(){
        return activityStack.size();
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            System.exit(0);
        } catch (Exception e) {
//            L.e(e.toString());
        }
    }

    public NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }


}
