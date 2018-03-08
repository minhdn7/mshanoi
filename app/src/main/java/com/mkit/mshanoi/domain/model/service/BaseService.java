package com.mkit.mshanoi.domain.model.service;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mkit.mshanoi.app.LineApplication;
import com.mkit.mshanoi.app.utils.ErrorDef;
import com.mkit.mshanoi.domain.model.pojo.request.HeaderAPI;
import com.mkit.mshanoi.domain.model.pojo.response.APIError;
import com.mkit.mshanoi.domain.repository.TimeDef;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
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
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MinhDN on 28/12/2017.
 */

public class BaseService {
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Gson gson = new GsonBuilder().setLenient().create();
    private static Retrofit.Builder builder = null;

    private static KeyStore newEmptyKeyStore(char[] password)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private static SSLContext sslContextForTrustedCertificates(InputStream in) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
            if (certificates.isEmpty()) {
                throw new IllegalArgumentException("expected non-empty set of trusted certificates");
            }
            // Put the certificates a key store.
            char[] password = "password".toCharArray(); // Any password will work.
            KeyStore keyStore = newEmptyKeyStore(password);
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

    // handler https://
    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            InputStream certIS = new ByteArrayInputStream(rapidCACert.getBytes());
            final SSLContext sslContext = sslContextForTrustedCertificates(certIS);

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

    public static String rapidCACert = "-----BEGIN CERTIFICATE-----\n" +
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

    public static <T> APIError parseErrorHandler(Response<T> response) {
        Converter<ResponseBody, APIError> converter =
                builder.client(httpClient.build()).build().responseBodyConverter(APIError.class, new Annotation[0]);
        APIError error;
        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIError(0, ErrorDef.MESSAGE_RESPONSE);
        }
        return error;
    }





    public static <S> S createService(Class<S> serviceClass, final HeaderAPI headerAPI) {
        builder = new Retrofit.Builder().baseUrl(LineApplication.apiBaseUrl).addConverterFactory(GsonConverterFactory.create(gson));
        httpClient.connectTimeout(TimeDef.CONNECTION_TIMEOUT, TimeUnit.SECONDS).readTimeout(TimeDef.READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(TimeDef.WRITE_TIMEOUT, TimeUnit.SECONDS);
        if (headerAPI != null) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder();
                    requestBuilder.addHeader(headerAPI.getKey(), headerAPI.getValue());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }
        Retrofit retrofit = builder
//                .client(getUnsafeOkHttpClient())
                .build();
        return retrofit.create(serviceClass);
    }



}
