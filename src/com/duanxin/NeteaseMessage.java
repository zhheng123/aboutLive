package com.duanxin;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SimpleTimeZone;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class NeteaseMessage {
	private static final String cdn_server_address = "https://api.netease.im/sms/sendcode.action";
	public static void main(String[] args) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/sms/sendcode.action";//发送验证码
//		String url="https://api.netease.im/sms/verifycode.action";//验证验证码
        HttpPost httpPost = new HttpPost(url);

//        String appKey = "0b22a1cd2490bb5c711e628f57498a7d";
//        String appSecret = "271ea66c2581";
        String appKey = "854337e8e3815b394c763252d5508b95";
        String appSecret = "2ec9edde564a";
        String nonce =  "12345";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("mobile", "15201163385"));
        nvps.add(new BasicNameValuePair("code", "987641"));
        nvps.add(new BasicNameValuePair("codeLen", "6"));
        nvps.add(new BasicNameValuePair("templateid","3033736"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
        // 打印执行结果
        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
        System.out.println(new Date());
        
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
        return cdn_server_address + "/?" + map_to_url(parameters);
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
    // 计算并获取CheckSum
    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }

    // 计算并获取md5值
    public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}
