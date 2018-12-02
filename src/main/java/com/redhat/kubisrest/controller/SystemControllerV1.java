package com.redhat.kubisrest.controller;

import com.redhat.kubisrest.payload.AppStatusResponse;
import com.redhat.kubisrest.payload.HostMetadataResponse;
import com.redhat.kubisrest.payload.ResponsePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ResponseEntity hostMetadata( @RequestHeader(value="X-APP-USER",defaultValue = "") String xAppUser) {
        InetAddress ip;
        try {
            ip = Inet4Address.getLocalHost();
            String hostname = ip.getHostName();
            // For Circuit breaker test, return HTTP 502 if the request
            // arriving to the first instance in the stateful set (should be end with 0)
            if (hostname.equals(xAppUser))
                return new ResponseEntity<>(null, HttpStatus.BAD_GATEWAY);
//                    throw new RuntimeException("This is error generator method");
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
