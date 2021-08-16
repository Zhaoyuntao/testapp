package com.test.test3app.faceview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.test.test3app.R;


/**
 * created by zhaoyuntao
 * on 2020/7/13
 * description:
 */
public class BlueFaceView extends AppCompatImageView {

    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    private Path path;
    private Path pathStatus;
    private Drawable defaultFace;

    private float radiusBackgroundRoundConner;
    private boolean isGroup;
    private float percent;
    private float percentText;
    private int colorBack;
    private String firstName, lastName, uid;
    private int statusDrawableId;

    //Status icon.
    private Drawable statusDrawable;
    private int statusColor;

    private boolean supportStatus;
    private boolean showStatus;
    private int widthOfStatus;
    private int heightOfStatus;
    private float radiusRectStatusRoundConner;
    private int xOffset;
    private int yOffset;

    private boolean useRoundBreach;
    private boolean useCircleBackground;
    private Paint paint;
    private boolean backgroundRadiusSet;

    public BlueFaceView(Context context) {
        super(context);
        init(null);
    }

    public BlueFaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BlueFaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        setScaleType(ScaleType.CENTER_CROP);
        String firstName = null;
        String lastName = null;
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BlueFaceView);
            radiusBackgroundRoundConner = typedArray.getDimensionPixelSize(R.styleable.BlueFaceView_BlueFaceView_radius, 0);
            widthOfStatus = typedArray.getDimensionPixelSize(R.styleable.BlueFaceView_BlueFaceView_widthOfStatus, 0);
            heightOfStatus = typedArray.getDimensionPixelSize(R.styleable.BlueFaceView_BlueFaceView_heightOfStatus, 0);
            xOffset = typedArray.getDimensionPixelSize(R.styleable.BlueFaceView_BlueFaceView_offsetX, 0);
            yOffset = typedArray.getDimensionPixelSize(R.styleable.BlueFaceView_BlueFaceView_offsetY, 0);
            radiusRectStatusRoundConner = typedArray.getDimensionPixelSize(R.styleable.BlueFaceView_BlueFaceView_radiusStatus, 0);
            percentText = typedArray.getFloat(R.styleable.BlueFaceView_BlueFaceView_percentText, 1f);
            statusDrawable = typedArray.getDrawable(R.styleable.BlueFaceView_BlueFaceView_statusDrawable);
            statusColor = typedArray.getColor(R.styleable.BlueFaceView_BlueFaceView_statusColor, -1);
            if (statusColor == -1) {
                statusColor = ContextCompat.getColor(getContext(), R.color.color_main_blue_275edb);
            }
            supportStatus = typedArray.getBoolean(R.styleable.BlueFaceView_BlueFaceView_supportStatus, false);
            firstName = typedArray.getString(R.styleable.BlueFaceView_BlueFaceView_firstName);
            lastName = typedArray.getString(R.styleable.BlueFaceView_BlueFaceView_lastName);
            colorBack = typedArray.getColor(R.styleable.BlueFaceView_BlueFaceView_backgroundColor, -1);
            useRoundBreach = typedArray.getBoolean(R.styleable.BlueFaceView_BlueFaceView_useRoundBreach, false);
            useCircleBackground = typedArray.getBoolean(R.styleable.BlueFaceView_BlueFaceView_useCircleBackground, false);
            typedArray.recycle();
        }
        if (supportStatus) {
            percent = 0.86f;
            radiusBackgroundRoundConner *= percent;
        } else {
            percent = 1f;
        }
        if (firstName == null) {
            firstName = "";
        }
        if (lastName == null) {
            lastName = "";
        }
        setName(firstName, lastName);
        paint = new Paint();
        path = new Path();
        pathStatus = new Path();
    }

    public void setCornerRadius(float cornerRadius) {
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
            }
        });
        setClipToOutline(true);
    }

    public void setName(String firstName, String lastName) {
        setNameWithStatus(firstName, lastName, null, 0);
    }

    public void setName(String firstName, String lastName, String uid) {
        setNameWithStatus(firstName, lastName, uid, 0);
    }

    public void setNameWithStatus(String firstName, String lastName, @DrawableRes int drawableResourceId) {
        setNameWithStatus(firstName, lastName, null, drawableResourceId);
    }

    public void setUid(String uid) {
        setNameWithStatus(null, null, uid, 0);
    }

    public void setNameWithStatus(String firstName, String lastName, String uid,
                                  @DrawableRes int statusDrawableId) {
        //If any info changed, clear old drawable and redraw whole face.
        if (nameChanged(uid, firstName, lastName) || statusDrawableIdChanged(statusDrawableId)) {
            postInvalidate();
        }
    }

    protected boolean nameChanged(String uid, String firstName, String lastName) {
        boolean nameChanged = !TextUtils.equals(this.firstName, firstName)
                || !TextUtils.equals(this.lastName, lastName);
        if (nameChanged || uidChanged(uid)) {
            this.firstName = firstName;
            this.lastName = lastName;
            defaultFace = DefaultFaceUtils.getFaceDrawable(getContext(), uid, firstName, lastName, colorBack, percentText * percent);
        }
        return nameChanged;
    }

    protected boolean uidChanged(String uid) {
        if (!TextUtils.isEmpty(uid) && uid.startsWith("+")) {
            uid = uid.substring(1);
        }
        boolean uidChanged = !TextUtils.equals(this.uid, uid);
        if (uidChanged) {
            this.uid = uid;
            this.isGroup = PhoneUtils.isGroupByUid(uid);
            setImageDrawable(null);
        }
        return uidChanged;
    }

    protected boolean statusDrawableIdChanged(int statusDrawableId) {
        boolean statusChanged = this.statusDrawableId != statusDrawableId && statusDrawableId != 0 && statusDrawableId != -1;
        if (statusChanged) {
            this.statusDrawableId = statusDrawableId;
            statusDrawable = ContextCompat.getDrawable(getContext(), statusDrawableId);
        }
        return supportStatus && statusChanged;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            if (defaultFace == null) {
                return;
            }
            drawable = defaultFace;
        }
        int widthCanvas = getWidth();
        int heightCanvas = getHeight();
        int backgroundWidth = (int) (widthCanvas * percent);
        int backgroundHeight = (int) (heightCanvas * percent);

        if (backgroundWidth == 0 || backgroundHeight == 0) {
            return;
        }

        if (radiusBackgroundRoundConner == 0) {
            radiusBackgroundRoundConner = widthCanvas * 0.25f;
        }
        int left = (int) ((widthCanvas - backgroundWidth) / 2f);
        int right = left + backgroundWidth;
        int top = (int) ((heightCanvas - backgroundHeight) / 2f);
        int bottom = top + backgroundHeight;
        float backgroundRadius = useCircleBackground ? widthCanvas / 2f : radiusBackgroundRoundConner;
        if (supportStatus) {

            if (radiusRectStatusRoundConner == 0) {
                radiusRectStatusRoundConner = widthCanvas * 0.08f;
            }
            if (widthOfStatus == 0 || heightOfStatus == 0) {
                widthOfStatus = (int) (widthCanvas * 0.2f);
                heightOfStatus = (int) (heightCanvas * 0.2f);
            }

            float xStatusCenter = right - widthOfStatus / 2f;
            float yStatusCenter = top + heightOfStatus / 2f;

            int leftStatus = (int) (xStatusCenter - widthOfStatus / 2f + xOffset);
            int topStatus = (int) (yStatusCenter - heightOfStatus / 2f + yOffset);
            int rightStatus = leftStatus + widthOfStatus;
            int bottomStatus = topStatus + heightOfStatus;

            drawStatus(canvas, widthCanvas, leftStatus, topStatus, rightStatus, bottomStatus);

            path.reset();
            path.addRoundRect(left, top, right, bottom, backgroundRadius, backgroundRadius, Path.Direction.CW);
            canvas.clipPath(path);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas.clipOutPath(pathStatus);
            } else {
                canvas.clipPath(pathStatus, Region.Op.DIFFERENCE);
            }

        } else {
            if (!backgroundRadiusSet) {
                backgroundRadiusSet = true;
                setCornerRadius(backgroundRadius);
            }
        }
        drawable.setBounds(left, top, right, bottom);
        canvas.setDrawFilter(paintFlagsDrawFilter);
        drawable.draw(canvas);
    }

    private void drawStatus(Canvas canvas, int widthCanvas, int leftStatus, int topStatus, int rightStatus, int bottomStatus) {
        if (!needDrawStatus()) {
            return;
        }
        pathStatus.reset();
        float spaceBetweenStatusAndBackground = widthCanvas * 0.04f;
        if (useRoundBreach) {
            float radiusOfRoundStatus = (rightStatus - leftStatus) / 2f;
            float xRoundStatus = rightStatus - radiusOfRoundStatus;
            float yRoundStatus = topStatus + radiusOfRoundStatus;
            paint.setColor(statusColor);
            canvas.drawCircle(xRoundStatus, yRoundStatus, radiusOfRoundStatus, paint);
            pathStatus.addCircle(xRoundStatus, yRoundStatus, radiusOfRoundStatus + spaceBetweenStatusAndBackground, Path.Direction.CW);
        } else {
            statusDrawable.setBounds(leftStatus, topStatus, rightStatus, bottomStatus);
            statusDrawable.draw(canvas);
            pathStatus.addRoundRect(leftStatus - spaceBetweenStatusAndBackground,
                    topStatus - spaceBetweenStatusAndBackground, rightStatus + spaceBetweenStatusAndBackground,
                    bottomStatus + spaceBetweenStatusAndBackground, radiusRectStatusRoundConner, radiusRectStatusRoundConner, Path.Direction.CW);
        }
    }

    private boolean needDrawStatus() {
        return supportStatus && showStatus && !isGroup && (useRoundBreach || statusDrawable != null);
    }

    public boolean isSupportStatus() {
        return supportStatus;
    }

    public void setShowStatus(boolean showStatus) {
        this.showStatus = showStatus;
        postInvalidate();
    }

    public void setStatusDrawable(Drawable statusDrawable) {
        this.statusDrawable = statusDrawable;
        postInvalidate();
    }
}