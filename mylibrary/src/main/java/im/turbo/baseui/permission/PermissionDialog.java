package im.turbo.baseui.permission;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.doctor.mylibrary.R;

import im.turbo.baseui.clicklistener.AvoidDoubleClickListener;

/**
 * created by zhaoyuntao
 * on 2019-11-08
 * description:
 */
public class PermissionDialog extends DialogFragment {
    private int drawableRes;
    private int stringRes;
    private int leftStringRes;
    private View.OnClickListener leftListener;
    private int rightStringRes;
    private View.OnClickListener rightListener;
    private OnDismissListener onDismissListener;
    private boolean clickedButton;

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.80f;
            windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(windowParams);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_permission, container, false);
        ImageView iconView = view.findViewById(R.id.permission_dialog_icon);
        TextView messageView = view.findViewById(R.id.permission_dialog_message);
        Button buttonViewLeft = view.findViewById(R.id.permission_dialog_left);
        Button buttonViewRight = view.findViewById(R.id.permission_dialog_right);

        iconView.setImageResource(drawableRes);
        if (stringRes != 0) {
            messageView.setText(stringRes);
        }

        if (leftStringRes != 0) {
            buttonViewLeft.setText(leftStringRes);
        }
        buttonViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedButton = true;
                dismiss();
                if (leftListener != null) {
                    leftListener.onClick(v);
                }
            }
        });
        buttonViewLeft.setVisibility(View.VISIBLE);

        if (rightStringRes != 0) {
            buttonViewRight.setText(rightStringRes);
        }
        buttonViewRight.setOnClickListener(new AvoidDoubleClickListener() {
            @Override
            public void onClickView(View view) {
                clickedButton = true;
                dismiss();
                if (rightListener != null) {
                    rightListener.onClick(view);
                }
            }
        });
        buttonViewRight.setVisibility(View.VISIBLE);
        return view;
    }

    public PermissionDialog setPermissionIcon(@DrawableRes int drawableRes) {
        this.drawableRes = drawableRes;
        return this;
    }

    public PermissionDialog setMessage(int stringRes) {
        this.stringRes = stringRes;
        return this;
    }

    public PermissionDialog setButtonLeft(int stringRes, View.OnClickListener listener) {
        this.leftStringRes = stringRes;
        this.leftListener = listener;
        return this;
    }

    public PermissionDialog setButtonRight(int stringRes, View.OnClickListener listener) {
        this.rightStringRes = stringRes;
        this.rightListener = listener;
        return this;
    }

    public PermissionDialog setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null && !clickedButton) {
            onDismissListener.onDismiss();
        }
    }

    public interface OnDismissListener {
        void onDismiss();
    }
}
