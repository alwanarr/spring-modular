package com.spring.controller;

import com.spring.config.AuthorizationBrisurf;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    private static Logger log = LoggerFactory.getLogger(TestController.class);
    @Value("${brisurf.client.id}")
    private String clientId;

    @Value("${brisurf.client.secret}")
    private String clientSecret;

    private String pathUrlToken;
    @GetMapping("/call")
    public ResponseEntity<?> tests() throws Exception {
        String getAccessToken = AuthorizationBrisurf.getToken(pathUrlToken, clientId, clientSecret);
        log.info("getToken : {}", getAccessToken);

        String getDataTimestamp = AuthorizationBrisurf.getTimestamp();
        log.info("getTimeStamp : {}", getDataTimestamp);

        JSONObject parameters = new JSONObject();
        parameters.put("modul", "kbg"); // test dev 9930
        parameters.put("nomorRekeningPinjaman", "");
        parameters.put("digitalDocument", "WarkatBG");
        parameters.put("channelUniqueKeyId", "sp3100052233");
        parameters.put("channelUniqueKey", "Nomor SP3");
//        parameters.put("fidProgram", 104); // test dev 9930
//        parameters.put("offset", 1);
//        parameters.put("status", 1);

        String getBase64Signature = AuthorizationBrisurf.generateSignature("/v1/brisurf/getdigitaldocumenthistory",
                "POST", getAccessToken, getDataTimestamp, parameters.toString(), clientSecret);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("BRI-ExternalId", "123447193");
        headers.set("BRI-Timestamp", getDataTimestamp);
        headers.set("BRI-Signature", getBase64Signature);
        headers.set("Authorization", "Bearer " + getAccessToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<String>(parameters.toString(), headers);
//        https://sandbox.partner.api.bri.co.id/v1/brisurf/getdigitaldocumenthistory
        ResponseEntity<Map> responseEntity = restTemplate
                .exchange("https://sandbox.partner.api.bri.co.id/v1/brisurf/getdigitaldocumenthistory",
                        HttpMethod.POST,
                requestEntity, Map.class);
        return responseEntity;
    }
}
