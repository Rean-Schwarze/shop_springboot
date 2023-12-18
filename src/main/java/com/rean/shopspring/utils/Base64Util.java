package com.rean.shopspring.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class Base64Util {
    private static final Decoder decoder= Base64.getDecoder();
    private static final Encoder encoder= Base64.getEncoder();


    /***
     * BASE64解密
     */
    public static String decryBASE64(byte[] key) throws Exception{
        return encoder.encodeToString(key);
    }

    /***
     * BASE64加密
     */
    public static String encryptBASE64(byte[] key) throws Exception{
        return new String(decoder.decode(key), StandardCharsets.UTF_8);
    }

}
