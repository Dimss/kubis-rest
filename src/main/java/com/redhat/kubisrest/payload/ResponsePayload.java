package com.redhat.kubisrest.payload;

import com.google.gson.GsonBuilder;

public class ResponsePayload {

    private String status;
    private Object data;

    public ResponsePayload(Object data) {
        this.status = "ok";
        this.data = data;
    }

    public ResponsePayload(String status, Object data) {
        this.status = status;
        this.data = data;
    }

    public String getJsonPayload() {
        return (new GsonBuilder())
                .create()
                .toJson(this);
    }


}
