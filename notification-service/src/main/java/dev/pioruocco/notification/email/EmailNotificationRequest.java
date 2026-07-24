package dev.pioruocco.notification.email;

public record EmailNotificationRequest(
        String to,
        String username,
        EmailTemplateName emailTemplate,
        String confirmationUrl,
        String activationCode,
        String subject
) {
}
