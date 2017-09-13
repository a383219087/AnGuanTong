package com.cqj.test.wbd2_gwpy.myinterface;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/23.
 */
public interface IYhdjImageCallBack {
    ArrayList<String> getImageDatas();

    void takePhoto();

    void success();

    void releaseCamera();

    void setCameraCallback();

}
