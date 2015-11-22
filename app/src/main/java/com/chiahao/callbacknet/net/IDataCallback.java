package com.chiahao.callbacknet.net;

/**
 * Created by chiahao on 2015/11/22.
 */
public interface IDataCallback {
    void handleServerResult(int requestCode,int errCode,Object data);
}
