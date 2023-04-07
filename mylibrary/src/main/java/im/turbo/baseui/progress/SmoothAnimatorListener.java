package im.turbo.baseui.progress;

/**
 * created by zhaoyuntao
 * on 15/02/2023
 */
abstract class SmoothAnimatorListener {
    private float percentStart, percentEnd;

    protected SmoothAnimatorListener(float percentStart, float percentEnd) {
        this.percentStart = percentStart;
        this.percentEnd = percentEnd;
    }

    public float getPercentStart() {
        return percentStart;
    }

    public float getPercentEnd() {
        return percentEnd;
    }

    abstract void onValueUpdate(float percent);
}
