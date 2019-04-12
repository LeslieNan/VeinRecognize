package com.example.yourstory.jxd1500.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.yourstory.jxd1500.bean.FingerBuf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;

import static com.example.yourstory.jxd1500.constant.UserInfoConstant.PASSWORD_LENGTH;

/**
 * Created by lenovo on 2019/3/21.
 */

public class CodeUtil {

    /**
     * 保存图片到sd卡
     * @param bitmap
     */
    public void savePicture(Bitmap bitmap)
    {
        String pictureName = "/mnt/sdcard/" + "car"+".jpg";
        File file = new File(pictureName);
        FileOutputStream out;
        try
        {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 将字节数组转换为ImageView可调用的Bitmap对象
     * @param bytes
     * @param opts
     * @return Bitmap
     */
    public static Bitmap getPicFromBytes(byte[] bytes,
                                         BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    public static Bitmap byteToBitmap(byte[] imgByte) {
        InputStream input = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        input = new ByteArrayInputStream(imgByte);
        SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
                input, null, options));
        bitmap = (Bitmap) softRef.get();
        if (imgByte != null) {
            imgByte = null;
        }
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 将字符串转变为字节数组,不更改字节数组的长度
     * 将userID转变为veinID
     * @param str
     * @param length
     * @return
     */
    public static byte[] strToBytes(String str,int length){
        byte[] bytes=new byte[length];
        byte[] strByte=str.getBytes();
        for (int i=0;i<strByte.length;i++){
            bytes[i]=strByte[i];
        }
        return bytes;
    }

    public static String bytesToStr(byte[] bytes){
        int length=0;
        for (int i=0;i<bytes.length;i++){
            if (bytes[i]=='\0'){
                length=i;
                break;
            }
        }
        try {
            return new String(bytes,0,length,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String findUserFromVeinID(byte[] bytes){
        try {
            return new String(bytes,0,PASSWORD_LENGTH,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isArrayNull(FingerBuf[] fingerBufs){
        for (int i=0;i<fingerBufs.length;i++){
            if (fingerBufs[i]!=null){
                return false;
            }
        }
        return true;
    }



    public static byte[] base64StrToByte(String str){
        return Base64.decode(str,Base64.DEFAULT);
    }

    public static String base64ByteToStr(byte[] bytes){
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }



}
