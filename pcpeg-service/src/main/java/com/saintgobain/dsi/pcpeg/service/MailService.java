package com.saintgobain.dsi.pcpeg.service;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.saintgobain.dsi.pcpeg.config.PcpegProperties;

import lombok.RequiredArgsConstructor;

/**
 * The type Mail service.
 */
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender emailSender;
    private final PcpegProperties properties;

    @Async
    public void sendMail(String[] to, String object, String template) throws MessagingException {
        if (!properties.getMail().isEnabled()) {
            return;
        }
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
            message,
            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            StandardCharsets.UTF_8.name()
        );

        helper.setTo(to);
        final String html = template;
        helper.setText(html, true);
        helper.setSubject(object);
        helper.setFrom(properties.getMail().getFrom());
        emailSender.send(message);
    }

}
