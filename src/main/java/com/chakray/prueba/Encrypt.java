package com.chakray.prueba;

import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class Encrypt {

    // Nota para los desarrolladores: Estas claves fueron generadas aleatoriamente en https://us.norton.com/feature/password-generator
    private static final String key = "sta1$o0HlTi_huwez#p_d!ditrepr+to";
    private static final String salt = "phAg4S-xIb3b*OtIrucREjOVlQ6R18Ir";

    private SecretKey secretKeyTemp;

    public Encrypt(){
        SecretKeyFactory factory;
        KeySpec spec;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
            secretKeyTemp = factory.generateSecret(spec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encryptAES256(String password){
        byte[] iv = new byte[16];
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKey secretKey = new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(password.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decryptAES256(String password){
        byte[] iv = new byte[16];
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKey secretKey = new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(password)), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}