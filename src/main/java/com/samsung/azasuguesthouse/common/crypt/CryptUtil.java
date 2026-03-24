package com.samsung.azasuguesthouse.common.crypt;

import com.samsung.azasuguesthouse.common.log.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptUtil {

    public static String sha256(String data, String salt) {
        byte[] byteData;
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes());
            md.update(salt.getBytes());
            byteData = md.digest();

            StringBuilder sb = new StringBuilder();
            String hexNumber;
            for (byte byteDatum : byteData) {
                hexNumber = "0" + Integer.toHexString(0xff & byteDatum);

                sb.append(hexNumber.substring(hexNumber.length() - 2));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e){
            Log.error(e.getMessage(), e);
        }

        return null;
    }
}
