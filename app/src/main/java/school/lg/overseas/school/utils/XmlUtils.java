package school.lg.overseas.school.utils;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import school.lg.overseas.school.bean.Conf;


/**
 * 解析xml
 */

public class XmlUtils {
    public static Conf getConf (InputStream inputStream){
        List<String> pngs =new ArrayList<>();
        List<Integer> times =new ArrayList<>();
        Conf conf =null;
        DocumentBuilderFactory factory=null;
        DocumentBuilder builder=null;
        Document document=null;
        //首先找到xml文件
        factory=DocumentBuilderFactory.newInstance();
        try {
            //找到xml，并加载文档
            builder=factory.newDocumentBuilder();
            document=builder.parse(inputStream);
            //找到根Element
            Element root=document.getDocumentElement();
            NodeList nodes=root.getElementsByTagName("module");
            //遍历根节点所有子节点,rivers 下所有river
            conf =new Conf();
            String hls = root.getAttribute("hls");
            conf.setHls(hls);
            for (int i = 0; i < nodes.getLength(); i++) {
                String nodeName = nodes.item(i).getAttributes().getNamedItem("name").getNodeValue();
                if("document".equals(nodeName)){
                    Node item = nodes.item(i);
                    NodeList childNodes = item.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        if(childNodes.item(j).getNodeType()!=Node.TEXT_NODE) {
                            NodeList childNodes1 = childNodes.item(j).getChildNodes();
                            for (int k = 0; k < childNodes1.getLength(); k++) {
                                if(childNodes1.item(k).getNodeType()!=Node.TEXT_NODE){
                                    String hls1 = childNodes1.item(k).getAttributes().getNamedItem("hls").getNodeValue();
                                    String starttimestamp = childNodes1.item(k).getAttributes().getNamedItem("starttimestamp").getNodeValue();
                                    Double aDouble = Double.valueOf(starttimestamp);
                                    pngs.add(hls1);
                                    times.add((int) (aDouble*1000));
//                                    Log.i("图片地址及时间",hls1+":"+aDouble*1000);
                                }
                            }
                        }
                    }
                }
            }
            conf.setPngs(pngs);
            conf.setTimes(times);
        }catch (IOException e){
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }finally{
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return conf;
    }

    /**
     * 获取指定HTML标签的指定属性的值
     * @param source 要匹配的源文本
     * @return 属性值列表
     */
    public static List<String> match(String source) {
        List<String> result = new ArrayList<String>();
        String reg = "xmlUrl='(.*?)';";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            String r = m.group(1);
            result.add(r);
        }
        return result;
    }

    public static byte[] readStream(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }

        inputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static String testGetHtml(String urlpath) throws Exception {
        URL url = new URL(urlpath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(6 * 1000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        try {
            int code =conn.getResponseCode();
            Log.i("Html返回码",code+"");
        }catch (Exception e){
            Log.i("错误",e.getMessage());
        }
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            byte[] data = readStream(inputStream);
            String html = new String(data);
//            LogUtils.log(html);
            return html;
        }
        return null;
    }


    public static Conf testGetXml(String urlpath) throws Exception {
        URL url = new URL(urlpath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(6 * 1000);
        conn.setRequestMethod("GET");
//        conn.connect();
//        try {
//            int code =conn.getResponseCode();
//            Log.i("返回码",code+"");
//        }catch (IOException e){
//            Log.i("错误","1");
//            e.printStackTrace();
//        }
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            Conf conf = XmlUtils.getConf(inputStream);
            return conf;
        }
        Log.i("解析错误","null");
        return null;
    }

    public void log(String s){
        if(s.length() > 4000) {
            for(int i=0;i<s.length();i+=4000){
                if(i+4000<s.length())
                    Log.i("表单"+i,s.substring(i, i+4000));
                else
                    Log.i("表单"+i,s.substring(i, s.length()));
            }
        } else
            Log.i("表单",s);
    }

    public static Conf main(String startUrl){
        try {
            String s = XmlUtils.testGetHtml(startUrl);//html表单
            String url = XmlUtils.match(s).get(0);//表单中的xmlUrl
            String urlStart = url.substring(0, url.lastIndexOf("/") + 1);//视频连接开头
            Conf conf = XmlUtils.testGetXml(url);//视频地址，PPT（图片，开始时间）
            conf.setUrlStart(urlStart);
            return conf;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
