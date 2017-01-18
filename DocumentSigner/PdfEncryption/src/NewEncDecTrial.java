import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

public class NewEncDecTrial {

	public static byte[] getFile() {

		File f = new File("/home/bridgeit/Desktop/Contract.pdf");
		InputStream is = null;
		try {
			is = new FileInputStream(f);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		byte[] content = null;
		try {
			content = new byte[is.available()];
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			is.read(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return content;
	}

	public static byte[] encryptPdfFile(SecretKey secret, byte[] content) {
		Cipher cipher;
		byte[] encrypted = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret);
			encrypted = cipher.doFinal(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encrypted;

	}

	public static byte[] decryptPdfFile(SecretKey secret, byte[] textCryp) {

		Cipher cipher = null;
		byte[] decrypted = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] iv = null;
		try {
			iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
		} catch (InvalidParameterSpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			decrypted = cipher.doFinal(textCryp);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return decrypted;
	}

	public static void saveFile(byte[] bytes) throws IOException {

		FileOutputStream fos = new FileOutputStream("/home/bridgeit/Desktop/Contract-new.pdf");
		fos.write(bytes);
		fos.close();

	}

	public static void main(String args[])
			throws NoSuchAlgorithmException, InstantiationException, IllegalAccessException, IOException {

		/*
		 * KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		 * keyGenerator.init(128); Key key = keyGenerator.generateKey();
		 * System.out.println(key);
		 */

		String password = "Bridgelabz Solution LLP";
		byte[] salt = new byte[] { 9, 8, 9, 2, 4, 6, 9, 1, 2, 3 };

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKey tmp = null;
		try {
			tmp = factory.generateSecret(spec);
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
		System.out.println(secret);

		byte[] content = getFile();
		System.out.println(content);

		byte[] encrypted = encryptPdfFile(secret, content);
		System.out.println(encrypted);

		byte[] decrypted = decryptPdfFile(secret, encrypted);
		System.out.println(decrypted);

		saveFile(decrypted);
		System.out.println("Done");

	}

}
