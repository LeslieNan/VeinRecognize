package com.example.yourstory.jxd1500.util;

import java.io.DataOutputStream;

/**
 * Created by lenovo on 2019/3/25.
 * 获取usb权限
 */

public class GetUSBPermission {


    /**
     * 执行shell adb语句
     */
    public GetUSBPermission() {
        String[] commands = new String[] {"cd /dev/bus",
                "chmod -R 777 usb" };
        Process process = null;
        DataOutputStream dataOutputStream = null;
        try {

            process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            int length = commands.length;
            for (int i = 0; i < length; i++) {
                dataOutputStream.writeBytes(commands[i] + "\n");
            }
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
        } catch (Exception e) {

        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
    }
}
