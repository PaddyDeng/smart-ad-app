package school.lg.overseas.school.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawableResource;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import school.lg.overseas.school.R;
import school.lg.overseas.school.http.NetworkTitle;
import school.lg.overseas.school.ui.home.SchoolDetailsActivity;

/**
 * 图片加载工具
 */

public class GlideUtils {
    //普通加载图片
    public void load(Context context,String url, ImageView iv){
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.short_defult)//占位图
                .error(R.mipmap.short_defult)//加载错误占位图
                .crossFade(300)//过渡动画
                .into(iv);
    }
    public void loadGuide(Context context,int id,ImageView iv){
        Glide.with(context)
                .load(id)
                .into(iv);
    }
    //加载本地资源图片
    public void load(Context context,int id,ImageView iv){
        Glide.with(context)
                .load(id)
                .placeholder(R.mipmap.short_defult)//占位图
                .error(R.mipmap.short_defult)//加载错误占位图
                .crossFade(300)//过渡动画
                .into(iv);
    }
    //加载圆形图片
    public void loadCircle(final Context context, String url, final ImageView iv){
        Glide.with(context)
                .load(url)
                .asBitmap()
                .skipMemoryCache(false)
                .placeholder(R.mipmap.head_defult)//占位图
                .error(R.mipmap.head_defult)//加载错误占位图
                .into(new BitmapImageViewTarget(iv){
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                iv.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    //加载本地圆形图片
    public void loadBitmapCircle(final Context context, Bitmap bitmap, final ImageView iv){
        Glide.with(context)
                .load(bitmap)
                .placeholder(R.mipmap.head_defult)//占位图
                .error(R.mipmap.head_defult)//加载错误占位图
                .transform(new GlideCircleTransform(context))
                .into(iv);
    }

    //加载本地圆形图片
    public void loadResCircle(final Context context, int resId, final ImageView iv){
        Glide.with(context)
                .load(resId)
                .placeholder(R.mipmap.head_defult)//占位图
                .error(R.mipmap.head_defult)//加载错误占位图
                .transform(new GlideCircleTransform(context))
                .into(iv);
    }


    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
