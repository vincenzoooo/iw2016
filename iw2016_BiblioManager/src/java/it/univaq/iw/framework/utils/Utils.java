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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if (!isNullOrEmpty(string)) {
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

    /**
     * Recupera un parametro da un Map altrimenti restituisce null
     *
     * @param list
     * @param paramName
     * @return
     */
    public static String getArrayParameter(Map<String, String> list, String paramName) {
        String result = null;
        if (list != null && !isNullOrEmpty(paramName) && !list.isEmpty() && list.containsKey(paramName)) {
            result = list.get(paramName);
        }
        return result;
    }

    /**
     * Verifica se una stringa è un numero
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Verifica se la stringa passata è una data valida del formato dd-mm-yyyy
     *
     * @param str
     * @return
     */
    public static boolean isDate(String str) {
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

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
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
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
}
