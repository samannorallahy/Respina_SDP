package com.nor.sdpplugin.service;

import com.nor.sdpplugin.dataBase.SQLiteDao;
import com.nor.sdpplugin.model.Response;
import com.nor.sdpplugin.model.StatusModel;
import lombok.Getter;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ServiceDeskPlus {
    private static final Logger logger = LoggerFactory.getLogger(ServiceDeskPlus.class);
    private String serviceAddress;
    private String authtoken;
    @Getter
    private StatusModel statusNameForCalling;
    @Getter
    private StatusModel statusNameForClosing;
    @Getter
    private StatusModel statusNameForReferredToAnExpert;
    @Getter
    private StatusModel statusNameForNotAllowedInThisTime;
    @Getter
    private StatusModel statusNameForNotCalled;
    @Getter
    private String user_udf_field;
    @Getter
    private String allowedTime;
    @Getter
    private int SchedulerDelayTime;

    public ServiceDeskPlus() {
//        serviceAddress = "http://localhost:8080";
//        authtoken = "373808F5-D523-44D9-899C-4E4EAB5FC5C6";
//        statusNameForJiraUpdate = "Open";
//        serviceAddress = Utils.readFile("c:" + File.separator + "callSDP" + File.separator + "serviceAddress.txt");
//        authtoken = Utils.readFile("c:" + File.separator + "callSDP" + File.separator + "authtoken.txt");
//
//        if (serviceAddress.equals("TOKEN FILE NOT FOUND!"))
//            serviceAddress = "https://sdp.okcs.com";
//        if (authtoken.equals("TOKEN FILE NOT FOUND!"))
//            authtoken = "878939BD-4E16-4B38-9B55-C8817E747659";

        SQLiteDao dao = new SQLiteDao();
        try {
            ArrayList<HashMap<String, String>> list = dao.selectQuery("SELECT * FROM sdpConfig");
//            list.forEach(System.out::println);
            serviceAddress = list.get(0).get("SERVICEADDRESS");
            authtoken = list.get(0).get("AUTHTOKEN");
            statusNameForCalling = new StatusModel(list.get(0).get("STATUSNAMEFORCALLING"));
            statusNameForClosing = new StatusModel(list.get(0).get("STATUSNAMEFORCLOSING"));
            statusNameForReferredToAnExpert = new StatusModel(list.get(0).get("STATUSNAMEFORREFERREDTOANEXPERT"));
            statusNameForNotAllowedInThisTime = new StatusModel(list.get(0).get("STATUSNAMEFORNOTALLOWEDINTHISTIME"));
            statusNameForNotCalled = new StatusModel(list.get(0).get("STATUSNAMEFORNOTCALLED"));
            SchedulerDelayTime = Integer.parseInt(list.get(0).get("SCHEDULERDELAYTIME"));
            user_udf_field = list.get(0).get("USER_UDF_FIELD");
            allowedTime = list.get(0).get("ALLOWEDTIME");
        } catch (Exception e) {
            logger.error(e.toString());
            System.exit(1);
        }
    }

    public Response postCallSDP(String input_data, String url) throws IOException {
        String outPut = "";
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .connectTimeout(2, TimeUnit.MINUTES)
//                .writeTimeout(2, TimeUnit.MINUTES)
//                .readTimeout(2, TimeUnit.MINUTES)
//                .build();
        OkHttpClient client = getUnsafeOkHttpClient().newBuilder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        MediaType mediaType = MediaType.parse("text/plain");

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("input_data", input_data)
                .build();

        Request request = new Request.Builder()
                .url(serviceAddress + url)
                .method("POST", body)
                .addHeader("authtoken", authtoken)
//                .addHeader("Cookie", "SDPSESSIONID=B1BA872ECD0FCB8607045C964EC4A934; _zcsr_tmp=db3bed0c-6c44-4dd6-a8ea-2dc473fbb521; sdpcsrfcookie=db3bed0c-6c44-4dd6-a8ea-2dc473fbb521")
                .build();
        logger.info("Calling POST service: {} ,\t input_data:{}", serviceAddress + url, input_data.replace("\r\n", "").replace("\t", ""));
        okhttp3.Response response = client.newCall(request).execute();

//        if (response.code() == 201)
        if (response.body() != null)
            outPut = response.body().string();

        logger.info("Response code:{} ,\tresponse body:{}  ,\tresponse headers:{}", response.code(), outPut, response.headers().toString().replace("\n", ",\t"));
        return new Response(response.code(), outPut, "", null, "", "", input_data);
    }

    public Response putCallSDP(File input_file, String id) throws IOException {
        String outPut = "";

//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .connectTimeout(2, TimeUnit.MINUTES)
//                .writeTimeout(2, TimeUnit.MINUTES)
//                .readTimeout(2, TimeUnit.MINUTES)
//                .build();
        OkHttpClient client = getUnsafeOkHttpClient().newBuilder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        MediaType mediaType = MediaType.parse("text/plain");

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("input_file", input_file.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                input_file.getAbsoluteFile()))
                .build();

        Request request = new Request.Builder()
                .url(serviceAddress + "/api/v3/requests/" + id + "/upload")
                .method("PUT", body)
                .addHeader("authtoken", authtoken)
                .addHeader("Accept", "application/vnd.manageengine.sdp.v3+json")
                .build();
        logger.info("Calling put service: {} ,\t input_file:{}", serviceAddress + "/api/v3/requests/" + id + "/upload", input_file.getName());
        okhttp3.Response response = client.newCall(request).execute();

//        if (response.code() == 200)
        if (response.body() != null)
            outPut = response.body().string();

        logger.info("Response code:{} ,\tresponse body:{}  ,\tresponse headers:{}", response.code(), outPut, response.headers().toString().replace("\n", ",\t"));
        return new Response(response.code(), outPut, "", null, "", "", "");
    }

    public Response putCallSdpUpdateStatus(String id, String input_data) throws IOException {
        String outPut = "";

//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .connectTimeout(2, TimeUnit.MINUTES)
//                .writeTimeout(2, TimeUnit.MINUTES)
//                .readTimeout(2, TimeUnit.MINUTES)
//                .build();
        OkHttpClient client = getUnsafeOkHttpClient().newBuilder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("input_data", input_data)
                .build();
        String addres = serviceAddress + "/api/v3/requests/" + id;
        Request request = new Request.Builder()
                .url(addres)
                .method("PUT", body)
                .addHeader("authtoken", authtoken)
                .addHeader("Accept", "application/vnd.manageengine.sdp.v3+json")
                .build();
        logger.info("Calling {} service: {} ,\t input_data:{}", addres, "PUT", input_data);
        okhttp3.Response response = client.newCall(request).execute();

//        if (response.code() == 200)
        if (response.body() != null)
            outPut = response.body().string();

        logger.info("Response code:{} ,\tresponse body:{}  ,\tresponse headers:{}", response.code(), outPut, response.headers().toString().replace("\n", ",\t"));
        return new Response(response.code(), outPut, "", null, "", "", "");
    }

    public Response putCallSdpUpdateWorklogs(String id, String input_data) throws IOException {
        String outPut = "";

        OkHttpClient client = getUnsafeOkHttpClient().newBuilder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("input_data", input_data)
                .build();
        String address = serviceAddress + "/api/v3/requests/" + id + "/worklogs";
        Request request = new Request.Builder()
                .url(address)
                .method("POST", body)
                .addHeader("authtoken", authtoken)
                .addHeader("Accept", "application/vnd.manageengine.sdp.v3+json")
                .build();
        logger.info("Calling {} service: {} ,\t input_data:{}", address, "POST", input_data);
        okhttp3.Response response = client.newCall(request).execute();

        if (response.body() != null)
            outPut = response.body().string();

        logger.info("Response code:{} ,\tresponse body:{}  ,\tresponse headers:{}", response.code(), outPut, response.headers().toString().replace("\n", ",\t"));
        return new Response(response.code(), outPut, "", null, "", "", "");
    }

    public Response getUserData(int id) throws IOException {
        String outPut = "";

//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .connectTimeout(2, TimeUnit.MINUTES)
//                .writeTimeout(2, TimeUnit.MINUTES)
//                .readTimeout(2, TimeUnit.MINUTES)
//                .build();
        OkHttpClient client = getUnsafeOkHttpClient().newBuilder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
//        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("input_data", "")
                .build();
        String addres = serviceAddress + "/api/v3/users/" + id;
//        if (type == 3)
//            addres = addres + "/worklogs";
        Request request = new Request.Builder()
                .url(addres)
                .method("GET", null)
                .addHeader("authtoken", authtoken)
                .addHeader("Accept", "application/vnd.manageengine.sdp.v3+json")
                .build();
        logger.info("Calling GET service: {}", addres);
        okhttp3.Response response = client.newCall(request).execute();

//        if (response.code() == 200)
        if (response.body() != null)
            outPut = response.body().string();

        logger.info("Response code:{} ,\tresponse body:{}  ,\tresponse headers:{}", response.code(), outPut, response.headers().toString().replace("\n", ",\t"));
        return new Response(response.code(), outPut, "", null, "", "", "");
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

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
