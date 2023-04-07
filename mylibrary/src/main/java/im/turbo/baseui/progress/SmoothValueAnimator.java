package im.turbo.baseui.progress;

import android.animation.ValueAnimator;

import im.turbo.basetools.observer.ListenerManager;
import im.turbo.basetools.observer.NotifyAction;

/**
 * created by zhaoyuntao
 * on 15/02/2023
 */
public class SmoothValueAnimator extends ValueAnimator {

    private ListenerManager<SmoothAnimatorListener> listenerManager;
    private boolean percent;

    public SmoothValueAnimator() {
        listenerManager = new ListenerManager<>(false);
        addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                listenerManager.notifyListeners(new NotifyAction<SmoothAnimatorListener>() {
                    @Override
                    public void notify(SmoothAnimatorListener listener) {
                        float percentStart = listener.getPercentStart();
                        float percentEnd = listener.getPercentEnd();
                        if (percentEnd > percentStart) {
                            listenerManager.removeListener(listener);
                            return;
                        }
//                        listener.onValueUpdate(percent);
                    }
                });
            }
        });
    }
}
