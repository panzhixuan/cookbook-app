package com.example.cookbook.util;

public class PasswordMD5 {
    public static String passwordMd5(String password) {
        String salt = "KKDK64DDS39FFKFQQ1VMLFL";
        return MD5Utils.md5(MD5Utils.md5(salt + password));
    }
}
