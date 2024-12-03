package com.vlada.lab3;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class SimpleSMTPClient {

    public static void main(String[] args) {
        // SMTP server configuration
        String smtpHost = "smtp.gmail.com";
        String smtpPort = "587";
        String username = "labprtest@gmail.com";
        String password = "whtc wsid vdcr zvaj";

        // Recipient's email and message details
        String recipientEmail = "vlada.musin@gmail.com";
        String subject = "Test Email from Java";
        String body = "This is a test email sent using JavaMail.";

        // Set properties for SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);

        // Create a session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

