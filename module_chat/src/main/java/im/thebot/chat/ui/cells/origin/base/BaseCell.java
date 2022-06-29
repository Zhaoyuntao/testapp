package im.thebot.chat.ui.cells.origin.base;

import android.content.Context;
import android.view.View;

import androidx.annotation.IdRes;
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

    private M m;
    private View rootView;
    private ChatPresenter presenter;

    public BaseCell() {
    }

    final public void setPresenter(ChatPresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    final public ChatPresenter getPresenter() {
        Preconditions.checkNotNull(presenter);
        return presenter;
    }

    final public <T extends View> T findViewById(@IdRes int id) {
        return rootView.findViewById(id);
    }

    final public Context getContext() {
        return rootView.getContext();
    }

    /**
     * Init child view here.
     *
     * @param context
     */
    public abstract void initView(@NonNull Context context);


    /**
     * Min width that cell can be displayed.
     *
     * @return
     */
    protected int getMinWidthDp() {
        return 0;
    }

    /**
     * Set a layout resource for cell.
     *
     * @return layout resource.
     */
    @LayoutRes
    public abstract int setLayout();

    /**
     * If this cell need to be displayed as max width.
     *
     * @return
     */
    public boolean isMaxWidth() {
        return false;
    }

    protected abstract void onMessageInit(@NonNull M message);

    final public void initMessage(@NonNull M message) {
        setTag(message.getUUID());
        this.m = message;
        onMessageInit(message);
    }

    final public void changeMessage(@NonNull M message) {
        setTag(message.getUUID());
        this.m = message;
        onMessageChanged(message);
    }

    public void setTag(Object o) {
        this.rootView.setTag(o);
    }

    public Object getTag() {
        return rootView.getTag();
    }

    @NonNull
    final protected M getMessage() {
        Preconditions.checkNotNull(m);
        return m;
    }

    /**
     * If this message show as a character's message(Layout on left or right).
     *
     * @return If false, the width would match parent.
     */
    public boolean showAsSender() {
        return true;
    }

    /**
     * When data changed.
     *
     * @param message
     */
    public void onMessageChanged(@NonNull M message) {
    }

    /**
     * When click bubble view.
     *
     * @param message
     */
    public void onClickMessage(@NonNull M message) {

    }

    final protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
        onAttachedToCell(m);
    }

    final protected void onDetachedFromWindow() {
        onDetachedFromCell(m);
//        super.onDetachedFromWindow();
    }

    protected void onDetachedFromCell(@Nullable M message) {

    }

    protected void onAttachedToCell(@Nullable M message) {

    }

    public boolean isAttachedToWindow() {
        return rootView.isAttachedToWindow();
    }

    public boolean isSupportClick() {
        return !presenter.isSelecting();
    }

    public boolean isSupportLongClick() {
        return true;
    }

    public void setRootView(View cell) {
        rootView = cell;
        rootView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                onAttachedToWindow();
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                onDetachedFromWindow();
            }
        });
        initView(rootView.getContext());
    }

    public void setOnClickListener(View.OnClickListener listener) {
        rootView.setOnClickListener(listener);
    }

    public void setOnLongClickListener(View.OnLongClickListener listener) {
        rootView.setOnLongClickListener(listener);
    }

    final protected void runUI(Runnable runnable) {
        ThreadPool.runUi(new SafeRunnable(rootView) {
            @Override
            protected void runSafely() {
                runnable.run();
            }
        });
    }
}
