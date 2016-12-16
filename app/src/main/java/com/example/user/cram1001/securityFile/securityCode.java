package com.example.user.cram1001.securityFile;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class securityCode{
	
	private final static String IvAES = "1234567890abcdef" ;
	private final static String KeyAES = "12345678901234567890123456789012";
	
	
	public static String encode(String pass) {
		byte[] TextByte;
		String TEXT="";
		try {
			TextByte = EncryptAES(IvAES.getBytes("UTF-8"), KeyAES.getBytes("UTF-8"), pass.toString().getBytes("UTF-8"));
			TEXT = Base64.encodeToString(TextByte, Base64.DEFAULT);
			return TEXT;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return TEXT;
	}
	
	//AES�ѱK�A�a�Jbyte[]���A��16��^�ƲզX��r�B32��^�ƲզXKey�B�ݸѱK��r
	public static String decode(String pass){
		byte[] TextByte;
		String TEXT="";
		try {
			TextByte = DecryptAES(IvAES.getBytes("UTF-8"), KeyAES.getBytes("UTF-8"),Base64.decode(pass.toString().getBytes("UTF-8"), Base64.DEFAULT));
			TEXT = new String(TextByte,"UTF-8");
			return TEXT;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return TEXT;
	}
	

	private static byte[] EncryptAES(byte[] iv, byte[] key,byte[] text)
	{
	  try
	  {
	    AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(iv);
	    SecretKeySpec mSecretKeySpec = new SecretKeySpec(key, "AES");
	    Cipher mCipher = null;
	    mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    mCipher.init(Cipher.ENCRYPT_MODE,mSecretKeySpec,mAlgorithmParameterSpec);
	        
	    return mCipher.doFinal(text);
	  }
	  catch(Exception ex)
	  {
	    return null;
	  }
	}

	//AES�ѱK�A�a�Jbyte[]���A��16��^�ƲզX��r�B32��^�ƲզXKey�B�ݸѱK��r
	private static byte[] DecryptAES(byte[] iv,byte[] key,byte[] text)
	{
	  try
	  {
	    AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(iv);
	    SecretKeySpec mSecretKeySpec = new SecretKeySpec(key, "AES");
	    Cipher mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    mCipher.init(Cipher.DECRYPT_MODE, 
	                 mSecretKeySpec, 
	                 mAlgorithmParameterSpec);
	        
	    return mCipher.doFinal(text);
	  }
	  catch(Exception ex)
	  {
	    return null;
	  }
	}
}