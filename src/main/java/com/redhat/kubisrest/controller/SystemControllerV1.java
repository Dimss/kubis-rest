package com.redhat.kubisrest.controller;

import com.redhat.kubisrest.payload.AppStatusResponse;
import com.redhat.kubisrest.payload.HostMetadataResponse;
import com.redhat.kubisrest.payload.ResponsePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/v1/system")
public class SystemControllerV1 {
    @Autowired
    BuildProperties buildProperties;

    @Autowired
    private Environment environment;

    @GetMapping("/version")
    public ResponseEntity status() {
        return ResponseEntity
                .ok()
                .header("content-type", "application/json")
                .body((new ResponsePayload(new AppStatusResponse(buildProperties)).getJsonPayload()));
    }

    @GetMapping("/metadata")
    public ResponseEntity hostMetadata() {
        InetAddress ip;
        try {
            String cbTest = environment.getProperty("CB-MODE");
            ip = Inet4Address.getLocalHost();
            String hostname = ip.getHostName();
            // For Circuit breaker test, return HTTP 500 if the request
            // arriving to the first instance in the stateful set (should be end with 0)
            if (cbTest != null && hostname.substring(hostname.length() -1).equals("0"))
                    throw new RuntimeException("This is error generator method");
            return ResponseEntity
                    .ok()
                    .header("content-type", "application/json")
                    .body((new ResponsePayload(new HostMetadataResponse(hostname)).getJsonPayload()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/block")
    public ResponseEntity blockRequest() {
        try {
            Thread.sleep(10*1000);
            return ResponseEntity
                    .ok()
                    .header("content-type", "application/json")
                    .body((new ResponsePayload().getJsonPayload()));
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/error")
    public ResponseEntity error() {
        throw new RuntimeException("This is error generator method");
    }
}
