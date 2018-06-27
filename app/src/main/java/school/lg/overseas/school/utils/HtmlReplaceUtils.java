package school.lg.overseas.school.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/10/26.
 */

public class HtmlReplaceUtils {
    //替换特殊字符
    public static String replaceAllToCharacter(String s){
        s=s.replaceAll("&lt;p&gt;","").replaceAll("&lt;/p&gt;","\r\n")
                .replaceAll("&lt;br/&gt;","\r\n").replaceAll("&amp;"," ")
                .replaceAll("&quot;","'").replaceAll("&nbsp;"," ")
                .replaceAll("nbsp;"," ").replaceAll("&#39"," ")
                .replaceAll("#39"," ");
        while (s.contains("&lt;")) {
            String newS=s;
            String patternStr = "&lt;(.*?)&gt;";
            Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(newS);
            String group = null;
            while (matcher.find()) {
                group = matcher.group(0);
            }
            s=newS.replace(group,"");
        }
        return s;
    }

    public static String replaceAllToLable(String s){
        s =s.replaceAll("<p>","").replaceAll("</p>","\r\n").replaceAll("<br/>","\r\n").replaceAll("</span>","");
        while (s.contains("<")){
            String newS=s;
            String label = getLabel(s, "<(.*?)>");
            s=newS.replace(label,"");
        }
        return replaceAllToCharacter(s);
    }

    private static String getLabel(String s, String label) {
        String patternStr = label;
        Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);
        String labels=null;
        while (matcher.find()){
            labels = matcher.group(0);
        }
        return labels;
    }

    public static String h2s(String s){
        String htmlStr = s;
        String textStr = "";
        String scriptRegEx = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
        String styleRegEx = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
        String htmlRegEx1 = "<[^>]*>";
        String htmlRegEx2 = "<[^>]*";
        try {
            Pattern scriptPattern = Pattern.compile(scriptRegEx, Pattern.CASE_INSENSITIVE);
            Matcher scriptMatcher = scriptPattern.matcher(htmlStr);
            htmlStr = scriptMatcher.replaceAll("");
            Pattern stylePattern = Pattern.compile(styleRegEx,
                    Pattern.CASE_INSENSITIVE);
            Matcher styleMatcher = stylePattern.matcher(htmlStr);
            htmlStr = styleMatcher.replaceAll("");
            Pattern htmlPattern1 = Pattern.compile(htmlRegEx1,
                    Pattern.CASE_INSENSITIVE);
            Matcher htmlMatcher1 = htmlPattern1.matcher(htmlStr);
            htmlStr = htmlMatcher1.replaceAll("");
            Pattern htmlPattern2 = Pattern.compile(htmlRegEx2,Pattern.CASE_INSENSITIVE);
            Matcher htmlMatcher2 = htmlPattern2.matcher(htmlStr);
            htmlStr = htmlMatcher2.replaceAll("");
            textStr = htmlStr;
        } catch (Exception e) {
            System.err.println("->htmlToText(String inputString):"+ e.getMessage());
        }
        textStr = textStr.replaceAll("&acute;", "\'");
        textStr = textStr.replaceAll("&quot;", "\"");
        textStr = textStr.replaceAll("&lt;", "<");
        textStr = textStr.replaceAll("&gt;", ">");
        textStr = textStr.replaceAll("&nbsp;", " ");
        textStr = textStr.replaceAll("&amp;", "&");
        return textStr;
    }
}
