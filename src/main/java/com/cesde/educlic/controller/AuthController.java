package com.cesde.educlic.controller;

import com.cesde.educlic.dto.UsuarioDto;
import com.cesde.educlic.model.entity.Usuario;
import com.cesde.educlic.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@RequestBody UsuarioDto usuarioDto) {
        Usuario nuevoUsuario = authService.registrar(usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody UsuarioDto usuarioDto) {
        try {
            // Extraemos email y password del DTO que envía Postman
            Usuario usuarioLogueado = authService.login(usuarioDto.getEmail(), usuarioDto.getPassword());
            return ResponseEntity.ok(usuarioLogueado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}