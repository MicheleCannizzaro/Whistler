package it.aps.whistler.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AESUtil {
	public static SecretKey generateKey(int n){
	    SecretKey key = null;
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		    keyGenerator.init(n);
		    key = keyGenerator.generateKey();
	    }catch(NoSuchAlgorithmException ex) {
	    	System.out.println("[AESUtil]: Error in SecretKey generation "+ex);
	    }
	    return key;
	}
	
	public static IvParameterSpec generateIv() {
	    byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		//byte[] iv ={ 0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0, 7, 0, 8 }; //Use this to have always the same iv - (not secure) //it's fine though for simulation reasons
	    return new IvParameterSpec(iv);								 
	}
	
	public static String encrypt(String algorithm, String input, SecretKey key,IvParameterSpec iv) 
			throws NoSuchPaddingException, NoSuchAlgorithmException,
		    InvalidAlgorithmParameterException, InvalidKeyException,
		    BadPaddingException, IllegalBlockSizeException {
		    
		    Cipher cipher = Cipher.getInstance(algorithm);
		    cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		    byte[] cipherText = cipher.doFinal(input.getBytes());
		    return Base64.getEncoder()
		        .encodeToString(cipherText);
		}
	
	public static String decrypt(String algorithm, String cipherText, SecretKey key,
		    IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
		    InvalidAlgorithmParameterException, InvalidKeyException,
		    BadPaddingException, IllegalBlockSizeException {
		    
		    Cipher cipher = Cipher.getInstance(algorithm);
		    cipher.init(Cipher.DECRYPT_MODE, key, iv);
		    byte[] plainText = cipher.doFinal(Base64.getDecoder()
		        .decode(cipherText));
		    return new String(plainText);
		}
	
	public static String encryptPassword(String plainTextPassword, SecretKey secretKey, IvParameterSpec iv ) {
		String encryptedPassword = null;	
		
		try {
			
			encryptedPassword = AESUtil.encrypt("AES/CBC/PKCS5Padding", plainTextPassword, secretKey, iv);
			
		}catch(NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException |
			   BadPaddingException| IllegalBlockSizeException ex) {
			System.out.println("[AESUtil] - Error in Password Encryption "+ex);
		} 
	
		return encryptedPassword;
	}
	
	public static String decryptPassword(String encryptedPassword, SecretKey secretKey, IvParameterSpec iv) {
		String plainTextPassword = null;
		try {
			plainTextPassword = AESUtil.decrypt("AES/CBC/PKCS5Padding", encryptedPassword, secretKey, iv);
		}catch(NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException |
			   BadPaddingException| IllegalBlockSizeException ex) {
			System.out.println("[AESUtil] - Error in deciphering encryptedPassword " + ex);
		}
		return plainTextPassword;
	}
}
