package im.turbo.baseui.permission;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.example.module_chat.R;

import im.turbo.baseui.clicklistener.AvoidDoubleClickListener;

/**
 * created by zhaoyuntao
 * on 2019-11-08
 * description:
 */
class PermissionDialog extends Dialog {
    private ImageView iconView;
    private TextView messageView;
    private TextView buttonViewLeft;
    private TextView buttonViewRight;

    public PermissionDialog(@NonNull Context context) {
        super(context, R.style.TPermissionDialogTheme);
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setWindowAnimations(R.style.TPermissionDialogAnimate);
        setContentView(R.layout.layout_dialog_permission);
        iconView = findViewById(R.id.permission_dialog_icon);
        messageView = findViewById(R.id.permission_dialog_message);
        buttonViewLeft = findViewById(R.id.permission_dialog_left);
        buttonViewRight = findViewById(R.id.permission_dialog_right);
    }

    public PermissionDialog setPermissionIcon(@DrawableRes int drawableRes) {
        iconView.setImageResource(drawableRes);
        return this;
    }

    public PermissionDialog setMessage(int stringRes) {
        messageView.setText(stringRes);
        return this;
    }

    public PermissionDialog setButtonLeft(int stringRes, View.OnClickListener listener) {
        buttonViewLeft.setText(stringRes);
        buttonViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.onClick(v);
            }
        });
        buttonViewLeft.setVisibility(View.VISIBLE);
        return this;
    }

    public PermissionDialog setButtonRight(int stringRes, View.OnClickListener listener) {
        buttonViewRight.setText(stringRes);
        buttonViewRight.setOnClickListener(new AvoidDoubleClickListener() {
            @Override
            public void onClickView(View view) {
                dismiss();
                listener.onClick(view);
            }
        });
        buttonViewRight.setVisibility(View.VISIBLE);
        return this;
    }

    public PermissionDialog setTouchOutsideCancel(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }
}
