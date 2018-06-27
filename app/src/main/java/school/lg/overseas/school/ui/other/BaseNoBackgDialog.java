package school.lg.overseas.school.ui.other;

import android.os.Bundle;
import android.widget.EditText;

import school.lg.overseas.school.utils.Utils;
import school.lg.overseas.school.view.MultiImage.MeasureUtil;


/**
 * Created by fire on 2017/8/1  16:43.
 * 点击外部不 dismiss
 */

public abstract class BaseNoBackgDialog extends BaseDialogView {

    protected String getEditTxt(EditText editText) {
        return Utils.getEditTextString(editText);
    }

    protected boolean getHttpCodeSucc(int code) {
        if (code ==1 ) {
            return true;
        }
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setCancelable(false);
    }

    @Override
    protected int[] getWH() {
        int[] wh = {(int) (MeasureUtil.getScreenSize(getActivity()).x * 0.8), getDialog().getWindow().getAttributes().height};
        return wh;
    }

    @Override
    protected boolean isNoTitle() {
        return true;
    }
}
