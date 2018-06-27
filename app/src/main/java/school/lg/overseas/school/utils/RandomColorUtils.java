package school.lg.overseas.school.utils;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by Administrator on 2017/12/22.
 */

public class RandomColorUtils {
    public static int getRandomColor(){
        Random random =new Random();
        int i = random.nextInt(16777210)+1;//1是白色
        String hex = Integer.toHexString(0-i);
        return Color.parseColor("#"+hex);
    }
}
