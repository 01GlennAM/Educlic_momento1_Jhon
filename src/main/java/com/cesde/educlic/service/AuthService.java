package com.cesde.educlic.service;

import com.cesde.educlic.dto.UsuarioDto;
import com.cesde.educlic.model.entity.Usuario;
import com.cesde.educlic.model.enums.Rol;
import com.cesde.educlic.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario registrar(UsuarioDto dto) {
        // Mapeamos el DTO a la Entidad Usuario para guardarla en la base de datos
        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .password(dto.getPassword()) // Recuerda encriptarla con BCrypt si te lo han enseñado en clase
                .rol(dto.getRol() != null ? dto.getRol() : Rol.CLIENTE)
                .build();

        return usuarioRepository.save(usuario);
    }

    public Usuario login(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getPassword().equals(password)) {
                return usuario;
            }
        }
        throw new RuntimeException("Credenciales inválidas");
    }
}