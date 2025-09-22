package org.example.todoproject.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdService {

    public String randomID() {
        return UUID.randomUUID().toString();
    }
}
