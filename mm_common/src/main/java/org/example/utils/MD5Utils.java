package org.example.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    // 加密方法：接收一个字符串明文，返回使用 MD5 加密后的哈希值
    public static String encrypt(String plaintext) throws NoSuchAlgorithmException {
        // 使用 MD5 算法创建 MessageDigest 对象
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 更新 MessageDigest 对象中的字节数据
        md.update(plaintext.getBytes());
        // 对更新后的数据计算哈希值，存储在 byte 数组中
        byte[] digest = md.digest();
        // 将 byte 数组转换为十六进制字符串
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        // 返回十六进制字符串
        return sb.toString();
    }

    // 解密方法：接收一个字符串明文和一个使用 MD5 加密后的哈希值，返回解密结果（true 表示匹配，false 表示不匹配）
    public static boolean decrypt(String plaintext, String encrypted) throws NoSuchAlgorithmException {
        // 调用加密方法计算出明文的哈希值
        String decrypted = encrypt(plaintext);
        // 比较计算得到的哈希值和输入的哈希值是否相同
        return decrypted.equals(encrypted);
    }
}