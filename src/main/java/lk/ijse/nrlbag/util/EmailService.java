package lk.ijse.nrlbag.util;


import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    // sender email and app password
    // it is generated in google account by app password
    private static final String username = "lakmiindu11@gmail.com";
    private static final String password = "kndi lxft bdgr oxcz";

    // this method send the user's password to their email
    // this used when click on the "Forget Password?"
    public static boolean sendPasswordRecovery(String email, String actualPassword) throws Exception{

        // SMTP setting required to connect with Gmail serever
        Properties prop = new Properties();
        // Gmail SMTP server
        prop.put("mail.smtp.host", "smtp.gmail.com");
        // TLS port
        prop.put("mail.smtp.port", "587");
        // Enable Authentication
        prop.put("mail.smtp.auth", "true");
        // enable TLS security
        prop.put("mail.smtp.starttls.enable", "true");

        // create mail session and provide email credentials
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
            // Gmail authentication using App password
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
                });

        try {
            // create a new email message
            Message message = new MimeMessage(session);
            // set sender email address
            message.setFrom(new InternetAddress(username));
            // set receiver email address
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            // set subject of the email
            message.setSubject("NRL Bag Management System - Tour Password Recovery");

            // email body content (HTML format)
            String htmlContent = "<h3>Password Recovery</h3>"
                    + "<p>You requested to retrieve your password. It is:</p>"
                    + "<h2 style='color:#d35400;'>" + actualPassword + "</h2>" // displayed in orange
                    + "<p>Please delete this email after logging in for security.</p>";

            // attach HTML content to the email
            message.setContent(htmlContent, "text/html");

            // send the email
            Transport.send(message);
            // email sent successfully
            return true;

        } catch (Exception e) {
            // if something went wrong at the sending time
            System.out.println(e.getMessage());
            return false;
        }
    }

}
