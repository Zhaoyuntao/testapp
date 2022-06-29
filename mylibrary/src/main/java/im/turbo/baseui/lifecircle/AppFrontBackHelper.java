package im.turbo.baseui.lifecircle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;

import im.turbo.basetools.observer.ListenerManager;
import im.turbo.utils.ResourceUtils;


public class AppFrontBackHelper extends ListenerManager<AppFrontBackHelper.OnAppStatusListener> {
    private int activityStartCount = 0;
    private static final AppFrontBackHelper instance = new AppFrontBackHelper();

    private AppFrontBackHelper() {
        super(true);
    }

    public static AppFrontBackHelper getInstance() {
        return instance;
    }

    public void register() {
        ResourceUtils.getApplication().registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    public void unregister(Application application) {
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    public void addActivityListener(OnAppStatusListener listener) {
        if (listener == null) {
            return;
        }
        addListener(listener);
    }

    public void removeActivityListener(OnAppStatusListener listener) {
        if (listener == null) {
            return;
        }
        removeListener(listener);
    }

    private final Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {


        @Override
        public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            activityStartCount++;
            //数值从0变到1说明是从后台切到前台
            if (activityStartCount == 1) {
                //从后台切到前台
                notifyListeners(OnAppStatusListener::onFront);
            }
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
            activityStartCount--;
            //数值从1到0说明是从前台切到后台
            if (activityStartCount == 0) {
                //从前台切到后台
                notifyListeners(OnAppStatusListener::onBack);
            }
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }
    };

    public boolean isAppForeground() {
        return activityStartCount > 0;
    }

    public interface OnAppStatusListener {
        void onFront();

        void onBack();
    }

}
