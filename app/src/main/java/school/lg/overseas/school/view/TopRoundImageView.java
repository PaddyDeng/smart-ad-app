package school.lg.overseas.school.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * 上半部分圆角
 */
public class TopRoundImageView extends ImageView{

    float width,height;

    public TopRoundImageView(Context context) {
        this(context, null);
    }

    public TopRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopRoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (width > 10 && height > 10) {
            Path path = new Path();
            path.moveTo(10, 0);
            path.lineTo(width - 10, 0);
            path.quadTo(width, 0, width, 10);
            path.lineTo(width, height);
            path.lineTo(0, height);
            path.lineTo(0, 10);
            path.quadTo(0, 0, 10, 0);
            canvas.clipPath(path);
        }

        super.onDraw(canvas);
    }
}
