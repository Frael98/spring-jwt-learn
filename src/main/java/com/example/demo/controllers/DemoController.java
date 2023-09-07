package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoController {
    
    @GetMapping("/getTest")
    public ResponseEntity<String> getMessage(){
        return ResponseEntity.ok("Este mensaje puede estar bloqueado!");
    }

}
