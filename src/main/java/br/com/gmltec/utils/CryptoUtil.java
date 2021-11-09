package br.com.gmltec.utils;

import java.math.BigInteger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {
	private static String hmac(String plainText, String key, String algorithm, String format) throws Exception {
        if (plainText == null || key == null) {
            return null;
        }

        byte[] hmacResult = null;

        Mac mac = Mac.getInstance(algorithm);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        mac.init(secretKeySpec);
        hmacResult = mac.doFinal(plainText.getBytes());
        return String.format(format, new BigInteger(1, hmacResult));
    }

//    public static String hmacMd5(String plainText, String key) throws Exception {
//        return hmac(plainText,key,"HmacMD5","%032x");
//    }
//
//    public static String hmacSha1(String plainText, String key) throws Exception {
//        return hmac(plainText,key,"HmacSHA1","%040x");
//    }

    public static String hmacSha256(String plainText, String key) throws Exception {
        return hmac(plainText,key,"HmacSHA256","%064x");
    }

}
