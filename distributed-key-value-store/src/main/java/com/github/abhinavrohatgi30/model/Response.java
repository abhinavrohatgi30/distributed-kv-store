package com.github.abhinavrohatgi30.model;

public class Response {

    private int statusCode;
    private String message;

    public Response(int statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }


    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
