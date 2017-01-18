// Program for generating the same key again and again.
// If multiple keys are generated for encryption and decryption of data
// it would have to be stored in the database and we cannot save the keys in the database 
// for the sole reason that if the database has been compromised one cannot use the keys to decrypt the data

package org.bridgelabz.documentsigner.keygenerator;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class KeyGenerator {

	SecretKey Key = generateKey();

	public SecretKey generateKey() {
		String password = "Bridgelabz Solution LLP";
		byte[] salt = new byte[] { 9, 8, 9, 2, 4, 6, 9, 1, 2, 3 };

		SecretKeyFactory factory = null;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKey tmp = null;
		try {
			tmp = factory.generateSecret(spec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
		return secret;
	}

	public byte[] encryptFile(SecretKey secret, byte[] bytes) {
		Cipher cipher;
		byte[] encryptedFile = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret);
			encryptedFile = cipher.doFinal(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedFile;
	}

	public byte[] decryptFile(SecretKey secret, byte[] bytes) {
		Cipher cipher = null;
		byte[] decryptedFile = null;
		byte[] iv = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		try {
			iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
		} catch (InvalidParameterSpecException e) {
			e.printStackTrace();
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}

		try {
			decryptedFile = cipher.doFinal(bytes);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}

		return decryptedFile;
	}

}
