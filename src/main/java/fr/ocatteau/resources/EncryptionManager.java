package fr.ocatteau.resources;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class EncryptionManager {
	private String key;

	public EncryptionManager(String key) {
		this.key = key;
	}

	/**
	 * Encrypt a file.
	 * @param inputFile the {@link File} to encrypt
	 * @param encryptedFile the encrypted {@link File}
	 * @throws IOException
	 */
	public void encrypt(File inputFile, File encryptedFile) throws IOException {
		fileProcessor(Cipher.ENCRYPT_MODE, key, inputFile, encryptedFile);
	}

	/**
	 * Decrypt an encrypted file.
	 * @param encryptedFile the encrypted {@link File}
	 * @param decryptedFile the decrypted {@link File}
	 * @throws IOException
	 */
	public void decrypt(File encryptedFile, File decryptedFile) throws IOException {
		fileProcessor(Cipher.DECRYPT_MODE, key, encryptedFile, decryptedFile);
	}

	private void fileProcessor(int cipherMode, String key, File inputFile, File outputFile) throws IOException {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(cipherMode, secretKey);

			inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			outputStream = new FileOutputStream(outputFile);
			outputStream.write(outputBytes);
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}
}
