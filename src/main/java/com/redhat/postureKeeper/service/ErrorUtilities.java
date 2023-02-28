package com.redhat.postureKeeper.service;

public class ErrorUtilities {
   
    public static String getErrorJson(String message) {
        if(message == null ){
            return "{}";
        }
        return String.format("{ \"message\": \"%s\" }", message);
    } 
}
