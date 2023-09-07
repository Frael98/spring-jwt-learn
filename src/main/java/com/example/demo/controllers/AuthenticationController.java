/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.AuthenticationRequest;
import com.example.demo.models.AuthenticationResponse;
import com.example.demo.models.RegisterRequest;
import com.example.demo.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author Frael
 */
@RestController
@RequiredArgsConstructor //Generates a constructor with required arguments. Required arguments are final fields and fields
@RequestMapping("/api/authentication")
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;

    @PostMapping("/registrarse")
    public ResponseEntity<AuthenticationResponse> registrarse(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.registerUser(request));
    }

    @PostMapping("/autenticarse")
    public ResponseEntity<AuthenticationResponse> autenticarse(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authentication(request));
    }
}
