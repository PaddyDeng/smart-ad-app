package school.lg.overseas.school.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenShotUtil {

    private static Bitmap takeScreenShot(Activity activity) {
        //View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        //获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);

        //获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        //去掉标题栏
        //Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return b;
    }

    //保存
    private static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 70, fos);
                fos.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //程序入口
    public static void shoot(Activity a) {
        savePic(takeScreenShot(a), a.getFilesDir().getPath() + "share.png");
        savePic(takeScreenShot(a), a.getExternalCacheDir().getPath() +"share.png");
    }

}
