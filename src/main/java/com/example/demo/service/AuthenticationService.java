package com.example.demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

//import javax.management.relation.Role;
//import org.springframework.security.core.userdetails.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;
import com.example.demo.models.User;
import com.example.demo.models.AuthenticationRequest;
import com.example.demo.models.AuthenticationResponse;
import com.example.demo.models.RegisterRequest;
import com.example.demo.models.Role;

import lombok.RequiredArgsConstructor;

/**
 * Servicio Authentication
 * 
 * @author Frael
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    /**
     * Registra usuario y genera JWT Token
     * 
     * @param request
     * @return AuthenticationResponse
     */
    public AuthenticationResponse registerUser(RegisterRequest request){
        var user = User.builder()
            .nombres(request.getNombre())
            .apellidos(request.getApellido())
            .usuario(request.getUsuario())
            .contrasenia(encoder.encode(request.getContrasenia()))
            .rol(Role.USER)
            .build();

        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwt).build();
    }

    /**
     * Para la autenticación del usuario al sistema
     * 
     * @param request
     * @return AuthenticationResponse
     */
    public AuthenticationResponse authentication(AuthenticationRequest request){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsuario(), request.getContrasenia())
        );
        var user = userRepository.findByUsuario(request.getUsuario()).orElseThrow();
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwt).build();

    }
}
