package com.cdn;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cdn.model.v20141111.RefreshObjectCachesRequest;
import com.aliyuncs.cdn.model.v20141111.RefreshObjectCachesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
public class AliyunCdnUtlis {
	 //阿里云管理员账号给你生成的key和value
    private final static String ACCESS_KEY_ID = "03mykSxCa8m0q4CE";

    private final static String ACCESS_KEY_VALUE = "gjIUUzBaA990kqPFD3oPeDqoZqW95v";

    private final static String OBJECT_TYPE_FILE = "File";

    private final static String OBJECT_TYPE_DIRECTORY = "Directory";

    /**
     * 刷新一个文件
     * @param url
     * @return
     */
    public static RefreshObjectCachesResponse reflushFileCDN(String url) {
        RefreshObjectCachesRequest describe = new RefreshObjectCachesRequest();
        describe.setObjectPath(url);
        describe.setObjectType(OBJECT_TYPE_FILE);
        return reflush(describe);
    }

    /**
     * 刷新文件夹
     * @param packageUrl
     * @return
     */
    public static RefreshObjectCachesResponse reflushPackageCDN(String packageUrl) {
        RefreshObjectCachesRequest describe = new RefreshObjectCachesRequest();
        describe.setObjectPath(packageUrl);
        describe.setObjectType(OBJECT_TYPE_DIRECTORY);
        return reflush(describe);
    }

    private static RefreshObjectCachesResponse reflush(RefreshObjectCachesRequest describe){
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_VALUE);
        IAcsClient client = new DefaultAcsClient(profile);
        try {
            RefreshObjectCachesResponse response
                    = client.getAcsResponse(describe);
            return response;
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args){
    	RefreshObjectCachesResponse re=reflushFileCDN("https://app.sodasoccer.com.cn/sodaPort/api/v5/module_all?moduleId=1");
    	System.out.println(re);
    }
}
