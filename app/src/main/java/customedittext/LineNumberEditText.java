package customedittext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;

import codeondroid.codeondroid.R;

public class LineNumberEditText extends androidx.appcompat.widget.AppCompatEditText {
    private Rect rect;
    private Paint paint;
    final LineNumberEditText me;


    public LineNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        me = this;
        rect = new Rect();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.CodeColor));
        paint.setTextSize(40);
        setHorizontallyScrolling(true);
        setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int baseline = getBaseline();
        for (int i = 0; i < getLineCount(); i++) {
            canvas.drawText(String.format("  %d ", (i+1)), rect.left, baseline, paint);
            baseline += getLineHeight();
        }
    }
}