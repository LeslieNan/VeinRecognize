package com.example.yourstory.jxd1500.db;

import android.util.Log;

import com.example.yourstory.jxd1500.MyApplication;
import com.example.yourstory.jxd1500.bean.VeinBean;
import com.example.yourstory.jxd1500.util.CodeUtil;

import java.util.List;

import jx.vein.javajar.JXFVJavaInterface;

import static jx.vein.javajar.JXFVJavaInterface.groupIDLength;
import static jx.vein.javajar.JXFVJavaInterface.veinIDLength;

/**
 * Created by lenovo on 2019/3/27.
 */

public class VeinDBDao {

    private static final String TAG = "VeinDBDao";

    private JXFVJavaInterface jxfvJavaInterface;        //设备API接口
    private long devHandler;                            //设备的唯一标识符
    private long dbHandler;                             //数据库连接句柄

    public VeinDBDao() {
        jxfvJavaInterface = MyApplication.getjxfvJavaInterface();
        devHandler = MyApplication.getDvHandler();
        dbHandler = MyApplication.getDbHandler();
    }

    /**
     * 删除同一id下的三个可能有的指静脉
     *
     * @param veinID
     * @param groupID
     * @return
     */
    public int deleteVeinByID(final String veinID, final String groupID) {
        int veinNumber = 0;
        byte[] groupIDbyte = CodeUtil.strToBytes(groupID, groupIDLength);
        //删除一个用户的三个指静脉样本
        byte[] veinIDbyte = CodeUtil.strToBytes(veinID, veinIDLength);
        try {
            if (jxfvJavaInterface.jxRemoveVeinFeature(dbHandler, veinIDbyte, groupIDbyte) == 0) {
                //删除成功
                veinNumber++;
                Log.d(TAG, "设备建的数据库删除指静脉成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return veinNumber;
    }


    /**
     * 删除同一id下的三个可能有的指静脉
     *
     * @param veinID
     * @param groupID
     * @return
     */
    public int deleteThreeVeinByID(final String veinID, final String groupID) {
        int veinNumber = 0;
        byte[] groupIDbyte = CodeUtil.strToBytes(groupID, groupIDLength);
        //删除一个用户的三个指静脉样本
        for (int i = 0; i < 3; i++) {
            byte[] veinIDbyte = CodeUtil.strToBytes(veinID + i, veinIDLength);
            try {
                if (jxfvJavaInterface.jxRemoveVeinFeature(dbHandler, veinIDbyte, groupIDbyte) == 0) {
                    //删除成功
                    veinNumber++;
                    Log.d(TAG, "设备建的数据库删除指静脉成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return veinNumber;

    }

    /**
     * 查询指静脉数据库中的样本数
     */
    public int selectCountVein() {
        return jxfvJavaInterface.jxCountAllFeatures(dbHandler);
    }


    public int changeGroup(String veinID, String groupID) {
        try {
            return jxfvJavaInterface.jxChangeVeinGroup(dbHandler,
                    CodeUtil.strToBytes(veinID, veinIDLength),
                    CodeUtil.strToBytes(groupID, groupIDLength));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2;
    }


    public int insertVein(List<VeinBean> veins) {
        int isSuccessful = 0;
        for (VeinBean veinBean : veins) {
            try {
                if (jxfvJavaInterface.jxAddThreeVeinFeature(dbHandler,
                        CodeUtil.base64StrToByte(veinBean.veinbuf1),
                        CodeUtil.base64StrToByte(veinBean.veinbuf2),
                        CodeUtil.base64StrToByte(veinBean.veinbuf3),
                        CodeUtil.base64StrToByte(veinBean.veinID),
                        CodeUtil.base64StrToByte(veinBean.groupID)) == 0) {
                    isSuccessful = 1;
                } else {
                    isSuccessful = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isSuccessful = 0;
            }
        }
        return isSuccessful;
    }

}
