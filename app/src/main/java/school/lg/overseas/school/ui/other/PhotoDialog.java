package school.lg.overseas.school.ui.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import school.lg.overseas.school.R;
import school.lg.overseas.school.callback.SelectListener;

/**
 * Created by Administrator on 2018/1/18.
 */

public class PhotoDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private SelectListener listener;
    private TextView photograph,photo_album,cancel;
    public PhotoDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context=context;
    }

    public void setListener(SelectListener listener){
        this.listener =listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_phote);
        photograph = (TextView) findViewById(R.id.photograph);
        photo_album = (TextView) findViewById(R.id.photo_album);
        cancel = (TextView) findViewById(R.id.cancel);
        photograph.setOnClickListener(this);
        photo_album.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                dismiss();
                break;
            case R.id.photograph:
                listener.select(0);
                dismiss();
                break;
            case R.id.photo_album:
                listener.select(1);
                dismiss();
                break;
        }
    }
}
