package connorhenke.com.lifts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by connor on 3/22/2018.
 */

public class WeightView extends View {

    private Paint paint;

    private int front45;
    private int back45;
    private int fourfives;

    private int front25;
    private int back25;
    private int twofives;

    private int front10;
    private int back10;
    private int tens;

    private int front5;
    private int back5;
    private int fives;

    private int front2point5;
    private int back2point5;
    private int twopointfives;

    private float viewHeight;
    private float viewWidth;
    private float totalWidth;

    public WeightView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public WeightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public WeightView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();

        front45 = ContextCompat.getColor(context, R.color.pomegranate);
        back45 = ContextCompat.getColor(context, R.color.cinnabar);

        front25 = ContextCompat.getColor(context, R.color.san_marino);
        back25 = ContextCompat.getColor(context, R.color.sapphire);

        front10 = ContextCompat.getColor(context, R.color.gorse);
        back10 = ContextCompat.getColor(context, R.color.bright_sun);

        front5 = ContextCompat.getColor(context, R.color.fruit_salad);
        back5 = ContextCompat.getColor(context, R.color.apple);

        front2point5 = ContextCompat.getColor(context, R.color.purple_heart);
        back2point5 = ContextCompat.getColor(context, R.color.gigas);

        fourfives = 1;
        twofives = 1;
        tens = 1;
        fives = 1;
        twopointfives = 1;
    }

    private void calculateTotalWidth() {
        float height = getHeight(); // 500f
        float width = height * 0.65496f; // 327.48f
        totalWidth = 0f;

        if (fourfives > 0) {
            float offset = width * 0.087f;
            totalWidth += width + offset + 5f;
            for (int i = 1; i < fourfives; i++) {
                totalWidth += offset;
            }
        } else if (twofives > 0) {
            float plateWidth = 0.75f * width;
            float offset = plateWidth * 0.087f;
            totalWidth += plateWidth + offset + 5f;
            for (int i = 1; i < twofives; i++) {
                totalWidth += offset;
            }
        } else if (tens > 0) {
            float plateWidth = 0.55f * width;
            float offset = plateWidth * 0.087f;
            totalWidth += plateWidth + offset + 5f;
            for (int i = 1; i < tens; i++) {
                totalWidth += offset;
            }
        } else if (fives > 0) {
            float plateWidth = 0.45f * width;
            float offset = plateWidth * 0.087f;
            totalWidth += plateWidth + offset + 5f;
            for (int i = 1; i < fives; i++) {
                totalWidth += offset;
            }
        } else if (twopointfives > 0) {
            float plateWidth = 0.3f * width;
            float offset = plateWidth * 0.087f;
            totalWidth += plateWidth + offset + 5f;
            for (int i = 1; i < twopointfives; i++) {
                totalWidth += offset;
            }
        }
    }

    public void setWeight(int weight) {
        weight -= 45;
        fourfives = weight / 90;
        if (fourfives > 0) {
            weight = weight % (fourfives * 90);
        }

        twofives = weight / 50;
        if (twofives > 0) {
            weight = weight % (twofives * 50);
        }

        tens = weight / 20;
        if (tens > 0) {
            weight = weight % (tens * 20);
        }

        fives = weight / 10;
        if (fives > 0) {
            weight = weight % (fives * 10);
        }

        twopointfives = weight / 5;

        invalidate();
    }

    private void drawPlate(Paint paint, Canvas canvas, int frontColor, int backColor, float offset, int i, float width, float plateHeight, float plateWidth) {
        float plateOffset = offset * i;
        float centeringHeightOffset = (getHeight() - getHeight()) / 2;
        float centeringWidthOffset = (getWidth() - totalWidth) / 2;

        // Draw back of plate
        paint.setColor(backColor);
        paint.setStyle(Paint.Style.FILL);
        float top = (getHeight() - plateHeight) / 2 + centeringHeightOffset;
        float bottom = top + plateHeight - centeringHeightOffset;
        float left = (width - plateWidth) / 2 + centeringWidthOffset;
        float right = left + plateWidth;
        canvas.drawOval(plateOffset + left, top, right - offset + plateOffset, bottom, paint);
        canvas.drawRect((plateOffset + left + right - offset + plateOffset) / 2, top, (left + offset + plateOffset + right + plateOffset) / 2, bottom, paint);

        // Outline back plate
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawOval(plateOffset + left, top, right - offset + plateOffset, bottom, paint);
        canvas.drawLine((plateOffset + left + right - offset + plateOffset) / 2, top, (left + offset + plateOffset + right + plateOffset) / 2, top, paint);
        canvas.drawLine((plateOffset + left + right - offset + plateOffset) / 2, bottom, (left + offset + plateOffset + right + plateOffset) / 2, bottom, paint);

        // Draw front plate
        paint.setColor(frontColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawOval(left + offset + plateOffset, top, right + plateOffset, bottom, paint);

        // Outline front plate
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawOval(left + offset + plateOffset, top, right + plateOffset, bottom, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateTotalWidth();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        float height = getHeight(); // 500f
        float width = height * 0.65496f; // 327.48f
        float offset = width * 0.087f;

        for(int i = 0; i < fourfives; i++) {
            drawPlate(paint, canvas, front45, back45, offset, i, width, height, width);
        }

        for(int i = 0; i < twofives; i++) {
            drawPlate(paint, canvas, front25, back25, offset, i + fourfives, width, height * 0.75f, width * 0.75f);
        }

        for(int i = 0; i < tens; i++) {
            drawPlate(paint, canvas, front10, back10, offset, i + fourfives + twofives, width, height * 0.55f, width * 0.55f);
        }

        for(int i = 0; i < fives; i++) {
            drawPlate(paint, canvas, front5, back5, offset, i + fourfives + twofives + tens, width, height * 0.45f, width * 0.45f);
        }

        for(int i = 0; i < twopointfives; i++) {
            drawPlate(paint, canvas, front2point5, back2point5, offset, i + fourfives + twofives + tens + fives, width, height * 0.3f, width * 0.3f);
        }
    }
}
