package school.lg.overseas.school.ui.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import school.lg.overseas.school.R;
import school.lg.overseas.school.db.PracticeManager;
import school.lg.overseas.school.db.Reading;
import school.lg.overseas.school.ui.home.EnrollActivity;

/**
 * Created by Administrator on 2018/1/18.
 */

public class MajorDialog extends Dialog{
    private Context context;
    private Reading reading;
    private TextView cn_name,en_name,tv1,tv2;
    private ImageView collection_iv;
    private View ll;
    private boolean isColle;
    public MajorDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context =context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_major);
        findView();
        setClick();
    }

    public void setData(Reading reading){
        this.reading=reading;
        //存入最近浏览
        Reading reading1 =reading;
        reading1.setTag(0);
        PracticeManager.getInstance().insert(reading1);

        List<Reading> readings = PracticeManager.getInstance().queryForId(1, 1, reading.getId());
        if(null==readings||readings.size()==0){
            collection_iv.setSelected(false);
            isColle=false;
        }else{
            collection_iv.setSelected(true);
            isColle=true;
        }
        cn_name.setText(reading.getTitle());
        en_name.setText(reading.getEnMajprName());
    }

    private void setClick() {
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        collection_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isColle){
                    PracticeManager.getInstance().deleteOne(1,1,reading.getId());
                }else{
                    reading.setTag(1);
                    PracticeManager.getInstance().insert(reading);
                }
                isColle=!isColle;
                collection_iv.setSelected(isColle);
            }
        });
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnrollActivity.start01(context,reading.getName(),reading.getTitle());
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.start(context,reading.getS());
            }
        });
    }

    private void findView() {
        ll = findViewById(R.id.ll);
        cn_name = (TextView) findViewById(R.id.cn_name);
        en_name = (TextView) findViewById(R.id.en_name);
        collection_iv = (ImageView) findViewById(R.id.collection_iv);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
    }
}
