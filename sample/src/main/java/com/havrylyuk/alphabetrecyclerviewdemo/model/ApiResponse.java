package com.havrylyuk.alphabetrecyclerviewdemo.model;


/**
 * Created by Igor Havrylyuk on 08.03.2017.
 */

public class ApiResponse {

    private Status status;

    public ApiResponse() {
    }

    public Status getStatus() {
        return status;
    }

    public static class  Status {

        private String message;
        private int value;

        public Status() {
        }

        public String getMessage() {
            return message;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Error Code " + value + ':' + message;
        }
    }

}
