package com.chiahao.callbacknet.net;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.chiahao.callbacknet.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络加载框架
 *      整体的CloudEngine采用单例的模式来做
 *      回调的方式来实现
 * Created by chiahao on 2015/11/22.
 */
public class Engine {
    private static Engine sInstance;
    private Event event;
    private Context context;

    private Engine(Context context){
        this.context = context;
    }
    public static Engine getInstance(Context context){
        if (sInstance == null) {
            if (sInstance == null) {
                synchronized (Engine.class){
                    sInstance = new Engine(context);
                }
            }
        }
        return sInstance;
    }

    /**
     * 使用三方框架
     * @param url 接口地址
     * @param requestCode 请求码
     * @param callback 回调接口对象
     */
    public void getServerDataXutils(String url, final int requestCode, final IDataCallback callback){
        event = new Event();
        event.requestCode = requestCode;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                event.data = responseInfo.result;// data里面存储的是服务器返回的json字符串
                // 回调
                // 这里调用这个方法相当于MainActivity调用的
                // 因为传进来的IDataCallback对象就是MainActivity
                callback.handleServerResult(event.requestCode, event.errCode, event.data);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(context,"访问网络失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 使用AsyncTask
     * @param url 接口地址
     * @param requestCode 请求码
     * @param callback 回调接口对象
     */
    public void getServerDataAsyncTask(String url,int requestCode, IDataCallback callback){
        new MyAsyncTask(url,requestCode,callback).execute();
    }

    /**
     * AsyncTask，里面使用了原生网络访问
     */
    class MyAsyncTask extends AsyncTask<Void,Void,Void>{
        private String url;
        private int requestCode;
        private IDataCallback callback;
        private Event event;
        public MyAsyncTask(String url,int requestCode, IDataCallback callback) {
            this.url = url;
            this.callback = callback;
            event = new Event();
            event.requestCode = requestCode;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL urll = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) urll.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(2000);
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    InputStream inputStream = conn.getInputStream();
                    String result = StreamUtils.readStream(inputStream);
                    event.data = result;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // 回调
            callback.handleServerResult(event.requestCode,event.errCode,event.data);
        }
    }
}
