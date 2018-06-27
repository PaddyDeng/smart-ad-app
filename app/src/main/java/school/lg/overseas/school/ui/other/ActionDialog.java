package school.lg.overseas.school.ui.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import school.lg.overseas.school.R;
import school.lg.overseas.school.bean.HomeAdver;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.utils.GlideUtil;
import school.lg.overseas.school.utils.MeasureUtil;

/**
 * Created by fire on 2017/8/30  14:51.
 */

public class ActionDialog extends BaseNoBackgDialog {

    ImageView actionIv;
    ImageView actionClose;
    private HomeAdver mActionData;

    public static ActionDialog getInstance(HomeAdver actionData) {
        ActionDialog dialog = new ActionDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("action_data", actionData);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        Bundle bundle = getArguments();
        if (bundle == null) return;
        mActionData = bundle.getParcelable("action_data");
        actionIv = (ImageView) mRootView.findViewById(R.id.action_iv);
        actionClose = (ImageView) mRootView.findViewById(R.id.action_close);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mActionData != null)
//            new GlideUtils().load(getActivity(), NetworkTitle.DomainSmartApplyResourceNormal + mActionData.getImage(), actionIv);
            GlideUtil.loadNoDefalut(NetworkTitle.DomainSmartApplyResourceNormal + mActionData.getImage(), actionIv);

        actionIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionData == null) return;
                DealActivity.startDealActivity(getActivity(), "", mActionData.getUrl());
                dismiss();
            }
        });
        actionClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getRootViewId() {
        return R.layout.dialog_action_layout;
    }

    @Override
    protected int[] getWH() {
        int[] wh = {(int) (MeasureUtil.getScreenSize(getActivity()).x * 0.8), getDialog().getWindow().getAttributes().height};
        return wh;
    }

}
