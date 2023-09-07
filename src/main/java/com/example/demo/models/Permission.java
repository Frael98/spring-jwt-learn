package com.example.demo.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    
    ADMIN_READ("ADMIN:READ");

    @Getter
    private final String permission;
}
