package com.barrybarry.coffeeport.utils;

import com.barrybarry.coffeeport.constant.Message;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.security.auth.x500.X500Principal;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class SecurityUtil {
    private static final Logger logger = Logger.getLogger(SecurityUtil.class.getName());
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

    // generate a new self-signed localhost certificate with bouncycastle
    public static void generateKeyStore(KeyStore keyStore) {
        Security.addProvider(new BouncyCastleProvider());

        X500Principal subject = new X500Principal("CN=coffeeport");
        KeyPair keyPair = generateKeyPair();

        long notBefore = System.currentTimeMillis();
        long notAfter = notBefore + (1000L * 3600L * 24 * 365);

        ASN1Encodable[] encodableAltNames = new ASN1Encodable[]{new GeneralName(GeneralName.dNSName, "localhost"),
                new GeneralName(GeneralName.dNSName, "127.0.0.1")
        };
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
            if (!FileUtil.exists(SystemUtil.getKeystorePath())) {
                keyStore.load(null, pwdArray);
                // create a new localhost certificate
                generateKeyStore(keyStore);
                keyStore.store(new FileOutputStream(SystemUtil.getKeystorePath()), pwdArray);
                FileUtil.writeContent(SystemUtil.getAppDataPath() + "ca.crt",
                        "-----BEGIN CERTIFICATE-----\n" +
                                new String(Base64.getMimeEncoder(64, "\n".getBytes()).encode(keyStore.getCertificate("localhost").getEncoded()))
                                + "\n-----END CERTIFICATE-----");
                System.out.println(Message.MSG_NEW_KEYSTORE);
                System.out.println(Message.MSG_NEW_KEYSTORE_MANUAL_ADD_NOTICE);
                System.out.println(SystemUtil.getAppDataPath() + "ca.crt");
            } else {
                keyStore.load(FileUtil.readContentStream(SystemUtil.getKeystorePath()), SystemUtil.getHashedUUID().toCharArray());
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            Logger.getLogger(SecurityUtil.class.getName()).severe(e.getMessage());
            throw new AssertionError(e.getMessage());
        }
        return keyStore;
    }

    // convert pem certificate to X509CertificateHolder
    private static X509CertificateHolder convertPemToX509CertificateHolder(String pem) throws CertificateException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(pem.getBytes()));
        return new X509CertificateHolder(cert.getEncoded());
    }

    public static String verifyCmsSign(byte[] data, String caCertPem) {
        String result = null;
        // verify CMS message with Root CA certificate, return the data if verified
        try {
            CMSSignedData cms = new CMSSignedData(data);
            SignerInformationStore signers = cms.getSignerInfos();
            Collection<SignerInformation> c = signers.getSigners();
            Iterator<SignerInformation> it = c.iterator();
            X509CertificateHolder caCertHolder = convertPemToX509CertificateHolder(caCertPem);
            while (it.hasNext()) {
                SignerInformation signer = it.next();
                @SuppressWarnings("unchecked")
                Collection<X509CertificateHolder> certCollection = cms.getCertificates().getMatches(signer.getSID());
                Iterator<X509CertificateHolder> certIt = certCollection.iterator();
                X509CertificateHolder cert = certIt.next();
                if (signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(cert))) {
                    // X509CertificateHolder to X509Certificate
                    X509Certificate x509Cert = new JcaX509CertificateConverter().getCertificate(cert);
                    X509Certificate caCert = new JcaX509CertificateConverter().getCertificate(caCertHolder);
                    // verify the certificate with Root CA certificate
                    try {
                        x509Cert.verify(caCert.getPublicKey());
                        byte[] content = (byte[]) cms.getSignedContent().getContent();
                        // return the data with utf-8 encoding
                        result = new String(content, StandardCharsets.UTF_8);
                        break;
                    } catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException |
                             NoSuchProviderException | SignatureException ignored) {
                    }
                }
            }
        } catch (CMSException | OperatorCreationException | CertificateException | IOException e) {
            Logger.getLogger(SecurityUtil.class.getName()).severe(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    public static boolean checkAllowedDomain(String origin, String[] allowDomains){
        boolean result = false;
        if (allowDomains == null || allowDomains.length == 0) {
            return true;
        }
        for (String domain : allowDomains) {
            // * matches only subdomains. 2nd level subdomain is not matched.
            if (domain.startsWith("*.")) {
                String domainName = domain.substring(2);
                if (origin.endsWith(domainName)) {
                    result = true;
                    break;
                }
            } else {
                if (origin.equals(domain)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
