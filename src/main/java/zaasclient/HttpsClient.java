/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zaasclient;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class HttpsClient implements Closeable {
    private CloseableHttpClient closeableHttpsClient;
    private final String keyStorePassword;
    private final String trustStorePassword;
    private final String keyStoreType;
    private final String trustStoreType;
    private final RequestConfig requestConfig;
    private String trustStorePath;
    private String keyStorePath;

    public HttpsClient(ConfigProperties configProperties) {
        this.requestConfig = this.buildCustomRequestConfig();
        this.keyStorePath = configProperties.getKeyStorePath();
        this.keyStorePassword = configProperties.getKeyStorePassword();
        this.keyStoreType = configProperties.getKeyStoreType();
        this.trustStorePath = configProperties.getTrustStorePath();
        this.trustStorePassword = configProperties.getTrustStorePassword();
        this.trustStoreType = configProperties.getTrustStoreType();
    }


    public CloseableHttpClient getHttpsClientWithTrustStore()
        throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, KeyManagementException {
        SSLContext sslContext;
        TrustManagerFactory tmf = null;

        if (trustStorePath != null) {
            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore trustStore = KeyStore.getInstance(trustStoreType);
            File trustFile = new File(trustStorePath);
            trustStore.load(new FileInputStream(trustFile), trustStorePassword.toCharArray());
            tmf.init(trustStore);
        }
        sslContext = this.getSSLContext(null, tmf);

        return getCloseableHttpClient(sslContext);
    }

    /**
     * an overloaded version of getHttpsClientWithTrustStore for the query api
     *
     * @param cookieStore
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws KeyManagementException
     */
    public CloseableHttpClient getHttpsClientWithTrustStore(BasicCookieStore cookieStore)
        throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, KeyManagementException {
        SSLContext sslContext;
        TrustManagerFactory tmf = null;

        if (trustStorePath != null) {
            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore trustStore = KeyStore.getInstance(trustStoreType);
            File trustFile = new File(trustStorePath);
            trustStore.load(new FileInputStream(trustFile), trustStorePassword.toCharArray());
            tmf.init(trustStore);
        }
        sslContext = this.getSSLContext(null, tmf);

        return getCloseableHttpClient(sslContext, cookieStore);
    }

    /**
     * This method is used to return httpClient with TrustStore & KeyStore
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    public CloseableHttpClient getHttpsClientWithKeyStoreAndTrustStore()
        throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        SSLContext sslContext;
        TrustManagerFactory tmf = null;
        KeyManagerFactory kmf = null;

        if (trustStorePath != null) {
            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore trustStore = KeyStore.getInstance(trustStoreType);
            File trustFile = new File(trustStorePath);
            trustStore.load(new FileInputStream(trustFile), trustStorePassword.toCharArray());
            tmf.init(trustStore);
        }

        if (keyStorePath != null) {
            kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            File keyFile = new File(keyStorePath);
            keyStore.load(new FileInputStream(keyFile), keyStorePassword.toCharArray());
            kmf.init(keyStore, keyStorePassword.toCharArray());
        }

        sslContext = this.getSSLContext(kmf, tmf);

        return getCloseableHttpClient(sslContext);
    }

    private CloseableHttpClient getCloseableHttpClient(SSLContext sslContext) {
        final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
            SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        closeableHttpsClient = HttpClients.custom()
            .setSSLSocketFactory(sslsf)
            .setDefaultRequestConfig(this.requestConfig)
            .setMaxConnTotal(3 * 3)
            .setMaxConnPerRoute(3)
            .build();
        return closeableHttpsClient;
    }

    /**
     * Overloaded method of the getclosableHttpClient method .. so that the cookies can be used for storing jwt tokens
     *
     * @param sslContext
     * @param cookieStore
     * @return
     */
    private CloseableHttpClient getCloseableHttpClient(SSLContext sslContext, BasicCookieStore cookieStore) {
        final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
            SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        closeableHttpsClient = HttpClients.custom()
            .setSSLSocketFactory(sslsf)
            .setDefaultRequestConfig(this.requestConfig)
            .setMaxConnTotal(3 * 3)
            .setMaxConnPerRoute(3)
            .setDefaultCookieStore(cookieStore)
            .build();
        return closeableHttpsClient;
    }


    /**
     * @param kmf
     * @param tmf
     * @return
     * @throws Exception
     */
    private SSLContext getSSLContext(KeyManagerFactory kmf, TrustManagerFactory tmf) throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf != null ? kmf.getKeyManagers() : null, tmf != null ? tmf.getTrustManagers() : null, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw e;
        }
        return sslContext;
    }

    /**
     * This method is sed to create custom request configuration
     *
     * @return
     */
    private RequestConfig buildCustomRequestConfig() {
        final RequestConfig.Builder builder = RequestConfig.custom();
        builder.setConnectionRequestTimeout(10 * 1000);
        builder.setSocketTimeout(10 * 1000);
        builder.setConnectTimeout(10 * 1000);
        return builder.build();
    }

    @Override
    public void close() throws IOException {
        closeableHttpsClient.close();
    }
}
