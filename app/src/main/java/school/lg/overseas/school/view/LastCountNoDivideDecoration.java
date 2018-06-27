package school.lg.overseas.school.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * 最后一行不绘制间隔
 */

public class LastCountNoDivideDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider ;

    public LastCountNoDivideDecoration(Context context){
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c ,parent);
    }


    private int getSpanCount(RecyclerView parent){
        int spanCount = - 1 ;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            spanCount =  ((GridLayoutManager) layoutManager).getSpanCount();
        }else if (layoutManager instanceof LinearLayoutManager){
            spanCount = layoutManager.getChildCount();
        }else if (layoutManager instanceof StaggeredGridLayoutManager){
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount ;
    }

    public void drawVertical(Canvas c ,RecyclerView parent){
        final  int childCount  = parent.getChildCount();
        for (int i = 0 ; i < childCount ; i++){
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getLeft() + params.leftMargin ;
            final int right = left +    mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private boolean isLastRaw(RecyclerView parent ,int pos , int spanCount ,int childCount){
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager() ;
        if (layoutManager instanceof GridLayoutManager){
            childCount = childCount - childCount % spanCount ;
            if (pos >= childCount) return true ;
        }else if (layoutManager instanceof  LinearLayoutManager){
            childCount =  childCount - childCount % spanCount ;
            if (pos >= childCount) return true ;
        }
        return false ;
    }



    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
        {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }
}
