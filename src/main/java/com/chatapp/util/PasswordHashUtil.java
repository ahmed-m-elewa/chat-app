package com.chatapp.util;

import java.security.MessageDigest;
import java.util.Base64;

public class PasswordHashUtil {
    private static String dm5Hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++)
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            return sb.toString();
        } catch (Exception ex) {
            return null;
        }
    }

    public static String encodeAndHash(String password) {
        return dm5Hash(Base64.getEncoder().encodeToString(password.getBytes()));
    }

    public static void main(String[] args) {
        System.out.println(encodeAndHash("ali123"));
    }
}
