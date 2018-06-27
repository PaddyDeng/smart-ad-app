package school.lg.overseas.school.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class CustomViewPager extends ViewPager {
    private Map<Integer, Integer> map = new HashMap<>();
    private int currentPage;

    public CustomViewPager(Context context) {
        this(context, null);

    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int measureHeight(int measureSpec, View view) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (view != null) {
                result = view.getMeasuredHeight();

            }

            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//


        int height = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            map.put(i,h);
        }

        if (map.size() > currentPage) {
            height = map.get(currentPage);
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);



    }

    /**
     * 在切换tab的时候，重置ViewPager的高度
     *
     * @param current
     */
    public void resetHeight(int current) {
        this.currentPage = current;
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        if (map.size() > currentPage) {
            if (params == null) {
                params = new MarginLayoutParams(LayoutParams.MATCH_PARENT, map.get(current));
            } else {
                params.height = map.get(current);
            }
            this.setLayoutParams(params);
        }
    }

    /**
     * 获取、存储每一个tab的高度，在需要的时候显示存储的高度
     *
     * @param current tab的position
     * @param height  当前tab的高度
     */
    public void addHeight(int current, int height) {
        map.put(current, height);
    }

}