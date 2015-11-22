package com.chiahao.callbacknet.net;

/**
 *  封装和网络相关的对象
 *      requestCode     请求码
 *      errCode    响应码
 *      data    服务器返回的数据
 * Created by chiahao on 2015/11/22.
 */
public class Event {
    public int requestCode;
    public int errCode;
    public Object data;

    @Override
    public String toString() {
        return "Event{" +
                "requestCode=" + requestCode +
                ", errCode=" + errCode +
                ", data=" + data +
                '}';
    }
}
