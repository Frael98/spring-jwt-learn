/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 *
 * @author Frael
 */
@Service
public class JwtService {
    
    // Llave secreta
    private static final String SECRET_KEY = "ThisisjustanothersadsongIcantdenythatIneedoneAndthattheworldisalways";
    
    /**
     * Extraer el usuario del JWT
     * 
     * @param token
     * @return String - devuelve el usuario
     */
    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extrae todos los Claims del JWT
     * @param token
     * @return Claims
     */
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    // Esto define un método público llamado extractClaim que es genérico. 
    // El <T> antes del nombre del método indica que el método es genérico y devolver un valor de tipo T generico. 
    /**
     * Extrae el Claims del JWT
     * 
     * @param token Token
     * @param claimsResolver Callback
     * @return T
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Codifica la llave a Base64
     * 
     * @return Key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * Genera el JWT
     * 
     * Un JWT esta constituido por 3 partes: Header, Payload y Signature
     * todo esto codificado y separados por (.)
     * 
     * @param extraClaims
     * @param userDetails
     * @return
     */
    public String generateToken(
        Map<String, Object> extraClaims,
        UserDetails userDetails
    ){
        
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 *24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }
    
    /**
     * 
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }
    
    /**
     * Extrae la fecha de expiración del JWT
     * 
     * @param token
     * @return
     */
    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Valida si el token esta expirado
     * devuelve True si lo esta caso contraio False
     * 
     * @param token
     * @return boolean
     */
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Verifica si el JWT es valido
     * 
     * @param token
     * @param userDetails
     * @return boolean
     */
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUserName(token);
        System.out.println("Usuario es: "+username);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    
}
