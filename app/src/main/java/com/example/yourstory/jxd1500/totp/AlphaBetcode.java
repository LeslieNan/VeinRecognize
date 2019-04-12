package com.example.yourstory.jxd1500.totp;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

/**
 * Created by lenovo on 2019/3/20.
 */

public class AlphaBetcode {

    public static void example() {
        String data = "test encode";
        String encode = Encode(data);        // 字母编码
        String decode = Decode(encode);        // 字母解码
        boolean b = data.equals(decode);
        boolean b2 = b;
    }

    // / <summary>
    // / 转码data为全字母串，并添加前缀
    // / </summary>
    public static String Encode(String data) {
        String str = data;
        if (!data.startsWith("ALPHABETCODE")) {
            str = "ALPHABETCODE" + EncodeAlphabet(data);
        }
        return str;
    }

    // / <summary>
    // / 解析字母串为原有串
    // / </summary>
    public static String Decode(String data) {
        String str = data;
        if (data.startsWith("\"ALPHABETCODE@") && data.endsWith("\""))
            data = data.substring(1, data.length() - 1);
        if (data.startsWith("ALPHABETCODE@")) {
            str = DecodeAlphabet(data.substring("ALPHABETCODE@".length()));
        }
        return str;
    }

    // / <summary>
    // / 获取文件对应的编码字符串
    // / </summary>
    public static String getFileData(String file) {
        byte[] bytes = File2Bytes(file);
        String data = ToStr(bytes);

        return data;
    }

    /**
     * 将文件转换为byte数组
     */
    public static byte[] File2Bytes(String path) {
        byte[] bytes = {};

        try {
            FileInputStream in = new FileInputStream(path);

            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int count = 0;
            while ((count = in.read(data, 0, 1024)) > 0) {
                byteOutStream.write(data, 0, count);
            }

            in.close();
            bytes = byteOutStream.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    // / <summary>
    // / 获取文件中的数据,自动判定编码格式
    // / </summary>
    private static String fileToString(String filePath) {
        byte[] bytes = File2Bytes(filePath);
        String str = new String(bytes);

        return str;
    }

    // # region 字符串字母编码逻辑

    // / <summary>
    // / 转化为字母字符串
    // / </summary>
    public static String EncodeAlphabet(String data) {
        byte[] B = data.getBytes();
        return ToStr(B);
    }

    // / <summary>
    // / 每个字节转化为两个字母
    // / </summary>
    private static String ToStr(byte[] B) {
        StringBuilder Str = new StringBuilder();
        for (byte b : B) {
            Str.append(ToStr(b));
        }
        return Str.toString();
    }

    private static String ToStr(byte b) {
        return "" + ToChar(b / 16) + ToChar(b % 16);
    }

    private static char ToChar(int n) {
        return (char) ('a' + n);
    }

    // / <summary>
    // / 解析字母字符串
    // / </summary>
    public static String DecodeAlphabet(String data) {
        byte[] B = ToBytes(data);
        return new String(B);
    }

    // / <summary>
    // / 解析字符串为Bytes数组
    // / </summary>
    public static byte[] ToBytes(String data) {
        char[] D = data.toCharArray();
        byte[] B = new byte[D.length / 2];

        for (int i = 0; i < D.length; i += 2) {
            char ci0 = data.charAt(i);
            char ci1 = data.charAt(i + 1);

            byte b = ToByte(ci0, ci1);
            B[i / 2] = b;
        }

        return B;
    }

    // / <summary>
    // / 每两个字母还原为一个字节
    // / </summary>
    private static byte ToByte(char a1, char a2) {
        return (byte) ((a1 - 'a') * 16 + (a2 - 'a'));
    }

}
