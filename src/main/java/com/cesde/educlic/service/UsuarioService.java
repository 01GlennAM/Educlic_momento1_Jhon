/* package com.cesde.educlic.service;

import com.cesde.educlic.dto.UsuarioDto;
import com.cesde.educlic.model.entity.Usuario;
import com.cesde.educlic.model.enums.Rol;
import com.cesde.educlic.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Método de negocio obligatorio: Si no existe, lanza excepción con mensaje claro
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El usuario con ID " + id + " no existe."));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No se encontró ningún usuario registrado con el correo: " + email));}

    public Usuario guardarUsuario(UsuarioDto dto) {
        // Regla de negocio: No dejar crear usuario sin nombre   
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
        throw new IllegalArgumentException("El nombre es obligatorio.");
    }

        // Regla de negocio: Validar si el email ya está registrado
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
        throw new IllegalArgumentException("Ya existe un usuario registrado con el correo: " + dto.getEmail());
    }
        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .rol(dto.getRol() != null ? dto.getRol() : Rol.CLIENTE)
                .build();
        
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Long id, UsuarioDto dto) {
        // Aquí reutilizamos la regla de negocio: si no existe, lanza la excepción automáticamente
        Usuario usuarioExistente = buscarPorId(id);

        usuarioExistente.setNombre(dto.getNombre());
        usuarioExistente.setEmail(dto.getEmail());
        
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuarioExistente.setPassword(dto.getPassword());
        }
        
        if (dto.getRol() != null) {
            usuarioExistente.setRol(dto.getRol());
        }
        
        return usuarioRepository.save(usuarioExistente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        try {
        // Validamos y eliminamos a través del servicio
            usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario con ID " + id + " eliminado exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
}    */

package com.cesde.educlic.service;

import com.cesde.educlic.dto.UsuarioDto;
import com.cesde.educlic.model.entity.Usuario;
import com.cesde.educlic.model.enums.Rol;
import com.cesde.educlic.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El usuario con ID " + id + " no existe."));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No se encontró ningún usuario registrado con el correo: " + email));
    }

    public Usuario guardarUsuario(UsuarioDto dto) {
        // Regla de negocio: No dejar crear usuario sin nombre
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        // Regla de negocio: Límite de caracteres para el nombre
        if (dto.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre no puede superar los 50 caracteres.");
    }

        // Regla de negocio: Validar si el email ya está registrado
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con el correo: " + dto.getEmail());
        }

        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .rol(dto.getRol() != null ? dto.getRol() : Rol.CLIENTE)
                .build();
        
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Long id, UsuarioDto dto) {
        Usuario usuarioExistente = buscarPorId(id);

        usuarioExistente.setNombre(dto.getNombre());
        usuarioExistente.setEmail(dto.getEmail());
        
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuarioExistente.setPassword(dto.getPassword());
        }
        
        if (dto.getRol() != null) {
            usuarioExistente.setRol(dto.getRol());
        }
        
        return usuarioRepository.save(usuarioExistente);
    }

    // Método de negocio puro para eliminar (sin anotaciones HTTP)
    public void eliminarUsuario(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}