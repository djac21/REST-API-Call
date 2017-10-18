package com.dj.zerionmanageapp;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccessToken {

    public static String getToken(String clientKey, String clientSecret, String URL) {

        String token = "";

        Map<String, Object> firstHeader = new HashMap<>();
        firstHeader.put("typ", "JWT");
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256, null, null, null,
                null, null, null, null, null, null, null, firstHeader, null);

        JWSSigner jwsSigner = new MACSigner(clientSecret);

        JWTClaimsSet claimsSet = new JWTClaimsSet();
        claimsSet.setClaim("iss", clientKey);
        claimsSet.setAudience(URL);
        claimsSet.setExpirationTime(DateUtils.addMinutes(new Date(), 5));
        claimsSet.setIssueTime(new Date());

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        try {
            signedJWT.sign(jwsSigner);
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        String jwt = signedJWT.serialize();

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(URL);
            httpPost.setHeader("Content-Type",
                    "application/x-www-form-urlencoded");
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type",
                    "urn:ietf:params:oauth:grant-type:jwt-bearer"));
            params.add(new BasicNameValuePair("assertion", jwt));
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            token = responseBody.substring(17, 57);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return token;
    }
}