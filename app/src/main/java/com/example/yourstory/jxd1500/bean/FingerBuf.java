package com.example.yourstory.jxd1500.bean;

import static jx.vein.javajar.JXFVJavaInterface.veinSampleSize;

/**
 * Created by lenovo on 2019/3/24.
 */

public class FingerBuf {

    public byte[] feat_buf1 = new byte[veinSampleSize];  //指静脉样本
    public byte[] feat_buf2 = new byte[veinSampleSize];  //指静脉样本
    public byte[] feat_buf3 = new byte[veinSampleSize];  //指静脉样本

    //用于在修改用户信息时判断此手指静脉是否是占位用的，
    public boolean isReal = true;
}
