package com.example.truecallertest.models;

import com.android.volley.VolleyError;

/**
 * Created by salil on 13/8/15.
 */
public class Error {

    public static final int ERROR_PARSING = 1;
    public static final int ERROR_INVALID_RESPONSE = 2;
    public static final int ERROR_NETWORK = 11;

    private final String ERROR_MESSAGE_PARSING = "Error Occured While Parsing.";
    private final String ERROR_MESSAGE_INVALID_RESPONSE = "Invalid Response";
    private final String ERROR_MESSAGE_NETWORK = "Internet Connection Not Available.";
    private final String ERROR_MESSAGE_UNKNOWN = "Oops! Something went wrong.";

    private int errorCode = -1;
    private String errorMessage = null;

    public Error(int errorCode) {
        this.errorCode = errorCode;

        if (errorCode == ERROR_PARSING) {
            errorMessage = ERROR_MESSAGE_PARSING;
        } else if (errorCode == ERROR_NETWORK) {
            errorMessage = ERROR_MESSAGE_NETWORK;
        } else {
            errorMessage = ERROR_MESSAGE_UNKNOWN;
        }
    }

    public Error(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Error(VolleyError volleyError) {
        this.errorMessage = volleyError.getMessage();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return errorMessage;
    }
}
