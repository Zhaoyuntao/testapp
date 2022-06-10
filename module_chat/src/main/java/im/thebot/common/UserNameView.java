package im.thebot.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * created by zhaoyuntao
 * on 09/06/2022
 * description:
 */
public class UserNameView extends AppCompatTextView {
    public UserNameView(Context context) {
        super(context);
    }

    public UserNameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UserNameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setFollowView(View viewById) {

    }

    public void bindUid(String senderUid) {
        setText(senderUid);
    }

    public void clear() {
        setText("");
    }
}
