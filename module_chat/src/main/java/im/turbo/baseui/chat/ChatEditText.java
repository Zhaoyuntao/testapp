package im.turbo.baseui.chat;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.emoji.widget.EmojiEditTextHelper;

import im.turbo.basetools.observer.ListenerManager;
import im.turbo.basetools.observer.NotifyAction;

/**
 * created by zhaoyuntao
 * on 2020/7/17
 * description:
 */
public class ChatEditText extends AppCompatEditText {
    private OnSelectionChangeListener onSelectionChangeListener;
    private EmojiEditTextHelper emojiTextViewHelper;
    private boolean closeFocusListener;
    private OnKeyPreImeListener onKeyPreImeListener;
    private final ListenerManager<OnFocusChangeListener> focusChangeListenerListenerManager = new ListenerManager<>(false);

    public ChatEditText(Context context) {
        super(context);
        init();
    }

    public ChatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        super.setKeyListener(getEmojiEditTextHelper().getKeyListener(getKeyListener()));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFocused()) {
                    openFocusListener();
                    focusChangeListenerListenerManager.notifyListeners(new NotifyAction<OnFocusChangeListener>() {
                        @Override
                        public void notify(OnFocusChangeListener onFocusChangeListener) {
                            onFocusChangeListener.onFocusChange(v, isFocused());
                        }
                    });
                }
            }
        });
    }

    @Override
    public void setKeyListener(android.text.method.KeyListener keyListener) {
        super.setKeyListener(getEmojiEditTextHelper().getKeyListener(keyListener));
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection inputConnection = super.onCreateInputConnection(outAttrs);
        return getEmojiEditTextHelper().onCreateInputConnection(inputConnection, outAttrs);
    }

    private EmojiEditTextHelper getEmojiEditTextHelper() {
        if (emojiTextViewHelper == null) {
            emojiTextViewHelper = new EmojiEditTextHelper(this);
        }
        return emojiTextViewHelper;
    }

    public void addOnFocusChangeListener(OnFocusChangeListener l) {
        focusChangeListenerListenerManager.addListener(l);
    }

    public void removeOnFocusChangeListener(OnFocusChangeListener l) {
        focusChangeListenerListenerManager.removeListener(l);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (onSelectionChangeListener != null) {
            onSelectionChangeListener.onSelectionChanged(selStart, selEnd);
        }
    }

    public void setOnSelectionChangeListener(OnSelectionChangeListener onSelectionChangeListener) {
        this.onSelectionChangeListener = onSelectionChangeListener;
    }

    public void removeOnSelectionChangeListener() {
        this.onSelectionChangeListener = null;
    }

    public void closeFocusListener() {
        this.closeFocusListener = true;
    }

    public void openFocusListener() {
        this.closeFocusListener = false;
    }

    public boolean isFocusListenerWork() {
        return !this.closeFocusListener;
    }

    public void insert(int selectionStart, String code) {
        if (TextUtils.isEmpty(getText())) {
            closeFocusListener();
        }
        getEditableText().insert(selectionStart, code);
    }

    public void setOnKeyPreImeListener(OnKeyPreImeListener onKeyPreImeListener) {
        this.onKeyPreImeListener = onKeyPreImeListener;
    }

    public interface OnSelectionChangeListener {
        void onSelectionChanged(int start, int end);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (onKeyPreImeListener != null && onKeyPreImeListener.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public interface OnKeyPreImeListener {
        boolean onKeyDown(int keyCode, KeyEvent event);
    }
}
