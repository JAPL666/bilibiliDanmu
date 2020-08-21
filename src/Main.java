import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        /*
        * 泡芙喵 1313
        * 吉原悠一 225
        * 泠鸢yousa 593
        * 呜米 22384516
        * */

        /*Scanner id=new Scanner(System.in);
        start(id.next());*/
        start("593");

    }
    public static void start(String id){
        Stack stack=new Stack();

        while (true){
            String s1=Post("https://api.live.bilibili.com/xlive/web-room/v1/dM/gethistory","","roomid="+getRoomid(id));

            JSONObject json1 = json(s1);
            String s2 = json1.get("data").toString();

            JSONObject json2 = json(s2);
            String s3 = json2.get("room").toString();

            JSONArray jsonArray = new JSONArray(s3);

            for (Object name : jsonArray) {
                JSONObject json3 = json(name.toString());
                String text = json3.get("text").toString();
                String nickname = json3.get("nickname").toString();
                String uid = json3.get("uid").toString();
                String timeline = json3.get("timeline").toString();

                if(stack.size()>=100){//移除旧弹幕
                    for (int i=0;i<50;i++){
                        stack.remove(0);
                    }
                }

                if (stack.search(uid+timeline)==-1){
                    stack.push(uid+timeline);//弹幕入栈
                    System.out.println("(uid:"+uid+") "+timeline+" "+nickname+"："+text);
                }else{

                }
            }
        }
    }
    public static String getRoomid(String id){
        String x="https://live.bilibili.com/"+id;
        String str=GET(x,"");
        if(str.contains("roomId")){
            return Regex("\"roomId\":\"([^\"]+)\",",str.replace("\\",""));
        }else{
            return Regex("\"room_id\":([^\"]+),",str);
        }
    }
    public static JSONObject json(String string){
        JSONObject json = new JSONObject(string);
        return json;
    }
    public static String Post(String url,String Cookies,String string) {

        try {
            URL url2=new URL(url);
            HttpURLConnection connection=(HttpURLConnection)url2.openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Connection", "keep-alive");
            connection.addRequestProperty("Pragma", "no-cache");
            connection.addRequestProperty("content-type", "application/x-www-form-urlencoded");
            connection.addRequestProperty("Origin","https://live.bilibili.com");
            connection.addRequestProperty("Accept-Language", "zh-CN,en-US;q=0.8");
            connection.addRequestProperty("Sec-Fetch-Dest","empty");
            connection.addRequestProperty("Sec-Fetch-Mode","cors");
            connection.addRequestProperty("Sec-Fetch-Site","same-site");
            connection.addRequestProperty("Cookie", Cookies);

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            byte[] data=string.getBytes();
            connection.getOutputStream().write(data);
            int code=connection.getResponseCode();
            if(code==200){
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream message =new ByteArrayOutputStream();
                int lenght=0;
                byte buffer[] =new byte[1024];
                while((lenght=is.read(buffer))!=-1) {
                    message.write(buffer,0,lenght);
                }
                is.close();
                message.close();
                String out=new String(message.toByteArray(),"UTF-8");

                return out;
            }else{
                System.out.println(code);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "null";
    }
    public static String Regex(String regex,String str) {
        String string="";
        Matcher mat= Pattern.compile(regex).matcher(str);
        while(mat.find()) {
            string=mat.group(1);
        }
        return string;
    }
    public static String GET(String url, String cookies){
        String out="";
        try {
            URL url2=new URL(url);
            HttpURLConnection connection=(HttpURLConnection)url2.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Cookie", cookies);

            connection.addRequestProperty("Connection", "keep-alive");
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0");
            connection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
            connection.addRequestProperty("Upgrade-Insecure-Requests", "1");
            connection.addRequestProperty("Cache-Control", "max-age=0");

            connection.connect();

            InputStream is = connection.getInputStream();
            ByteArrayOutputStream message =new ByteArrayOutputStream();
            int lenght=0;
            byte buffer[] =new byte[1024];
            while((lenght=is.read(buffer))!=-1) {
                message.write(buffer,0,lenght);
            }
            is.close();
            message.close();
            out=new String(message.toByteArray());

        }catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }
}
