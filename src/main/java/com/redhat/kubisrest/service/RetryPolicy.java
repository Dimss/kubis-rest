package com.redhat.kubisrest.service;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class RetryPolicy {
    private int sequence = 0;

    public int getSequence() {
        return sequence;
    }

    public void setSequence() {
        if (sequence == 2) sequence = 0;
        else sequence ++;
    }
}
