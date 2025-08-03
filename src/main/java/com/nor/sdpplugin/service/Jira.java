package com.nor.sdpplugin.service;

import com.nor.sdpplugin.dataBase.SQLiteDao;
import com.nor.sdpplugin.model.AddToJiraModel;
import com.nor.sdpplugin.model.Response;
import com.nor.sdpplugin.model.UpdateJiraModel;
import lombok.Getter;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Jira {
    private static final Logger logger = LoggerFactory.getLogger(Jira.class);
    @Getter
    private String itmsGroupField;
    private String serviceAddress;
    private String username;
    private String password;
    private String keyNameToCreateTaskInIt;

    public Jira() {
//        serviceAddress = "http://localhost:8080";
//        authtoken = "373808F5-D523-44D9-899C-4E4EAB5FC5C6";
//        serviceAddress = Utils.readFile("c:" + File.separator + "callSDP" + File.separator + "serviceAddress.txt");
//        authtoken = Utils.readFile("c:" + File.separator + "callSDP" + File.separator + "authtoken.txt");
//
//        if (serviceAddress.equals("TOKEN FILE NOT FOUND!"))
//            serviceAddress = "https://sdp.okcs.com";
//        if (authtoken.equals("TOKEN FILE NOT FOUND!"))
//            authtoken = "878939BD-4E16-4B38-9B55-C8817E747659";

        SQLiteDao dao = new SQLiteDao();
        try {
            ArrayList<HashMap<String, String>> list = dao.selectQuery("SELECT * FROM jiraConfig");
//            list.forEach(System.out::println);
            serviceAddress = list.get(0).get("SERVICEADDRESS");
            username = list.get(0).get("USERNAME");
            password = list.get(0).get("PASSWORD");
            itmsGroupField = list.get(0).get("ITMSGROUPFIELD");
            keyNameToCreateTaskInIt = list.get(0).get("KEYNAMETOCREATETASKINIT");
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    public Response addRequestInJira(AddToJiraModel model) throws IOException {
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
        String json = "{" +
                "\"fields\":{" +
                /*     */"\"project\":{" +
                /*            */"\"key\":\"" + keyNameToCreateTaskInIt + "\"" +
                /*     */"}," +
                /*     */"\"summary\":\"" + model.getSummary() + "\"," +
                /*     */"\"description\":\"" + model.getDescription() + "\"," +
                /*     */"\"issuetype\":{" +
                /*            */"\"name\":\"Task\"" +
                /*     */"}" +
                /**/"}" +
                "}";
        RequestBody body = RequestBody.create(mediaType, json);

        String authString = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(authString.getBytes());
        String authorizationHeader = "Basic " + encodedAuth;
        String url = serviceAddress + "/rest/api/2/issue";
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", authorizationHeader)
                .build();

        logger.info("Calling POST service: {} ,\t input_data:{}", url, json);
        okhttp3.Response response = client.newCall(request).execute();

//        if (response.code() == 201)
        if (response.body() != null)
            outPut = response.body().string();

        logger.info("Response code:{} ,\tresponse body:{}  ,\tresponse headers:{}", response.code(), outPut, response.headers().toString().replace("\n", ",\t"));
        return new Response(response.code(), outPut, "", null, "", "", json);
    }

    public Response updateTransitionsInJira(UpdateJiraModel model) throws IOException {
        String outPut = "";
        OkHttpClient client = getUnsafeOkHttpClient().newBuilder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();

        MediaType mediaType = MediaType.parse("application/json");
        String json =
                "{\"transition\":{\"id\":\"" + model.getTransitionID() + "\"}," +
                        "\"update\":{\"comment\":[{\"add\": {\"body\":\"درخواست در سمت itsm به وضعیت بسته انتقال یافت\"}}]}" +
                        "}";
        RequestBody body = RequestBody.create(mediaType, json);

        String authString = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(authString.getBytes());
        String authorizationHeader = "Basic " + encodedAuth;
        String url = serviceAddress + "/rest/api/2/issue/" + model.getJiraKey() + "/transitions";
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", authorizationHeader)
                .build();

        logger.info("Calling POST service: {} ,\t JSON:{}", url, json);
        okhttp3.Response response = client.newCall(request).execute();

//        if (response.code() == 201)
        if (response.body() != null)
            outPut = response.body().string();

        logger.info("Response code:{} ,\tresponse body:{}  ,\tresponse headers:{}", response.code(), outPut, response.headers().toString().replace("\n", ",\t"));
        return new Response(response.code(), outPut, "", null, "", "", json);
    }

    public Response getDonTransitionsID(UpdateJiraModel model) throws IOException {
        String outPut = "";
        OkHttpClient client = getUnsafeOkHttpClient().newBuilder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();

        MediaType mediaType = MediaType.parse("application/json");
        String json = "";

        String authString = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(authString.getBytes());
        String authorizationHeader = "Basic " + encodedAuth;
        String url = serviceAddress + "/rest/api/2/issue/" + model.getJiraKey() + "/transitions";
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
//                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", authorizationHeader)
                .build();

        logger.info("Calling GET service: {}", url);
        okhttp3.Response response = client.newCall(request).execute();

//        if (response.code() == 201)
        if (response.body() != null)
            outPut = response.body().string();

        logger.info("Response code:{} ,\tresponse body:{}  ,\tresponse headers:{}", response.code(), outPut, response.headers().toString().replace("\n", ",\t"));
        return new Response(response.code(), outPut, "", null, "", "", json);
    }


//    public Response putCallSDP(File input_file, String id) throws IOException {
//        String outPut = "";
//
////        OkHttpClient client = new OkHttpClient().newBuilder()
////                .connectTimeout(2, TimeUnit.MINUTES)
////                .writeTimeout(2, TimeUnit.MINUTES)
////                .readTimeout(2, TimeUnit.MINUTES)
////                .build();
//        OkHttpClient client = getUnsafeOkHttpClient().newBuilder()
//                .connectTimeout(2, TimeUnit.MINUTES)
//                .writeTimeout(2, TimeUnit.MINUTES)
//                .readTimeout(2, TimeUnit.MINUTES)
//                .build();
//        MediaType mediaType = MediaType.parse("text/plain");
//
//        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                .addFormDataPart("input_file", input_file.getName(),
//                        RequestBody.create(MediaType.parse("application/octet-stream"),
//                                input_file.getAbsoluteFile()))
//                .build();
//
//        Request request = new Request.Builder()
//                .url(serviceAddress + "/api/v3/requests/" + id + "/upload")
//                .method("PUT", body)
//                .addHeader("authtoken", authtoken)
//                .addHeader("Accept", "application/vnd.manageengine.sdp.v3+json")
//                .build();
//        logger.info("Calling put service: {} ,\t input_file:{}", serviceAddress + "/api/v3/requests/" + id + "/upload", input_file.getName());
//        okhttp3.Response response = client.newCall(request).execute();
//

    /// /        if (response.code() == 200)
//        if (response.body() != null)
//            outPut = response.body().string();
//
//        logger.info("Response code:{} ,\tresponse body:{}  ,\tresponse headers:{}", response.code(), outPut, response.headers().toString().replace("\n", ",\t"));
//        return new Response(response.code(), outPut, "", null, "", "", "");
//    }
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
