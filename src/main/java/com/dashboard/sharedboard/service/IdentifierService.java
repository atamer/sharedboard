package com.dashboard.sharedboard.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class IdentifierService {

    private final AtomicLong idLong;

    public IdentifierService() {
        this.idLong = new AtomicLong();
    }

    public String createUniqueId() {
        return String.valueOf(idLong.incrementAndGet());
    }


}
