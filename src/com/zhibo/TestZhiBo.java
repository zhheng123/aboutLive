package com.zhibo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import sun.misc.BASE64Encoder;

public class TestZhiBo {
	private static final String cdn_server_address = "https://api.netease.im/sms/sendcode.action";
	public static void main(String[] args) throws Exception {
	
		//注册用户
//		registUser();
		//创建直播室
		createLive();
		//查询直播室
//		queryLive();
		//更新直播室
//		editLive();
		
//		toTime("1492306200000 ");
	}
	
	
	//创建直播室
	public static void createLive() throws Exception{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		String url = "http://123.56.40.190:8081/video/vedio/mobLive_crtMobLive.htm";
//		String url = "https://video.sodasoccer.com.cn/video/vedio/mobLive_crtMobLive.htm";
		HttpPost httpPost = new HttpPost(url);
		// 设置请求的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("live.liveName", "soda宝贝直播"));
		nvps.add(new BasicNameValuePair("liveDesc", "中关村智云杯春季联赛，小组赛第5轮搜达足球vs千方集团"));
		nvps.add(new BasicNameValuePair("live.imgUrl", GetImageStr()));
		nvps.add(new BasicNameValuePair("live.chatRoomOwnerId", "8513"));
		nvps.add(new BasicNameValuePair("live.liveStatus", "2"));
		nvps.add(new BasicNameValuePair("live.liveIdentify", "18159"));//新闻ID
		nvps.add(new BasicNameValuePair("live.liveStartTime", getTime()));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
		// 执行请求
		HttpResponse response = httpClient.execute(httpPost);
		// 打印执行结果
		System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
	}
	public static String  getTime() throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date=new Date();
		date=sdf.parse("2017-04-19 14:00");
		return String.valueOf(date.getTime());
	}
	//查询直播室
	public static void queryLive() throws Exception{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		String url = "https://video.sodasoccer.com.cn/video/vedio/mobLive_qryDetailById.htm";
		HttpPost httpPost = new HttpPost(url);
		// 设置请求的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("id", "6"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
		// 执行请求
		HttpResponse response = httpClient.execute(httpPost);
		// 打印执行结果
		System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
	}
	//更新直播
	public static void editLive() throws Exception{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		String url = "https://video.sodasoccer.com.cn/video/vedio/mobLive_updateLiveStatus.htm";//更新直播的状态
//		String url = "https://video.sodasoccer.com.cn/video/vedio/mobLive_updateliveRoomOwner.htm";//更换主播链接
//		String url = "https://video.sodasoccer.com.cn/video/vedio/mobLive_updateLiveImg.htm";//更换直播图片
		HttpPost httpPost = new HttpPost(url);
		// 设置请求的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		/*//		更换主播
		nvps.add(new BasicNameValuePair("id", "277"));
		nvps.add(new BasicNameValuePair("live.chatRoomOwnerId", "4325"));
		*/
		
		
		 // 更新直播时间
		nvps.add(new BasicNameValuePair("id", "286"));
		nvps.add(new BasicNameValuePair("live.liveStatus", "2"));
		nvps.add(new BasicNameValuePair("t",getTime()));
		
		/*//更换直播图片
		nvps.add(new BasicNameValuePair("id", "283"));
		nvps.add(new BasicNameValuePair("live.imgUrl", GetImageStr()));
 		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));*/
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
		// 执行请求
		HttpResponse response = httpClient.execute(httpPost);
		// 打印执行结果
		System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
	}
	
	//注册用户
	public static void registUser() throws Exception{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		String url = "http://123.56.40.190:8081/video/vedio/mobLiveChat_addUser.htm";
//		String url = "https://video.sodasoccer.com.cn/video/vedio/mobLiveChat_addUser.htm";
		HttpPost httpPost = new HttpPost(url);
		// 设置请求的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", "8513"));
		nvps.add(new BasicNameValuePair("nickname", "带泳圈的鱼"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
		// 执行请求
		HttpResponse response = httpClient.execute(httpPost);
		// 打印执行结果
		System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
		
	}
	public static void toTime(String s)throws ParseException{
	        String res;
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date date = simpleDateFormat.parse(s);
	        long ts = date.getTime();
	        res = String.valueOf(ts);
	        System.out.println(res);
	}
	//图片转化成base64字符串
    public static String GetImageStr()
    {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String imgFile = "d://live//c.jpg";//待处理的图片
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try 
        {
            in = new FileInputStream(imgFile);        
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//返回Base64编码过的字节数组字符串
    }
}
