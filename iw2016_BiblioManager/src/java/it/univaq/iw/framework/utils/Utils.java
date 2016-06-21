/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.framework.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Vincenzo Lanzieri
 */
public class Utils {

    public static String checkString(String string) {
        if (string != null && !string.isEmpty()) {
            string = string.toLowerCase();
            string = string.trim();
        }
        return string;
    }

    public static boolean checkEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        email = email.toLowerCase();
        Pattern pattern = Pattern.compile("(([a-z._-]+)|([0-9._-]+[a-z]|[a-z._-]+[0-9]+[0-9a-z._-]*))@[a-z]+.[a-z]{2,4}");
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    public static String encryptPassword(String password) throws NoSuchAlgorithmException {
        byte[] salt = getSalt();
        return get_SHA_256_SecurePassword(password, salt);
    }

    private static String get_SHA_256_SecurePassword(String passwordToHash, byte[] salt) throws NoSuchAlgorithmException {
        String generatedPassword = null;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] bytes = md.digest(passwordToHash.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        generatedPassword = sb.toString();
        return generatedPassword;
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    public static void sendEmail(String emailTo, String text) throws MessagingException {
        if (checkEmail(emailTo)) {

            // Sender's email ID needs to be mentioned
            String from = "web@gmail.com"; //TODO

            String host = "localhost";

            Properties properties = System.getProperties();

            properties.setProperty("mail.smtp.host", host);

            Session session = Session.getDefaultInstance(properties);

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));

            message.setSubject("Reset password for Bibliomanager");

            message.setText(text);

            Transport.send(message);

        }
    }
}
