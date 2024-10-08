package com.devteria.notification.service;

import com.devteria.notification.dto.request.EmailRequest;
import com.devteria.notification.dto.request.SendEmailRequest;
import com.devteria.notification.dto.request.Sender;
import com.devteria.notification.dto.response.EmailResponse;
import com.devteria.notification.exception.AppException;
import com.devteria.notification.exception.ErrorCode;
import com.devteria.notification.repository.httpclient.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;

    // apiKey lay tu web Brevo
    String apiKey = "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";

    public EmailResponse sendEmail(SendEmailRequest request) {
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder() // nguoi gui
                        .name("NamNgoc")
                        .email("onggia23197@gmail.com")
                        .build())
                .to(List.of(request.getTo())) // nguoi nhan
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try {
            // Dung Brevo de gen key roi moi gui duoc mail
            return emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e){
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
