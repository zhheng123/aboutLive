package com.duanxin;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class ShortMessage {

	public static void main(String[] args) throws ApiException {

//		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest","23583517","c8ab2d77de1fc8cceedd969742612247");
//		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
//		req.setExtend("123456");
//		req.setSmsType("normal");
////		req.setSmsParam("{name:1234}");
//		req.setSmsFreeSignName("搜达足球");
//		req.setSmsParamString("{\"name\":\"1234\"}");
//		req.setRecNum("15010288239");
//		req.setSmsTemplateCode("SMS_36325137");
//		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
//		System.out.println(rsp.getBody());
		//自己测试
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest","23573622","b0171e3fb2ee63e62c2ccabd79514959");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("123456");
		req.setSmsType("normal");
//		req.setSmsParam("{name:1234}");
		req.setSmsFreeSignName("测试1");
		req.setSmsParamString("{\"name\":\"1234\"}");
		req.setRecNum("15010288239");
		req.setSmsTemplateCode("SMS_34610400");
		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
		System.out.println(rsp.getBody());

	}

}
