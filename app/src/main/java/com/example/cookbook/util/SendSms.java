package com.example.cookbook.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import java.util.Random;

public class SendSms {

    static final String accessKeyId = "LTAI4FhosnogGWBe19yLt2vq";
    static final String accessKeySecret = "2idxCvXi30rOikECjiBIEYnlAW5a2u";

    static private String verificode;

    public static void sendCode(String phonenumber) {


        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);




//生成随机验证码
        String string=generNumCode(6);
        int code =Integer.parseInt(string);
        verificode = string;

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers",phonenumber);
        request.putQueryParameter("SignName","cookbook");
        request.putQueryParameter("TemplateCode", "SMS_176535369");
        request.putQueryParameter("TemplateParam","{\"code\":\""+code+"\"}");

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());

        } catch (ServerException e) {
            e.printStackTrace();

        } catch (ClientException e) {
            e.printStackTrace();

        }
    }

    public static  String getcode( ){
        //verificode = "0";

        return verificode;

    }
    /**
     *获取随机验证码
     * @param len
     * @return
     */
    public static String generNumCode(int len){
        //实例化 StringBuffer ,用作拼接验证码
        //博主会在这篇博文发后不久，会更新一篇String与StringBuilder开发时的抉择的博文。
        StringBuffer code = new StringBuffer();
        //拼接一个不为0的数字
        code.append(getRandom());
        //长度减1，随机拼接数字
        for (int i = 0; i < len-1; i++) {
            code.append(new Random().nextInt(10));
        }
        //利用递归算法，如果随机数长度不够则重新随机
        if(code.length() != 6){
            return generNumCode(6);
        }else {
            return code.toString();
        }
    }

    /**
     * 使用递归算法，获取第一个随机数不为0
     * @return int
     */
    public static int getRandom(){
        int number = new Random().nextInt(10);
        if(0 == number){
            return getRandom();
        }
        return number;
    }
}
