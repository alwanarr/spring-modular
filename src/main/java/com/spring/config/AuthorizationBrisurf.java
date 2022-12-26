package com.spring.config;

import com.spring.controller.TestController;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class AuthorizationBrisurf {

    private static Logger log = LoggerFactory.getLogger(AuthorizationBrisurf.class);
    public static String generateSignature(String path, String verb, String token, String timestamp, String body,
                                           String secret) throws Exception {
        String payload = "path=" + path + "&verb=" + verb + "&token=Bearer " + token + "&timestamp=" + timestamp + "&body=" + body;System.out.println("Payload : " + payload);
        String signPaylaod = null;
        try {
            signPaylaod = hmacSHA256(payload, secret);
            System.out.println("generateSignature : " + signPaylaod);
        } catch (GeneralSecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return signPaylaod;
    }

    // HmacSHA256 implementation
    private static String hmacSHA256(String message, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
        return hash;
    }

    public static String getToken(String pathUrl, String clientId, String clientSecret) {
        final String uri = "https://sandbox.partner.api.bri.co.id/oauth/client_credential/accesstoken?grant_type=client_credentials";
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            System.out.println(uri);
//			List<Object> objects = new ArrayList();
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity,
                    String.class);
            log.info("response : {}", responseEntity.getBody());
            JSONObject jsonObject = new JSONObject(responseEntity.getBody());
            System.out.println("json : " +jsonObject);
            return jsonObject.getString("access_token");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String getTimestamp() {
        Date datetimeNow = new Date();
        DateFormat gmtFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        TimeZone gmtTime = TimeZone.getTimeZone("GMT");
        gmtFormat.setTimeZone(gmtTime);
        String RequestDate = gmtFormat.format(datetimeNow);
        return RequestDate;
    }

    public static int getRandomNumberInRange(int min, int max) {
        Integer randNum;
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        randNum = r.nextInt((max - min) + 1) + min;

        return randNum;
    }
}
