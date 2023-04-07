package im.turbo.baseui.roundcornerlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import im.turbo.baseui.utils.UiUtils;

/**
 * created by zhaoyuntao
 * on 2021/2/24
 * description:
 */
public class RoundCornerImageContainer extends RoundCornerFrameLayout {

    private Path path;
    private Rect rect;
    private RectF rectF;
    private Paint paint;
    private float[] radiusArray;
    private float strokeWidth;
    private int cornerBorderColor;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    public RoundCornerImageContainer(Context context) {
        this(context, null);
    }

    public RoundCornerImageContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerImageContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCornerBorderVisibility(true, Color.BLACK);
    }

    public void setCornerBorderVisibility(boolean visible, int cornerBorderColor) {
        setWillNotDraw(!visible);
        this.cornerBorderColor = cornerBorderColor;
        this.strokeWidth = UiUtils.dip2px(20);
        path = new Path();
        rectF = new RectF();
        rect = new Rect();
        paint = new Paint();
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        float cornerRadius = getCornerRadius() / 2f + 1;
        radiusArray = new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius};
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (path != null) {
            canvas.setDrawFilter(paintFlagsDrawFilter);
            canvas.save();
            getDrawingRect(rect);
            float width = 10;
            rectF.set(rect.left + width, rect.top + width, rect.right - width, rect.bottom - width);
            path.addRoundRect(rectF, radiusArray, Path.Direction.CW);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, paint);
            canvas.restore();
        }
        super.onDraw(canvas);
//        drawCornerBorder(canvas);
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
        drawCornerBorder(canvas);
    }

    private void drawCornerBorder(Canvas canvas) {
//        canvas.drawColor(cornerBorderColor);
        if (path != null) {
            canvas.setDrawFilter(paintFlagsDrawFilter);
            canvas.save();
            getDrawingRect(rect);
            float width = strokeWidth / 2f;
            rectF.set(rect.left + width, rect.top + width, rect.right - width, rect.bottom - width);
            path.addRoundRect(rectF, radiusArray, Path.Direction.CW);
            paint.setColor(cornerBorderColor);
            paint.setStrokeWidth(strokeWidth*2);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, paint);
            canvas.restore();
        }
    }
}
