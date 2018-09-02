package com.github.abhinavrohatgi30.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class ClientUtils {
    public static String executeGetRequest(String url, Boolean isRouted) throws IOException {
        StringBuilder response = new StringBuilder();
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(url);
        getRequest.getParams().setBooleanParameter("isRouted", isRouted);
        HttpResponse httpResponse = client.execute(getRequest);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (HttpStatus.SC_OK == statusCode) {
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(
                            httpResponse.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } else {
            return null;
        }
    }

    public static String executePostRequest(String url, String body, Boolean isRouted) throws IOException {
        StringBuilder response = new StringBuilder();
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost postRequest = new HttpPost(url);
        postRequest.getParams().setBooleanParameter("isRouted", isRouted);
        postRequest.setEntity(new StringEntity(body));
        HttpResponse httpResponse = client.execute(postRequest);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (HttpStatus.SC_OK == statusCode) {
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(
                            httpResponse.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } else {
            return null;
        }
    }
}
