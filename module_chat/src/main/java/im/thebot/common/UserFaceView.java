package im.thebot.common;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * created by zhaoyuntao
 * on 09/06/2022
 * description:
 */
public class UserFaceView extends AppCompatImageView {
    public UserFaceView(Context context) {
        super(context);
    }

    public UserFaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UserFaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bindUid(String contactCardUid) {


    }
}
