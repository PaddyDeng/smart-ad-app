package school.lg.overseas.school.ui.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import school.lg.overseas.school.R;
import school.lg.overseas.school.callback.ICallBack;
import school.lg.overseas.school.view.MultiImage.MeasureUtil;

/**
 * Created by fire on 2017/8/1  16:42.
 */

public class InternetBusyTipDialog extends BaseNoBackgDialog {

    private static ICallBack mCallBack;

    public static InternetBusyTipDialog getInstance(ICallBack callBack) {
        InternetBusyTipDialog simpleDialog = new InternetBusyTipDialog();
        simpleDialog.mCallBack = callBack;
        return simpleDialog;
    }
    ImageView img;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onSuccess("");
                    mCallBack = null;
                }
                dismiss();
            }
        });
    }

    private void findView(View view ){
        img = (ImageView) view.findViewById(R.id.internet_poor_iv);
    }
    @Override
    protected int[] getWH() {
        int[] wh = {MeasureUtil.getScreenSize(getActivity()).x, MeasureUtil.getScreenSize(getActivity()).y};
        return wh;
    }

    @Override
    protected int getRootViewId() {
        return R.layout.dialog_internet_busy_layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCallBack != null) {
            mCallBack = null;
        }
    }
}
