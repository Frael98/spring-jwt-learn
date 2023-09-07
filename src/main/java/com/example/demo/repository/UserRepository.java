/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.demo.repository;

import com.example.demo.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Frael
 */
public interface UserRepository extends JpaRepository<User, Integer>{
    
    /**
     * Retorno el usuario segun el usuario
     * @param usuario
     * @return 
     */
    Optional<User> findByUsuario(String usuario);
    
}
