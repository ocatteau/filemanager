package fr.ocatteau.resources;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class EncryptionManagerTest {

	private EncryptionManager encryptionManager;
	private File inputFile;
	private File encryptedFile;
	private File decryptedFile;

	@Before
	public void setUp() throws Exception {
		encryptionManager = new EncryptionManager("This is a secret");
		inputFile = new File(encryptionManager.getClass().getResource("/fr/ocatteau/resources/text.txt").toURI());
		encryptedFile = new File(inputFile.getParent(), "encrypted.txt");
		decryptedFile = new File(inputFile.getParent(), "decrypted.txt");
	}

	@After
	public void tearDown() throws Exception {
		encryptedFile.delete();
		decryptedFile.delete();
	}

	@Test
	public void encrypt_decrypt() throws Exception {
		encryptionManager.encrypt(inputFile, encryptedFile);
		encryptionManager.decrypt(encryptedFile, decryptedFile);

		assertTrue(FileUtils.contentEquals(inputFile, decryptedFile));
	}
}