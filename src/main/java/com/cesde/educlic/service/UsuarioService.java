package com.cesde.educlic.service;

import com.cesde.educlic.dto.UsuarioDto;
import com.cesde.educlic.model.entity.Usuario;
import com.cesde.educlic.model.enums.Rol;
import com.cesde.educlic.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario guardarUsuario(UsuarioDto dto) {
        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .rol(dto.getRol() != null ? dto.getRol() : Rol.CLIENTE)
                .build();
        
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Long id, UsuarioDto dto) {
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setNombre(dto.getNombre());
            usuarioExistente.setEmail(dto.getEmail());
            
            // Actualizar contraseña solo si viene provista
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                usuarioExistente.setPassword(dto.getPassword());
            }
            
            if (dto.getRol() != null) {
                usuarioExistente.setRol(dto.getRol());
            }
            
            return usuarioRepository.save(usuarioExistente);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con el ID: " + id));
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}