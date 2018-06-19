package com.sbolo.syk.common.tools;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 
 * @author lihaopeng
 * @date 2014�?0�?�? * @version 1.0.0
 * @description desc
 */
public class MD5Utils {

    /**
     * 生成有效签名
     * 
     * @param params
     * @param secret
     * @return
     */
    public static String signature(String orgin) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = byte2hex(md.digest(orgin.toString().getBytes("utf-8")));
        } catch (Exception e) {
            throw new java.lang.RuntimeException("sign error !");
        }
        return result;
    }

    /**
     * 生成有效签名
     * @param args
     * @return
     */
    public static String signature(String... args) {
        String arr = "";
        for (String arg : args) {
            if (arg != null) {
                arr += arg;
            }
        }
        return signature(arr);
    }

    /**
     * 二行制转字符�?     * 
     * @param b
     * @return
     */
    private static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * 获取单个文件的MD5值！ 
     * @param filePath
     * @return
     */
    public static String getFileMD5(String filePath) {
        return getFileMD5(new File(filePath));
    }

    /**
     * 获取单个文件的MD5值！
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {

        if (!file.isFile()) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            FileInputStream in = new FileInputStream(file);
            byte buffer[] = new byte[1024];
            int len;
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Sign file error!");
        }
    }

    /**
     * 获取单个文件的MD5值！
     * @param fileBytes
     * @return
     */
    public static String getFileMD5(byte[] fileBytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(fileBytes, 0, fileBytes.length);
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Sign file error!");
        }
    }
}
