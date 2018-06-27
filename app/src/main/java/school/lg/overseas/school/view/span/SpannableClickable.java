package school.lg.overseas.school.view.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;


import school.lg.overseas.school.MyApplication;
import school.lg.overseas.school.R;

public abstract class SpannableClickable extends ClickableSpan implements View.OnClickListener {

    private int DEFAULT_COLOR_ID = R.color.color_blue;
    /**
     * text颜色
     */
    private int textColor ;

    public SpannableClickable() {
        this.textColor = MyApplication.getInstance().getResources().getColor(DEFAULT_COLOR_ID);
    }

    public SpannableClickable(int textColor){
        this.textColor = textColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        ds.setColor(textColor);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
