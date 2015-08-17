package com.plastprod.plastprodapp;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Laurent on 12/06/2015.
 */
public class Cryptage {

    private static int PW_HASH_ITERATION_COUNT = 5000;
    private static MessageDigest md;

    private String motDePasse;
    private String salt;

    public Cryptage(String motDePasse, String salt) {
        this.motDePasse = motDePasse;
        this.salt = salt;
    }

    public String CrypterDonnees(){

        String result = "";

        try
        {
            md = MessageDigest.getInstance("SHA-512");
            result = hashPw(this.motDePasse, this.salt);
        }
        catch (NoSuchAlgorithmException e) {
            Log.d("Erreur de cryptage", "Message" + e.getMessage());
        }

        return result;
    }

    private static String hashPw(String pw, String salt) {
        byte[] bSalted;
        byte[] bPw;
        String salted;

        salted = pw + "{" + salt + "}";

        try {
            bSalted = salted.getBytes("UTF-8");
            bPw = pw.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding", e);
        }

        byte[] digest = run(bSalted);

        for (int i = 1; i < PW_HASH_ITERATION_COUNT; i++) {
            digest = run(concatener(digest, bSalted));
        }

        return Base64.encodeToString(digest, Base64.NO_WRAP); //encodeBytes(digest);
    }

    private static byte[] run(byte[] input) {
        md.update(input);
        return md.digest();
    }

    private static byte[] run2(byte[] input, byte[] salt) {
        md.update(input);
        return md.digest(salt);
    }

    private static byte[] concatener(byte[] one, byte[] two){

        byte[] combine = new byte[one.length + two.length];

        System.arraycopy(one,0,combine,0         ,one.length);
        System.arraycopy(two, 0, combine, one.length, two.length);

        return combine;
    }

    public String uniqid(String prefix,boolean more_entropy) {

        long time = System.currentTimeMillis();
        //String uniqid = String.format("%fd%05f", Math.floor(time),(time-Math.floor(time))*1000000);
        //uniqid = uniqid.substring(0, 13);
        String uniqid = "";

        if(!more_entropy) {
            uniqid = String.format("%s%08x%05x", prefix, time/1000, time);
        }
        else {
            SecureRandom sec = new SecureRandom();
            byte[] sbuf = sec.generateSeed(8);
            ByteBuffer bb = ByteBuffer.wrap(sbuf);

            uniqid = String.format("%s%08x%05x", prefix, time/1000, time);
            uniqid += "." + String.format("%.8s", ""+bb.getLong()*-1);
        }

        return uniqid ;
    }

    public static String md5(String input) throws NoSuchAlgorithmException {
        String result = input;
        if(input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while(result.length() < 32) { //40 for SHA-1
                result = "0" + result;
            }
        }
        return result;
    }
}
