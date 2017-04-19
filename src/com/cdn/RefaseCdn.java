package com.cdn;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SimpleTimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import sun.misc.BASE64Encoder;
import utils.HttpClient4Utils;

public class RefaseCdn {
	private static HttpClient httpClient = HttpClient4Utils.createHttpClient(100, 20, 10000, 1000, 1000);
	private final static String API_URL = "https://cdn.aliyuncs.com";
	private static final String cdn_server_address = "https://cdn.aliyuncs.com";
	
	/**
     * 刷新缓存
     * 
     * @param objectPath
     * @return
     */
    public static String refreshObjectCaches(String objectPath) {
        // Map<String, String> param = new TreeMap<String, String>();
        // param.put("Action", "RefreshObjectCaches");
        // param.put("ObjectPath",
        // "http://b1.goimg.cn/test/images/1471590829296.png");
        // param.put("ObjectType", "File");
    	RefaseCdn cdn = new RefaseCdn();
        Map<String, String> param = new TreeMap<String, String>();
        param.put("Action", "RefreshObjectCaches");
        param.put("ObjectPath", objectPath);
        param.put("ObjectType", "File");
        String url = cdn.compose_url(param);
        System.out.println("Aliyun CDN RefreshObjectCaches Request Url:" + url);
        HttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, Charset.forName("UTF-8"));
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 组合请求路径
     * 
     * @param user_params
     * @return
     */
    private String compose_url(Map<String, String> user_params) {
        // 请求的时间戳。日期格式按照ISO8601标准表示，并需要使用UTC时间。格式为：YYYY-MM-DDThh:mm:ssZ。
        // 例如，2014-11-11T12:00:00Z（为北京时间2014年11月11日20点0分0秒）
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'hh:mm:ss'Z'");
        sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
        String timestamp = sdf.format(new Date());
        Map<String, String> parameters = new TreeMap<String, String>();
        
      //公共参数
        parameters.put("Format", "JSON");
        parameters.put("Version", "2014-11-11");
        parameters.put("AccessKeyId", "03mykSxCa8m0q4CE");
        parameters.put("SignatureVersion", "1.0");
        parameters.put("SignatureMethod", "HMAC-SHA1");
        parameters.put("SignatureNonce", String.valueOf(System.currentTimeMillis()));
        parameters.put("TimeStamp", timestamp);
        // 添加用户请求参数到签名map中
        Iterator<Entry<String, String>> iterator = user_params.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            parameters.put(entry.getKey(), entry.getValue());
        }
        String signature = compute_signature(parameters,"gjIUUzBaA990kqPFD3oPeDqoZqW95v");
        parameters.put("Signature", signature);
        return cdn_server_address + "/?" + map_to_url(parameters);
    }

    /**
     * 计算签名
     * 
     * @param parameters
     * @param access_key_secret
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    private String compute_signature(Map<String, String> parameters, String access_key_secret) {
        try {
            StringBuilder canonicalizedQueryString = new StringBuilder();
            // 调用Map进行字典排序
            parameters = sortMap(parameters);
            // 一、按照参数名称的字典顺序对请求中所有的请求参数（包括文档中描述的“公共请求参数”和给定了的请求接口的自定义参数，但不能包括“公共请求参数”中提到Signature参数本身）进行排序。
            // 遍历组合组合签名
            Iterator<Entry<String, String>> iterator = parameters.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> entry = iterator.next();
                // 对Map键值对进行URL编码
                // 二、对每个请求参数的名称和值进行编码。名称和值要使用UTF-8字符集进行URL编码，URL编码的编码规则是：
                // (1)对于字符 A-Z、a-z、0-9以及字符“-”、“_”、“.”、“~”不编码;
                // (2)对于其他字符编码成“%XY”的格式，其中XY是字符对应ASCII码的16进制表示。比如英文的双引号（”）对应的编码就是%22
                // (3)对于扩展的UTF-8字符，编码成“%XY%ZA…”的格式；
                // (4)需要说明的是英文空格（ ）要被编码是%20，而不是加号（+）。
                canonicalizedQueryString.append("&").append(percent_encode(entry.getKey())).append("=")
                        .append(percent_encode(entry.getValue()));
            }
            // 三、调用percent_encode方法把编码后的字符串中加号（+）替换成%20、星号（*）替换成%2A、%7E替换回波浪号（~）
            String stringToSign = "GET&%2F&" + percent_encode(canonicalizedQueryString.toString().substring(1).trim());
            // 按照RFC2104的定义，使用上面的用于签名的字符串计算签名HMAC值。注意：计算签名时使用的Key就是用户持有的Access
            // Key Secret并加上一个“&”字符(ASCII:38)，使用的哈希算法是SHA1
            String key = "gjIUUzBaA990kqPFD3oPeDqoZqW95v" + "&";
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
            byte[] sign = mac.doFinal(stringToSign.getBytes());
            System.out.println("stringToSign:" + stringToSign);
            // 得到签名
            String signature = (new BASE64Encoder()).encode(sign);
            System.out.println("signature:" + signature);
            return percent_encode(signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * map转为请求URL
     * 
     * @param parameters
     * @return
     */
    private String map_to_url(Map<String, String> parameters) {
        try {
            parameters = sortMap(parameters);
            StringBuilder url = new StringBuilder();
            Iterator<Entry<String, String>> iterator = parameters.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> entry = iterator.next();
                url.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
            return url.toString().substring(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换特殊字符
     * 
     * @param param
     * @return
     */
    private String percent_encode(String param) {
        try {
            // 名称和值要使用UTF-8字符集进行URL编码
            param = URLEncoder.encode(param, "UTF-8");
            param = param.replaceAll("\\+", "%20");
            param = param.replaceAll("\\*", "%2A");
            param = param.replaceAll("%7E", "~");
            return param;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Map排序
     * 
     * @param parameters
     * @return
     */
    private Map<String, String> sortMap(Map<String, String> parameters) {
        if (parameters.isEmpty()) {
            return null;
        }
        // 按字典排序
        Map<String, String> sortMap = new TreeMap<String, String>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // 指定排序器按照降序排列
                // return o2.compareTo(o1);
                // 指定排序器按照升序序排列
                return o1.compareTo(o2);
            }
        });
        sortMap.putAll(parameters);
        return sortMap;
    }
	public static void main(String[] args) {
/*		Map<String, String> params = new HashMap<String, String>();
		Random ra =new Random();
//		String url="https://cdn.aliyuncs.com?&Action=RefreshObjectCaches&ObjectPath=app.sodasoccer.com.cn/sodaPort/api/v5/module_all";
//		params.put("ObjectPath", "https://app.sodasoccer.com.cn/sodaPort/api/v5/module_all");
		SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-DDThh:mm:ssZ");
		
		
		//私有参数
		params.put("ObjectPath", "https://app.sodasoccer.com.cn/picture/titleImages/20161212083812.jpg");
		params.put("Action", "RefreshObjectCaches");
		
		 String response = HttpClient4Utils.sendPost(httpClient, API_URL, params, Consts.UTF_8);
	        System.out.println(response);
	}
	*/
	
	 RefaseCdn a = new RefaseCdn();
     Map<String, String> param = new TreeMap<String, String>();
     param.put("Action", "RefreshObjectCaches");
     param.put("ObjectPath", "http://app.sodasoccer.com.cn/sodaPort/api/v2/team_listHot?currentTime=2016-08-09");
     param.put("ObjectType", "File");
     String url = a.compose_url(param);
     System.out.println("request url:" + url);
     HttpClient client = HttpClients.createDefault();
     HttpGet httpgets = new HttpGet(url);
     try {
         HttpResponse response = client.execute(httpgets);
         HttpEntity entity = response.getEntity();
         System.out.println(EntityUtils.toString(entity, Charset.forName("UTF-8")));
     } catch (ClientProtocolException e) {
         e.printStackTrace();
     } catch (IOException e) {
         e.printStackTrace();
     }
    
 }

}
