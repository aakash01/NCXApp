package com.anutanetworks.ncxapp.services;

/**
 * Created by Aakash on 7/2/2015.
 */
public class ValidationUtils {

    public static boolean isNotNull(String text) {
        return text != null && text.trim().length() > 0 ? true : false;
    }
}
