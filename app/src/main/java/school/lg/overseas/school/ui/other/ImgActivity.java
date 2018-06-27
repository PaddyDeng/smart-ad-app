package school.lg.overseas.school.ui.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import school.lg.overseas.school.R;
import school.lg.overseas.school.base.BaseActivity;
import school.lg.overseas.school.bean.Apply;
import school.lg.overseas.school.utils.GlideUtils;

/**
 * Created by Administrator on 2017/12/28.
 */

public class ImgActivity extends AppCompatActivity{
    public static void start(Context context,String url){
        Intent intent =new Intent(context,ImgActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        if(null!=getIntent()) {
            String url = getIntent().getStringExtra("url");
            ImageView iv = (ImageView) findViewById(R.id.iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            new GlideUtils().load(this, url, iv);
        }
    }
}
