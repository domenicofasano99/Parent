package com.bok.parent.utils;

import com.bok.parent.helper.EmailHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Objects;

@Component
public class ValidationUtils {

    @Autowired
    EmailHelper emailHelper;

    public static void nonNull(Object o) {
        nonNull(o, "Parameter cannot be null");
    }


    public static void nonNull(Object object, String errorMessage) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void checkEmail(String email, String errorMessage) {
        if (!validateEmail(email)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static Boolean validateEmail(String email) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://emailvalidator.russi.ovh/validate";
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // create a post object
        EmailValidationRequest request = new EmailValidationRequest(email);
        // build the request
        HttpEntity<EmailValidationRequest> entity = new HttpEntity<>(request, headers);
        // send POST request
        String response = restTemplate.postForObject(url, entity, String.class);
        if (Objects.nonNull(response)) {
            return response.equalsIgnoreCase("True");
        }
        return true;
    }

    public static class EmailValidationRequest {
        public String email;

        public EmailValidationRequest(String email) {
            this.email = email;
        }
    }
}
