package fr.ocatteau.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * Encryption / decryption component.
 */
public class EncryptionManager {
	private static final Logger LOG = LoggerFactory.getLogger(EncryptionManager.class);
	private String key;

	/**
	 * Constructor.
	 * @param key the encryption key
	 */
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
		try {
			fileProcessor(Cipher.ENCRYPT_MODE, key, inputFile, encryptedFile);
		} catch (IOException e) {
			LOG.error("Can't encrypt file : %s", encryptedFile.getName());
			throw e;
		}
	}

	/**
	 * Decrypt an encrypted file.
	 * @param encryptedFile the encrypted {@link File}
	 * @param decryptedFile the decrypted {@link File}
	 * @throws IOException
	 */
	public void decrypt(File encryptedFile, File decryptedFile) throws IOException {
		try {
			fileProcessor(Cipher.DECRYPT_MODE, key, encryptedFile, decryptedFile);
		} catch (IOException e) {
			LOG.error("Can't decrypt file : %s", encryptedFile.getName());
			throw e;
		}
	}

	private void fileProcessor(int cipherMode, String key, File inputFile, File outputFile) throws IOException {
		try(FileInputStream inputStream = new FileInputStream(inputFile);
			FileOutputStream outputStream = new FileOutputStream(outputFile)) {
			Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(cipherMode, secretKey);

			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);


			outputStream.write(outputBytes);
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException e) {
			throw new IOException(String.format("Can't process file : %s", inputFile.getName()), e);
		}
	}
}
