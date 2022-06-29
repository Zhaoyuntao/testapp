package im.turbo.thread;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import im.turbo.baseui.lifecircle.BaseLifeCircle;


/**
 * created by zhaoyuntao
 * on 2019-10-27
 * description:
 */
public abstract class SafeRunnable extends BaseLifeCircle implements Runnable {

    public SafeRunnable(Activity lifeCircle) {
        super(lifeCircle);
    }

    public SafeRunnable(Fragment lifeCircle) {
        super(lifeCircle);
    }

    public SafeRunnable(android.app.Fragment lifeCircle) {
        super(lifeCircle);
    }

    public SafeRunnable(Context lifeCircle) {
        super(lifeCircle);
    }

    public SafeRunnable(View lifeCircle) {
        super(lifeCircle);
    }

    protected abstract void runSafely();

    protected void beforeRun(){}
    protected void afterRun(){}

    @Override
    public void run() {
        if (checkLifeCircle()) {
            beforeRun();
            runSafely();
            afterRun();
        }
    }

}

