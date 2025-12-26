package com.project.moneymanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private static final String BREVO_URL =
            "https://api.brevo.com/v3/smtp/email";
    private final RestTemplate restTemplate;
    @Value("${spring.mail.password}")
    private String apiKey;

    public void sendEmail(
            String toEmail,
            String subject,
            String htmlContent) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);
        String body = String.format("""
                                            {
                                              "sender": {
                                                "name": "%s",
                                                "email": "%s"
                                              },
                                              "to": [
                                                { "email": "%s" }
                                              ],
                                              "subject": "%s",
                                              "htmlContent": "%s"
                                            }
                                            """,
                                    "Money Manager",
                                    "priyanshijindal2123@gmail.com",
                                    toEmail,
                                    subject,
                                    htmlContent.replace("\"", "\\\"")
        );
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(
                BREVO_URL,
                request,
                String.class
        );
    }
}
