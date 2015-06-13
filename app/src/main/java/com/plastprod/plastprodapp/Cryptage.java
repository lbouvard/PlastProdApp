package com.plastprod.plastprodapp;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        System.arraycopy(two,0,combine,one.length,two.length);

        return combine;
    }
}
