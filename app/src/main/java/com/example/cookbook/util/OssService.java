package com.example.cookbook.util;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.alibaba.sdk.android.oss.*;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

public class OssService {
    private OSS oss;
    private String accessKeyId="LTAI4FhosnogGWBe19yLt2vq";
    private String bucketName="qiandaobao";
    private String accessKeySecret="2idxCvXi30rOikECjiBIEYnlAW5a2u";
    private String endpoint="oss-cn-shanghai.aliyuncs.com";
    private Context context;

    public OssService(Context context){
        this.context=context;
    }

    public void initOSSClient(){
        OSSCredentialProvider credentialProvider=new OSSPlainTextAKSKCredentialProvider(accessKeyId,accessKeySecret);
        ClientConfiguration conf=new ClientConfiguration();
        conf.setConnectionTimeout(15*1000);
        conf.setSocketTimeout(15*1000);
        conf.setMaxConcurrentRequest(8);
        conf.setMaxErrorRetry(2);
        oss=new OSSClient(context,endpoint,credentialProvider,conf);
    }

    public int beginupload(final Context context, String filename, String path){
        String objectname=filename;
        if(objectname==null||objectname.equals("")){
            return 0;
        }
        PutObjectRequest put=new PutObjectRequest(bucketName,objectname,path);
        if(path==null||path.equals("")){
            return 1;
        }
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {

            }
        });
        Log.d("test123456","上传中");
//        try{
//            PutObjectResult putResult=oss.putObject(put);
//            return 2;
//        }catch (ClientException e){
//            e.printStackTrace();
//            return 3;
//        }catch (ServiceException e){
//            e.printStackTrace();
//            return 4;
//        }
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("oss", "success");
                Looper.prepare();
                Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                Looper.prepare();
                Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
                if (clientException != null) {
                    clientException.printStackTrace();
                }
                if (serviceException != null) {
                    serviceException.printStackTrace();
                }
            }
        });
        return 0;
    }
}
