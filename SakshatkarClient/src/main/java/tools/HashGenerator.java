package tools;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class HashGenerator {

    public static String hash(String str)
    {
        String sha1 = "";
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(str.getBytes("UTF-8"));
            sha1 = byteToHex(md.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    public static String hash(byte[] arr){
        String sha1 = "";
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            sha1 = byteToHex(md.digest(arr));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}