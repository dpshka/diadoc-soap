package ru.grinn.diadocsoap.service;

import Diadoc.Api.CertificateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.CryptoPro.JCP.JCP;
import ru.grinn.diadocsoap.configuration.ApplicationConfiguration;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;

@Service
@Slf4j
public class CertificateService {
    private final PrivateKey privateKey;
    private final X509Certificate x509Certificate;

    public CertificateService(ApplicationConfiguration applicationConfiguration) throws Exception {
        Security.addProvider(new JCP());

        KeyStore keyStore = KeyStore.getInstance("HDImageStore");
        keyStore.load(null, null);
        log.info("Loading certificate alias={}", applicationConfiguration.getCertificateAlias());
        x509Certificate = (X509Certificate)keyStore.getCertificate(applicationConfiguration.getCertificateAlias());
        privateKey = CertificateHelper.getPrivateKey(x509Certificate, applicationConfiguration.getKeyPassword().toCharArray());
        log.info("Loaded certificate {}", x509Certificate.getSubjectX500Principal().getName());
    }

    public byte[] sign(byte[] bytes) throws Exception {
        return CertificateHelper.CMSSign(bytes, privateKey, x509Certificate, false);
    }

    public byte[] getRawCertificate() {
        try {
            return x509Certificate.getEncoded();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
