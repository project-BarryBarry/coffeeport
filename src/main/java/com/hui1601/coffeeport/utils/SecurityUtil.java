package com.hui1601.coffeeport.utils;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.security.auth.x500.X500Principal;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.logging.Logger;

public class SecurityUtil {

    public static String encryptSha3(String uuid) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(uuid.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // generate a new self signed localhost certificate with bouncycastle
    public static void generateKeyStore(KeyStore keyStore) {
        Security.addProvider(new BouncyCastleProvider());

        X500Principal subject = new X500Principal("CN=localhost");
        KeyPair keyPair = generateKeyPair();

        long notBefore = System.currentTimeMillis();
        long notAfter = notBefore + (1000L * 3600L * 24 * 365);

        ASN1Encodable[] encodableAltNames = new ASN1Encodable[]{new GeneralName(GeneralName.dNSName, "localhost"), new GeneralName(GeneralName.dNSName, "127.0.0.1")};
        KeyPurposeId[] purposes = new KeyPurposeId[]{KeyPurposeId.id_kp_serverAuth, KeyPurposeId.id_kp_clientAuth};

        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(subject, BigInteger.ONE, new Date(notBefore), new Date(notAfter), subject, keyPair.getPublic());

        try {
            certBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
            certBuilder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature + KeyUsage.keyEncipherment));
            certBuilder.addExtension(Extension.extendedKeyUsage, false, new ExtendedKeyUsage(purposes));
            certBuilder.addExtension(Extension.subjectAlternativeName, false, new DERSequence(encodableAltNames));

            final ContentSigner signer = new JcaContentSignerBuilder(("SHA256withRSA")).build(keyPair.getPrivate());
            X509CertificateHolder certHolder = certBuilder.build(signer);
            X509Certificate cert = new JcaX509CertificateConverter().getCertificate(certHolder);

            keyStore.setCertificateEntry("localhost", cert);
            // add private key to keystore
            keyStore.setKeyEntry("localhost", keyPair.getPrivate(), SystemUtil.getHashedUUID().toCharArray(), new Certificate[]{cert});

        } catch (Exception e) {
            Logger.getLogger(SecurityUtil.class.getName()).severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048, new SecureRandom());
            return keyPairGenerator.generateKeyPair();
        } catch (GeneralSecurityException var2) {
            throw new AssertionError(var2);
        }
    }

    public static KeyStore getKeystore() {
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            char[] pwdArray = SystemUtil.getHashedUUID().toCharArray();
            if(!FileUtil.exists(SystemUtil.getKeystorePath())) {
                keyStore.load(null, pwdArray);
                // create a new localhost certificate
                generateKeyStore(keyStore);
                keyStore.store(new FileOutputStream(SystemUtil.getKeystorePath()), pwdArray);
                return keyStore;
            }
            keyStore.load(FileUtil.readContentStream(SystemUtil.getKeystorePath()), SystemUtil.getHashedUUID().toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            Logger.getLogger(SecurityUtil.class.getName()).severe(e.getMessage());
            throw new AssertionError(e.getMessage());
        }
        return keyStore;
    }
}
