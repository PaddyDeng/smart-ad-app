package school.lg.overseas.school.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import school.lg.overseas.school.bean.Login;
import school.lg.overseas.school.bean.PersonalDetail;


/**
 * Created by Administrator on 2017/12/11.
 */

public class SharedPreferencesUtils {

    private final static String PREFS_SHOW_ACTION_TIMES = "prefs_show_action_times";
    private final static String PREFS_SHOW_ACTION_ID = "prefs_show_action_id";
    protected static String PREFS_NAME = "SHAREDPREF_SCHOOL";
    private final static String PREFS_IMAGE_FILE_NAME = "prefs_image_file_name";

    public static String getActionId(Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0).getString(PREFS_SHOW_ACTION_ID,"");
    }

    public static String getImageFileName(Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0).getString(PREFS_IMAGE_FILE_NAME,"");
    }
    public static void setPrefsImageFileName(Context c, String id) {
        setString(PREFS_IMAGE_FILE_NAME, id, c);
    }
    public static void setActionId(Context c, String id) {
        setString(PREFS_SHOW_ACTION_ID, id, c);
    }

    public static int getCurrentTIMES(Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0).getInt(PREFS_SHOW_ACTION_TIMES, 0);
    }

    public static void setCurrentTIMES(Context c, int time) {
        setInt(PREFS_SHOW_ACTION_TIMES, time, c);
    }
    public static void setInt(String key, int value, Context c) {
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setString(String key, String value, Context c) {
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setPersonal(Context context, PersonalDetail personal){
        SharedPreferences sp =context.getSharedPreferences("Personal",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("id",personal.getId());
        edit.putString("userName",personal.getUserName());
        edit.putString("userPass",personal.getUserPass());
        edit.putString("nickname",personal.getNickname());
        edit.putString("phone",personal.getPhone());
        edit.putString("school",null==personal.getSchool()?"":personal.getSchool());
        edit.putString("major",null==personal.getMajor()?"":personal.getMajor());
        edit.putString("grade",null==personal.getGrade()?"":personal.getGrade());
        edit.putString("email",null==personal.getEmail()?"":personal.getEmail());
        edit.putString("createTime",personal.getCreateTime());
        edit.putString("image",null==personal.getImage()?"":personal.getImage());
        edit.putString("remark",null==personal.getRemark()?"":personal.getRemark());
        edit.putString("uid",personal.getUid());
        edit.putString("follow",personal.getFollow());
        edit.putString("fans",personal.getFans());
        edit.putString("questionNum",personal.getQuestionNum());
        edit.putString("answerNum",personal.getAnswerNum());
        edit.commit();
    }

    public static void exitLogin(Context context){
        context.getSharedPreferences("Personal", Context.MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences("Sessions",Context.MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences("UserInfo",Context.MODE_PRIVATE).edit().clear().commit();
    }

    public static PersonalDetail getPersonalDetail(Context context){
        PersonalDetail personalDetail =new PersonalDetail();
        SharedPreferences sp = context.getSharedPreferences("Personal", Context.MODE_PRIVATE);
        personalDetail.setId( sp.getString("id",""));
        personalDetail.setUserName(sp.getString("userName",""));
        personalDetail.setUserPass(sp.getString("userPass",""));
        personalDetail.setNickname(sp.getString("nickname",""));
        personalDetail.setPhone(sp.getString("phone",""));
        personalDetail.setSchool(sp.getString("school",""));
        personalDetail.setMajor(sp.getString("major",""));
        personalDetail.setGrade(sp.getString("grade",""));
        personalDetail.setEmail(sp.getString("email",""));
        personalDetail.setCreateTime(sp.getString("createTime",""));
        personalDetail.setImage(sp.getString("image",""));
        personalDetail.setRemark(sp.getString("remark",""));
        personalDetail.setUid(sp.getString("uid",""));
        personalDetail.setFollow(sp.getString("follow",""));
        personalDetail.setFans(sp.getString("fans",""));
        personalDetail.setQuestionNum(sp.getString("questionNum",""));
        personalDetail.setAnswerNum(sp.getString("answerNum",""));
        return personalDetail;
    }

    //保存用户登录信息
    public static void setLogin(Context context, Login login){
        SharedPreferences sp =context.getSharedPreferences("UserInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("uid",login.getUid());
        edit.putString("username",login.getUsername());
        edit.putString("password",login.getPassword());
        edit.putString("email",login.getEmail());
        edit.putString("phone",login.getPhone());
        edit.putString("nickname",login.getNickname());
        edit.commit();
    }

    public static Login getUserInfo(Context context){
        Login login =new Login();
        SharedPreferences sp = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        login.setUid(sp.getString("uid", ""));
        login.setUsername(sp.getString("username",""));
        login.setEmail(sp.getString("email",""));
        login.setPhone(sp.getString("phone",""));
        login.setNickname(sp.getString("nickname",""));
        login.setPassword(sp.getString("password",""));
        return login;
    }

    public static void setSession(Context context,int i,String session){
        SharedPreferences sp = context.getSharedPreferences("Sessions", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        String key="";
        switch (i){
            case 0:
                key="toefl";
                break;
            case 1:
                key="smartapply";
                break;
            case 2:
                key="gmat";
                break;
            case 3:
                key="gossip";
                break;
        }
        edit.putString(key,session);
        edit.commit();
    }

    public static String getSession(Context context,int i){
        String key = "";
        switch (i){
            case 0:
                key="toefl";
                break;
            case 1:
                key="smartapply";
                break;
            case 2:
                key="gmat";
                break;
            case 3:
                key="gossip";
                break;
        }
        SharedPreferences sp = context.getSharedPreferences("Sessions", Context.MODE_PRIVATE);
        String session = sp.getString(key, "");
        return session;
    }


    //验证是否登录过
    public static boolean isLogin(Context context){
        SharedPreferences sp = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String uid = sp.getString("uid", "");
        if(!TextUtils.isEmpty(uid)){
            return true;
        }
        return false;
    }


    //保存用户账户密码
    public static void setPassword(Context context,String phone,String pass){
        SharedPreferences sp =context.getSharedPreferences("PhoneAndPass",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if(!TextUtils.isEmpty(phone))edit.putString("phone",phone);
        if(!TextUtils.isEmpty(pass))edit.putString("pass",pass);
        edit.commit();
    }

    public static Map<String,String> getPassword(Context context){
        Map<String,String> map =new HashMap<>();
        SharedPreferences sp =context.getSharedPreferences("PhoneAndPass",Context.MODE_PRIVATE);
        map.put("phone",sp.getString("phone",""));
        map.put("pass",sp.getString("pass",""));
        return map;
    }
    public static void setQuestionTag(Context context,String s){
        SharedPreferences sp =context.getSharedPreferences("QuestionTag",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("tags",s);
        edit.commit();
    }
    public static String getQuestionTag(Context context){
        SharedPreferences sp =context.getSharedPreferences("QuestionTag",Context.MODE_PRIVATE);
        return sp.getString("tags", "");
    }


    public static void setDelete(Context context,String tag,boolean isDelete){
        SharedPreferences sp = context.getSharedPreferences("DeleteRecord",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(tag,isDelete);
        edit.commit();
    }
    public static boolean getDelete(Context context,String tag){
        SharedPreferences sp = context.getSharedPreferences("DeleteRecord", Context.MODE_PRIVATE);
        return sp.getBoolean(tag, false);
    }


    public static void setPost(Context context,String tag,boolean isPost){
        SharedPreferences sp =context.getSharedPreferences("PostNew",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit =sp.edit();
        edit.putBoolean(tag,isPost);
        edit.commit();
    }
    public static boolean getPost(Context context,String tag){
        SharedPreferences sp =context.getSharedPreferences("PostNew",Context.MODE_PRIVATE);
        return sp.getBoolean(tag,false);
    }

    public static void setCountry(Context context,int country){
        SharedPreferences sp =context.getSharedPreferences("country",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit =sp.edit();
        edit.putInt("c",country);
        edit.commit();
    }
    public static int getCountry(Context context){
        SharedPreferences sp =context.getSharedPreferences("country",Context.MODE_PRIVATE);
        return sp.getInt("c",0);
    }

    public static void setNewsTags(Context context,String tags){
        SharedPreferences sp =context.getSharedPreferences("newsTags",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit =sp.edit();
        edit.putString("tags",tags);
        edit.commit();
    }
    public static String getNewsTags(Context context){
        SharedPreferences sp =context.getSharedPreferences("newsTags",Context.MODE_PRIVATE);
        return sp.getString("tags","");
    }
}
