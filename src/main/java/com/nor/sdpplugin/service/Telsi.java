package com.nor.sdpplugin.service;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

public class Telsi {
    private static final Logger logger = LoggerFactory.getLogger(Telsi.class);

    public static void main(String[] args) {
        Telsi telsi = new Telsi();
        try {
            String s = telsi.callTelsi("09148289035");
            System.out.println(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String callTelsi(String mobileNo) throws IOException {
        String outPut = "";
        OkHttpClient client = getUnsafeOkHttpClient().newBuilder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();

        String url = "https://callcenter.respina.net/extensions/dialback/1013/7" + mobileNo + "/700020";
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();

        logger.info("Calling GET service: {}", url);
        okhttp3.Response response = client.newCall(request).execute();

        if (response.body() != null)
            outPut = response.body().string();

        logger.info("Response code:{} ,\tresponse body:{}  ,\tresponse headers:{}", response.code(), outPut, response.headers().toString().replace("\n", ",\t"));
        if (response.code() == 200)
            return "1";
        return "0";
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
