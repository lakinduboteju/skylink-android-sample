package sg.com.temasys.skylink.sdk.sampleapp.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import sg.com.temasys.skylink.sdk.sampleapp.R;

public class OptionButton extends android.support.v7.widget.AppCompatButton {

    private Paint mPaint;

    public OptionButton(Context context) {
        super(context);
        init(null, context);
    }

    public OptionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }

    public OptionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    private void init(AttributeSet attrs, Context context) {
        // create paint object
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // set style
        this.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // set color
        this.mPaint.setColor(getResources().getColor(R.color.primary));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerXOnView = getWidth() / 2;
        int centerYOnView = getHeight() / 2;

        // draw 3 dots in vertical line
        canvas.drawCircle(centerXOnView, centerYOnView - 20f, 5f, mPaint);
        canvas.drawCircle(centerXOnView, centerYOnView, 5f, mPaint);
        canvas.drawCircle(centerXOnView, centerYOnView + 20f, 5f, mPaint);
    }

}
