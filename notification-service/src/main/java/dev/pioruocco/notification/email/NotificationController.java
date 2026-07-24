package dev.pioruocco.notification.email;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailNotificationRequest request) throws MessagingException {
        emailService.sendEmail(
                request.to(),
                request.username(),
                request.emailTemplate(),
                request.confirmationUrl(),
                request.activationCode(),
                request.subject()
        );
        return ResponseEntity.accepted().build();
    }
}
