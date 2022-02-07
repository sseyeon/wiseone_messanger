package com.messanger.engine.uc.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.commons.codec.binary.Base64;

import com.messanger.engine.uc.Constants;

public class SecurityUtils {

    private String algorithm = "DES/ECB/PKCS5Padding";
    private Key    key       = null;
    private Cipher cipher    = null;
    
    public SecurityUtils(String userDirectoryPath) {
        
        try {
            cipher = Cipher.getInstance(algorithm);
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(userDirectoryPath + Constants.FILE_SECRET_KEY));
            key = (Key) in.readObject();
            in.close();
        } catch (FileNotFoundException e) {
                try {
                    KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
                    keygenerator.init(new SecureRandom());
                    key = keygenerator.generateKey();
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userDirectoryPath + Constants.FILE_SECRET_KEY));
                    out.writeObject(key);
                    out.close();
                    
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(userDirectoryPath + Constants.FILE_SECRET_KEY));
                    key = (Key) in.readObject();
                    in.close();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 
     * @param input
     * @return
     * @throws Exception
     */
    public String encrypt(byte[] input) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key);        
		byte[] encoded = Base64.encodeBase64(cipher.doFinal(input));
		return new String(encoded, "iso-8859-1");
    }

    
    

    /**
     * 
     * @param encryptString
     * @return
     * @throws Exception
     */

    public String decrypt(String encryptString) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key);        
        byte[] raw = Base64.decodeBase64(encryptString.getBytes("iso-8859-1"));
        return new String(cipher.doFinal(raw), Charset.forName("UTF-8"));
    }

    
}
