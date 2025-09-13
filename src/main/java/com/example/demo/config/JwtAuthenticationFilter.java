/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * Filtra todas las peticiones
 * @author Frael
 */
@Component // Esta anotación marca la clase como un componente de Spring,
// lo que significa que Spring la administrará y la registrará en el contexto de
// la aplicación.
// Esto permite que otras partes de la aplicación puedan inyectar instancias de
// esta clase como un bean.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // extends de -> para crear filtros que se ejecutan una vez por cada solicitud HTTP entrante

    // Este campo se inyectará automáticamente en el constructor de la clase debido
    // a la anotación @Component
    private final JwtService jwtService;
    //
    private final UserDetailsService userDetailsService;

    /**
     * Encargado de gestionar y filtrar los intentos de autenticación.
     * Validando los JSONWebTokens presentes en las cabeceras HTTP de cada solicitud
     * Si son validos permite que el usuario pase, otorgándole los permisos correctos. Si no, lo detiene en seco.
     */
    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Se obtiene el encabezado "Authorization" de la solicitud HTTP.
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userName;
        
        // Si el header es nulo o no empieza con Barer retorna
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Se extrae el token JWT eliminando la parte "Bearer " del encabezado.
        jwt = authHeader.substring(7);
        // Se extrae el nombre de usuario (u otra información relevante) del token JWT
        userName = jwtService.extractUserName(jwt);
        // Si el usuario no es nulo y no existe token de Authenticaction en el Contexto de Seguridad de Spring
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

            try {
                // Si token es valido
                if (jwtService.isTokenValid(jwt, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    //
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    // seteamos el token de Authentication en el Contexto de Seguridad de Spring
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            } catch (ExpiredJwtException e) {
                System.out.println("Token ha expirado: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token JWT ha expirado");
            }

        }
        //
        filterChain.doFilter(request, response);
    }

}
