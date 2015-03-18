package com.mycompany.myproject;

/**
 * Created by user on 3/17/2015.
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

public class MD5Gen {

    public static String EncryptString(String inputString){
        try {

           // String inputString = "connect";
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inputString.getBytes());
            byte byteData[] = md.digest();
            //convert the byte to hex format method 1

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
            //System.out.println("Password after hashed " + sb.toString());
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
