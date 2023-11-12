package org.rossonet.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
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
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

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

	public static String DEFAULT_CONTEXT_TLS_PROTOCOL = "TLSv1.2";

	public static KeyStore createKeystore(final String certificateAlias, final X509Certificate certificate,
			final String privateKeyAlias, final PrivateKey privateKey, final String keystorePassword)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		final KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		clientKeyStore.load(null, keystorePassword.toCharArray());
		clientKeyStore.setCertificateEntry(certificateAlias, certificate);
		clientKeyStore.setKeyEntry(privateKeyAlias, privateKey, keystorePassword.toCharArray(),
				new Certificate[] { certificate });
		return clientKeyStore;
	}

	public static KeyStore createKeystore(final String caAlias, final X509Certificate ca, final String certificateAlias,
			final X509Certificate certificate, final String privateKeyAlias, final PrivateKey privateKey,
			final String keystorePassword)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		final KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		clientKeyStore.load(null, keystorePassword.toCharArray());
		clientKeyStore.setCertificateEntry(certificateAlias, certificate);
		clientKeyStore.setKeyEntry(privateKeyAlias, privateKey, keystorePassword.toCharArray(),
				new Certificate[] { certificate });
		return clientKeyStore;
	}

	public static KeyStore createKeyStore(final String caAlias, final Path caCrtFile, final String certificateAlias,
			final Path crtFile, final String privateKeyAlias, final Path keyFile, final String keystorePassword)
			throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException {
		Security.addProvider(new BouncyCastleProvider());
		final JcaX509CertificateConverter certificateConverter = new JcaX509CertificateConverter().setProvider("BC");
		PEMParser reader = new PEMParser(new FileReader(caCrtFile.toFile().getAbsolutePath()));
		final X509CertificateHolder caCertHolder = (X509CertificateHolder) reader.readObject();
		reader.close();
		final X509Certificate caCert = certificateConverter.getCertificate(caCertHolder);
		reader = new PEMParser(new FileReader(crtFile.toFile().getAbsolutePath()));
		final X509CertificateHolder certHolder = (X509CertificateHolder) reader.readObject();
		reader.close();
		final X509Certificate cert = certificateConverter.getCertificate(certHolder);
		reader = new PEMParser(new FileReader(keyFile.toFile().getAbsolutePath()));
		final PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) reader.readObject();
		reader.close();
		final KeyStore keyStore = createKeyStore(caAlias, caCert, certificateAlias, cert, privateKeyAlias,
				privateKeyInfo, keystorePassword);
		return keyStore;
	}

	public static KeyStore createKeyStore(final String caAlias, final String caCrtString, final String certificateAlias,
			final String certificateString, final String privateKeyAlias, final String privateKeyString,
			final String keystorePassword)
			throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException {
		final Path caCrtFile = Files.createTempFile("caCrtFile", ".pem");
		final Path crtFile = Files.createTempFile("crtFile", ".pem");
		final Path keyFile = Files.createTempFile("keyFile", ".pem");
		Files.write(caCrtFile, caCrtString.getBytes());
		Files.write(crtFile, certificateString.getBytes());
		Files.write(keyFile, privateKeyString.getBytes());
		final KeyStore keyStore = createKeyStore(caAlias, caCrtFile, certificateAlias, crtFile, privateKeyAlias,
				keyFile, keystorePassword);
		caCrtFile.toFile().delete();
		crtFile.toFile().delete();
		keyFile.toFile().delete();
		return keyStore;
	}

	public static TrustManagerFactory createKeyStore(final String caAlias, final X509Certificate caCert)
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		final KeyStore caKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		caKeyStore.load(null, null);
		caKeyStore.setCertificateEntry(caAlias, caCert);
		final TrustManagerFactory trustManagerFactory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(caKeyStore);
		return trustManagerFactory;
	}

	public static KeyStore createKeyStore(final String certificateAlias, final X509Certificate certificate,
			final String privateKeyAlias, final PrivateKeyInfo privateKeyInfo, final String keystorePassword)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		final JcaPEMKeyConverter keyConverter = new JcaPEMKeyConverter().setProvider("BC");
		final PrivateKey key = keyConverter.getPrivateKey(privateKeyInfo);
		final KeyStore clientKeyStore = createKeystore(certificateAlias, certificate, privateKeyAlias, key,
				keystorePassword);
		return clientKeyStore;
	}

	public static KeyStore createKeyStore(final String caAlias, final X509Certificate ca, final String certificateAlias,
			final X509Certificate certificate, final String privateKeyAlias, final PrivateKeyInfo privateKeyInfo,
			final String keystorePassword)
			throws PEMException, KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		final JcaPEMKeyConverter keyConverter = new JcaPEMKeyConverter().setProvider("BC");
		final PrivateKey key = keyConverter.getPrivateKey(privateKeyInfo);
		final KeyStore clientKeyStore = createKeystore(caAlias, ca, certificateAlias, certificate, privateKeyAlias, key,
				keystorePassword);
		return clientKeyStore;
	}

	public static SSLContext createSSLContext(final String caAlias, final Path caCrtFile, final String certificateAlias,
			final Path crtFile, final String privateKeyAlias, final Path keyFile, final String keystorePassword)
			throws KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException,
			NoSuchAlgorithmException, IOException {
		return createSSLContext(keystorePassword, keyFile, keystorePassword, keyFile, keystorePassword, keyFile,
				keystorePassword, DEFAULT_CONTEXT_TLS_PROTOCOL);
	}

	public static SSLContext createSSLContext(final String caAlias, final Path caCrtFile, final String certificateAlias,
			final Path crtFile, final String privateKeyAlias, final Path keyFile, final String keystorePassword,
			final String sslContextProtocol) throws CertificateException, IOException, KeyStoreException,
			NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException {
		Security.addProvider(new BouncyCastleProvider());
		final JcaX509CertificateConverter certificateConverter = new JcaX509CertificateConverter().setProvider("BC");
		PEMParser reader = new PEMParser(new FileReader(caCrtFile.toFile().getAbsolutePath()));
		final X509CertificateHolder caCertHolder = (X509CertificateHolder) reader.readObject();
		reader.close();
		final X509Certificate caCert = certificateConverter.getCertificate(caCertHolder);
		reader = new PEMParser(new FileReader(crtFile.toFile().getAbsolutePath()));
		final X509CertificateHolder certHolder = (X509CertificateHolder) reader.readObject();
		reader.close();
		final X509Certificate cert = certificateConverter.getCertificate(certHolder);
		reader = new PEMParser(new FileReader(keyFile.toFile().getAbsolutePath()));
		final PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) reader.readObject();
		reader.close();
		final KeyStore clientKeyStore = createKeyStore(certificateAlias, cert, privateKeyAlias, privateKeyInfo,
				keystorePassword);
		final TrustManagerFactory trustManagerFactory = createKeyStore(caAlias, caCert);
		final KeyManagerFactory keyManagerFactory = KeyManagerFactory
				.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(clientKeyStore, keystorePassword.toCharArray());
		final SSLContext context = SSLContext.getInstance(sslContextProtocol);
		context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
		return context;
	}

	public static SSLContext createSSLContext(final String caAlias, final String caCrtString,
			final String certificateAlias, final String certificateString, final String privateKeyAlias,
			final String privateKeyString, final String keystorePassword) throws IOException, KeyManagementException,
			UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException {
		final Path caCrtFile = Files.createTempFile("caCrtFile", ".pem");
		final Path crtFile = Files.createTempFile("crtFile", ".pem");
		final Path keyFile = Files.createTempFile("keyFile", ".pem");
		Files.write(caCrtFile, caCrtString.getBytes());
		Files.write(crtFile, certificateString.getBytes());
		Files.write(keyFile, privateKeyString.getBytes());
		final SSLContext sslContext = createSSLContext(caAlias, caCrtFile, certificateAlias, crtFile, privateKeyAlias,
				keyFile, keystorePassword);
		caCrtFile.toFile().delete();
		crtFile.toFile().delete();
		keyFile.toFile().delete();
		return sslContext;
	}

	public static String encodeInPemFormat(final Object object) throws IOException {
		final StringWriter output = new StringWriter();
		final JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(output);
		jcaPEMWriter.writeObject(object);
		return output.toString();

	}

	public static String getDefaultCharSet() {
		final OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
		final String enc = writer.getEncoding();
		return enc;
	}

	private SslHelper() {
		throw new UnsupportedOperationException("Just for static usage");
	}
}
