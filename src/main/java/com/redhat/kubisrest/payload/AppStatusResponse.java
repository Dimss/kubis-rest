package com.redhat.kubisrest.payload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;

import java.time.Instant;

public class AppStatusResponse {

    private String name;
    private String version;
    private Instant time;
    private String artifact;
    private String group;

    public AppStatusResponse(BuildProperties buildProperties) {
        this.name = buildProperties.getName();
        this.version = buildProperties.getVersion();
        this.time = buildProperties.getTime();
        this.artifact = buildProperties.getArtifact();
        this.group = buildProperties.getGroup();
    }
}
