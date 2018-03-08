package com.mkit.mshanoi.app.di;


import com.mkit.mshanoi.app.LineApplication;

import dagger.Module;
import dagger.Provides;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by LiKaLi on 1/22/2018.
 */
@Module(complete = false, library = true)
public class NetModule {
  @Provides
  public InputStream byteArrayInput() {
    String rapidCACert = "-----BEGIN CERTIFICATE-----\n" +
        "MIIETTCCAzWgAwIBAgIDAjpxMA0GCSqGSIb3DQEBCwUAMEIxCzAJBgNVBAYTAlVT\n" +
        "MRYwFAYDVQQKEw1HZW9UcnVzdCBJbmMuMRswGQYDVQQDExJHZW9UcnVzdCBHbG9i\n" +
        "YWwgQ0EwHhcNMTMxMjExMjM0NTUxWhcNMjIwNTIwMjM0NTUxWjBCMQswCQYDVQQG\n" +
        "EwJVUzEWMBQGA1UEChMNR2VvVHJ1c3QgSW5jLjEbMBkGA1UEAxMSUmFwaWRTU0wg\n" +
        "U0hBMjU2IENBMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu1jBEgEu\n" +
        "l9h9GKrIwuWF4hdsYC7JjTEFORoGmFbdVNcRjFlbPbFUrkshhTIWX1SG5tmx2GCJ\n" +
        "a1i+ctqgAEJ2sSdZTM3jutRc2aZ/uyt11UZEvexAXFm33Vmf8Wr3BvzWLxmKlRK6\n" +
        "msrVMNI4/Bk7WxU7NtBDTdFlodSLwWBBs9ZwF8w5wJwMoD23ESJOztmpetIqYpyg\n" +
        "C04q18NhWoXdXBC5VD0tA/hJ8LySt7ecMcfpuKqCCwW5Mc0IW7siC/acjopVHHZD\n" +
        "dvDibvDfqCl158ikh4tq8bsIyTYYZe5QQ7hdctUoOeFTPiUs2itP3YqeUFDgb5rE\n" +
        "1RkmiQF1cwmbOwIDAQABo4IBSjCCAUYwHwYDVR0jBBgwFoAUwHqYaI2J+6sFZAwR\n" +
        "fap9ZbjKzE4wHQYDVR0OBBYEFJfCJ1CewsnsDIgyyHyt4qYBT9pvMBIGA1UdEwEB\n" +
        "/wQIMAYBAf8CAQAwDgYDVR0PAQH/BAQDAgEGMDYGA1UdHwQvMC0wK6ApoCeGJWh0\n" +
        "dHA6Ly9nMS5zeW1jYi5jb20vY3Jscy9ndGdsb2JhbC5jcmwwLwYIKwYBBQUHAQEE\n" +
        "IzAhMB8GCCsGAQUFBzABhhNodHRwOi8vZzIuc3ltY2IuY29tMEwGA1UdIARFMEMw\n" +
        "QQYKYIZIAYb4RQEHNjAzMDEGCCsGAQUFBwIBFiVodHRwOi8vd3d3Lmdlb3RydXN0\n" +
        "LmNvbS9yZXNvdXJjZXMvY3BzMCkGA1UdEQQiMCCkHjAcMRowGAYDVQQDExFTeW1h\n" +
        "bnRlY1BLSS0xLTU2OTANBgkqhkiG9w0BAQsFAAOCAQEANevhiyBWlLp6vXmp9uP+\n" +
        "bji0MsGj21hWID59xzqxZ2nVeRQb9vrsYPJ5zQoMYIp0TKOTKqDwUX/N6fmS/Zar\n" +
        "RfViPT9gRlATPSATGC6URq7VIf5Dockj/lPEvxrYrDrK3maXI67T30pNcx9vMaJR\n" +
        "BBZqAOv5jUOB8FChH6bKOvMoPF9RrNcKRXdLDlJiG9g4UaCSLT+Qbsh+QJ8gRhVd\n" +
        "4FB84XavXu0R0y8TubglpK9YCa81tGJUheNI3rzSkHp6pIQNo0LyUcDUrVNlXWz4\n" +
        "Px8G8k/Ll6BKWcZ40egDuYVtLLrhX7atKz4lecWLVtXjCYDqwSfC2Q7sRwrp0Mr8\n" +
        "2A==\n" +
        "-----END CERTIFICATE-----";
    return new ByteArrayInputStream(rapidCACert.getBytes());
  }
  @Provides
  public KeyStore newEmptyKeyStore() {
    KeyStore keyStore = null;
    try {
      char[] password = "password".toCharArray();
      keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      InputStream in = null; // By convention, 'null' creates an empty key store.
      keyStore.load(in, password);
    } catch (KeyStoreException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (CertificateException e) {
      e.printStackTrace();
    }
    return keyStore;
  }
  @Provides
  public SSLContext sslContextForTrustedCertificates(InputStream in,KeyStore newEmptyKeyStore) {
    try {
      CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
      Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
      if (certificates.isEmpty()) {
        throw new IllegalArgumentException("expected non-empty set of trusted certificates");
      }
      // Put the certificates a key store.
      // Any password will work.
      char[] password = "password".toCharArray();
      KeyStore keyStore = newEmptyKeyStore;
      int index = 0;
      for (Certificate certificate : certificates) {
        String certificateAlias = Integer.toString(index++);
        keyStore.setCertificateEntry(certificateAlias, certificate);
      }
      // Wrap it up in an SSL context.
      KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
          KeyManagerFactory.getDefaultAlgorithm());
      keyManagerFactory.init(keyStore, password);
      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
          TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init(keyStore);
      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
          new SecureRandom());
      return sslContext;

    } catch (CertificateException e) {
      e.printStackTrace();
      return null;
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    } catch (KeyStoreException e) {
      e.printStackTrace();
      return null;
    } catch (KeyManagementException e) {
      e.printStackTrace();
      return null;
    } catch (UnrecoverableKeyException e) {
      e.printStackTrace();
      return null;
    }

  }
  @Provides
  public OkHttpClient getUnsafeOkHttpClient(SSLContext sslContextForTrustedCertificates) {
    try {
      // Install the all-trusting trust manager
      final SSLContext sslContext = sslContextForTrustedCertificates;

      // Create an ssl socket factory with our all-trusting manager
      final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
      OkHttpClient okHttpClient = new OkHttpClient();

      okHttpClient = okHttpClient.newBuilder().sslSocketFactory(sslSocketFactory)
          .hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
              Certificate[] certificates = null;
              try {
                certificates = session.getPeerCertificates();
              } catch (Exception ex) {
                return false;
              }
              //String host = session.getPeerHost();
              X509Certificate x509Certificate = (X509Certificate) certificates[0];
              String DN = x509Certificate.getSubjectDN().toString();
              int i = DN.indexOf("CN=");
              int j = DN.indexOf(",", DN.indexOf("CN="));
              String cn = DN.substring(i, (j < 0) ? DN.length() : j);
              String[] cns = cn.split("=");

              String tmp = cns[1];
              int k = tmp.indexOf("*");
              if (k >= 0) {
                tmp = cns[1].substring(++k);
                return (hostname.endsWith(tmp)) ? true : false;
              } else {
                return (hostname.equals(tmp)) ? true : false;
              }
            }
          }).build();
      return okHttpClient;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  @Provides
  public Retrofit.Builder provideRestAdapterBuilder(OkHttpClient okHttpClient) {
    return new Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(LineApplication.apiBaseUrl)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
  }
}
