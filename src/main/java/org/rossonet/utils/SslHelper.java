package org.rossonet.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

/**
 * Need bouncycastle libs. This libs are not in the jar.
 * 
 * implementation group: 'org.bouncycastle', name: 'bcprov-jdk18on', version:
 * '1.76' implementation group: 'org.bouncycastle', name: 'bcpkix-jdk18on',
 * version: '1.76' implementation group: 'org.bouncycastle', name:
 * 'bcutil-jdk18on', version: '1.76'
 * 
 * @Author Andrea Ambrosini - Rossonet s.c.a.r.l.
 *
 */
public class SslHelper {

	public static KeyStore createKeystore(final String password, final X509Certificate cert, PrivateKey key)
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		final KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		clientKeyStore.load(null, null);
		clientKeyStore.setCertificateEntry("certificate", cert);
		clientKeyStore.setKeyEntry("private-key", key, password.toCharArray(), new Certificate[] { cert });
		return clientKeyStore;
	}

	public static String getDefaultCharSet() {
		final OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
		final String enc = writer.getEncoding();
		return enc;
	}

	public static SSLContext getSSLContext(final Path caCrtFile, final Path crtFile, final Path keyFile,
			final String password) throws Exception {
		try {
			Security.addProvider(new BouncyCastleProvider());
			final JcaX509CertificateConverter certificateConverter = new JcaX509CertificateConverter()
					.setProvider("BC");
			PEMParser reader = new PEMParser(new FileReader(caCrtFile.toFile().getAbsolutePath()));
			final X509CertificateHolder caCertHolder = (X509CertificateHolder) reader.readObject();
			reader.close();
			final X509Certificate caCert = certificateConverter.getCertificate(caCertHolder);
			reader = new PEMParser(new FileReader(crtFile.toFile().getAbsolutePath()));
			final X509CertificateHolder certHolder = (X509CertificateHolder) reader.readObject();
			reader.close();
			final X509Certificate cert = certificateConverter.getCertificate(certHolder);
			reader = new PEMParser(new FileReader(keyFile.toFile().getAbsolutePath()));
			final Object keyObject = reader.readObject();
			reader.close();
			final JcaPEMKeyConverter keyConverter = new JcaPEMKeyConverter().setProvider("BC");
			PrivateKey key;
			key = keyConverter.getPrivateKey((PrivateKeyInfo) keyObject);
			final KeyStore caKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			caKeyStore.load(null, null);
			caKeyStore.setCertificateEntry("ca-certificate", caCert);
			final TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(caKeyStore);
			final KeyStore clientKeyStore = createKeystore(password, cert, key);
			final KeyManagerFactory keyManagerFactory = KeyManagerFactory
					.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(clientKeyStore, password.toCharArray());
			final SSLContext context = SSLContext.getInstance("TLSv1.2");
			context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
			return context;

		} catch (final Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static SSLContext getSSLContext(String caCrtString, String certificateString, String privateKeyString,
			String keystorePassword) throws Exception {
		final Path caCrtFile = Files.createTempFile("caCrtFile", ".pem");
		final Path crtFile = Files.createTempFile("crtFile", ".pem");
		final Path keyFile = Files.createTempFile("keyFile", ".pem");
		Files.write(caCrtFile, caCrtString.getBytes());
		Files.write(crtFile, certificateString.getBytes());
		Files.write(keyFile, privateKeyString.getBytes());
		final SSLContext sslContext = getSSLContext(caCrtFile, crtFile, keyFile, keystorePassword);
		caCrtFile.toFile().delete();
		crtFile.toFile().delete();
		keyFile.toFile().delete();
		return sslContext;
	}

	private SslHelper() {
		throw new UnsupportedOperationException("Just for static usage");
	}
}
