package com.comment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.http.Consts;
import org.apache.http.client.HttpClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import utils.HttpClient4Utils;
import utils.SignatureUtils;

public class TestComment {
	 /** 产品密钥ID，产品标识 */
    private final static String SECRETID = "fcb0c0927dfa56e8459af67f34a23b93";
    /** 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露 */
    private final static String SECRETKEY = "7ddbd0755e82457bf82692d5ed4b38bd";
    /** 业务ID，易盾根据产品业务特点分配 */
    private final static String BUSINESSID = "81d128debb28a3bd1914cc3f55b1dcfc";
    /** 易盾反垃圾云服务图片离线检测结果获取接口地址 */
    private final static String API_URL = "https://api.aq.163.com/v2/text/check";
    /** 实例化HttpClient，发送http请求使用，可根据需要自行调参 */
    private static HttpClient httpClient = HttpClient4Utils.createHttpClient(100, 20, 10000, 1000, 1000);
    /**
     * 
     * @param args
     * @throws Exceptiondddddddd
     */
    public static void main(String[] args) throws Exception {
    	 Map<String, String> params = new HashMap<String, String>();
    	// 1.设置公共参数
        params.put("secretId", SECRETID);
        params.put("businessId", BUSINESSID);
        params.put("version", "v2");
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("nonce", String.valueOf(new Random().nextInt()));

        // 2.设置私有参数
        params.put("dataId", "ebfcad1c-dba1-490c-b4de-e784c2691768");
        params.put("content", "竞彩 足球 篮球 交 流 群，5962 377团队 推荐 分析就 是不一样 命中率 90 %的，入群 验证8 8 8，美 女 稳单 竞彩 的微信：X376 595 匆匆那年");
        params.put("dataOpType", "1");
        // 3.生成签名信息
        String signature = SignatureUtils.genSignature(SECRETKEY, params);
        params.put("signature", signature);

        // 4.发送HTTP请求，这里使用的是HttpClient工具包，产品可自行选择自己熟悉的工具包发送请求
        String response = HttpClient4Utils.sendPost(httpClient, API_URL, params, Consts.UTF_8);
        System.out.println(response);
        // 5.解析接口返回值
        JsonObject jObject = new JsonParser().parse(response).getAsJsonObject();
        int code = jObject.get("code").getAsInt();
        String msg = jObject.get("msg").getAsString();
        if (code == 200) {
            JsonObject resultObject = jObject.getAsJsonObject("result");
            int action = resultObject.get("action").getAsInt();
            if (action == 1) {
                System.out.println("正常内容，通过");
            } else if (action == 2) {
                System.out.println("垃圾内容，删除");
            } else if (action == 3) {
                System.out.println("嫌疑内容");
            }
        } else {
            System.out.println(String.format("ERROR: code=%s, msg=%s", code, msg));
        }

    }
}
