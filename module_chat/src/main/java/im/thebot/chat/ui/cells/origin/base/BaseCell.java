package im.thebot.chat.ui.cells.origin.base;

import android.content.Context;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import im.thebot.chat.api.chat.message.MessageBeanForUI;
import im.thebot.chat.mvp.presenter.ChatPresenter;
import im.turbo.basetools.preconditions.Preconditions;
import im.turbo.thread.SafeRunnable;
import im.turbo.thread.ThreadPool;

/**
 * created by zhaoyuntao
 * on 08/11/2020
 * description:
 */
public abstract class BaseCell<M extends MessageBeanForUI> {
    protected static final int GONE = View.GONE;
    protected static final int VISIBLE = View.VISIBLE;
    protected static final int INVISIBLE = View.INVISIBLE;

    @IntDef({GONE, VISIBLE, INVISIBLE})
    public @interface VISIBILITY {
    }

    private M m;
    private View rootView;
    private ChatPresenter presenter;

    public BaseCell() {
    }

    /**
     * Init child view here.
     */
    public abstract void initView(@NonNull Context context);


    /**
     * Set a layout resource for cell.
     */
    @LayoutRes
    public abstract int setLayout();

    protected abstract void onMessageInit(@NonNull M message);

    final public void setPresenter(ChatPresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    final public ChatPresenter getPresenter() {
        Preconditions.checkNotNull(presenter);
        return presenter;
    }

    final public void setRootView(@NonNull View rootView) {
        this.rootView = rootView;
        this.rootView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                onAttachedToWindow();
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                onDetachedFromWindow();
            }
        });
        initView(this.rootView.getContext());
    }

    final public <T extends View> T findViewById(@IdRes int id) {
        return rootView.findViewById(id);
    }

    final public Context getContext() {
        return rootView.getContext();
    }

    final public void initMessage(@NonNull M message) {
        rootView.setTag(message.getUUID());
        this.m = message;
        onMessageInit(message);
    }

    final public void changeMessage(@NonNull M message) {
        rootView.setTag(message.getUUID());
        this.m = message;
        onMessageChanged(message);
    }

    final public Object getTag() {
        return rootView.getTag();
    }

    @NonNull
    final protected M getMessage() {
        Preconditions.checkNotNull(m);
        return m;
    }

    final public void setOnClickListener(View.OnClickListener listener) {
        rootView.setOnClickListener(listener);
    }

    final public void setOnLongClickListener(View.OnLongClickListener listener) {
        rootView.setOnLongClickListener(listener);
    }

    public boolean isSupportClick(@NonNull M message) {
        return true;
    }

    public void onMessageChanged(@NonNull M message) {
    }

    public void onClickMessage(@NonNull M message) {

    }

    final protected void onAttachedToWindow() {
        onAttachedToCell(m);
    }

    final protected void onDetachedFromWindow() {
        onDetachedFromCell(m);
    }

    final public boolean isAttachedToWindow() {
        return rootView.isAttachedToWindow();
    }

    protected void onDetachedFromCell(@Nullable M message) {

    }

    protected void onAttachedToCell(@Nullable M message) {

    }
}
