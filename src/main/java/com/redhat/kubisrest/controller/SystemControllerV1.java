package com.redhat.kubisrest.controller;

import com.redhat.kubisrest.payload.AppStatusResponse;
import com.redhat.kubisrest.payload.HostMetadataResponse;
import com.redhat.kubisrest.payload.ResponsePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
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
            ip = Inet4Address.getLocalHost();
            return ResponseEntity
                    .ok()
                    .header("content-type", "application/json")
                    .body((new ResponsePayload(new HostMetadataResponse(ip.getHostName())).getJsonPayload()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
