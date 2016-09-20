/*
 * University of L'Aquila 2015/2016
 * BiblioManager project
 * Authors: Vincenzo Lanzieri, Angelo Iezzi
 * Professor: Giuseppe Della Penna
 */
package it.univaq.iw.framework.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
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

    /**
     * Verifica e sanitizza la stringa passata
     *
     * @param string Stringa da veri
     * @return string
     */
    public static String checkString(String string) {
        if (isNullOrEmpty(string)) {
            //string = string.toLowerCase();
            string = string.trim();
        }
        return string;
    }

    /**
     * Verifica se una stringa è vuota o nulla
     *
     * @param var Stringa da verificare
     * @return boolean
     */
    public static boolean isNullOrEmpty(final String var) {
        return var == null || var.trim().isEmpty();
    }

    public static String getArrayParameter(Map<String, String> list, String paramName){
        String result = null;
        if(list != null && !isNullOrEmpty(paramName) && !list.isEmpty() && list.containsKey(paramName)){
            result = list.get(paramName);
        }
        return result;
    }
    
    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public static boolean isDate(String str)
    {
        return str.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}");  //match a number with optional '-' and decimal.
    }
    
    /**
     * Verifica la correttezza dell'email
     *
     * @param email Stringa che rappresenta l'email
     * @return boolean
     */
    public static boolean checkEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        email = email.toLowerCase();
        Pattern pattern = Pattern.compile("(([a-z._-]+)|([0-9._-]+[a-z]|[a-z._-]+[0-9]+[0-9a-z._-]*))@[a-z]+.[a-z]{2,4}");
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    /**
     * Cripta le password
     *
     * @param password
     * @return password codificata
     * @throws NoSuchAlgorithmException
     */
    public static String encryptPassword(String password) throws NoSuchAlgorithmException {
        byte[] salt = getSalt();
        return get_SHA_256_SecurePassword(password, salt);
    }

    /**
     * Esegue la criptazione della password
     *
     * @param passwordToHash
     * @param salt
     * @return
     * @throws NoSuchAlgorithmException
     */
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

    /**
     *
     * @return @throws NoSuchAlgorithmException
     */
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    /**
     * Funzione per l'invio della email
     *
     * @param emailTo
     * @param text
     * @throws MessagingException
     */
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

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    /**
     * Algoritmo SHA1
     *
     * @param text
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String SHA1(String text)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
}
