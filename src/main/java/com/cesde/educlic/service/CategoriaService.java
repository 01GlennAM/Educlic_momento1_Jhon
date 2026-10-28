package com.cesde.educlic.service;

import com.cesde.educlic.dto.CategoriaDto;
import com.cesde.educlic.model.entity.Categoria;
import com.cesde.educlic.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    // Regla de negocio: Si no existe la categoría, lanza excepción clara (adiós a los vacíos silenciosos)
    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La categoría con el ID " + id + " no existe en el sistema."));
    }

    public Categoria guardarCategoria(CategoriaDto dto) {
        // Regla de negocio: El nombre es obligatorio
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio y no puede estar vacío.");
        }

        // Regla de negocio: Límite de caracteres para el nombre
        if (dto.getNombre().length() < 2 || dto.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre de la categoría debe tener entre 2 y 50 caracteres.");
        }

        Categoria categoria = Categoria.builder()
                .nombre(dto.getNombre())
                .build();
        
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizarCategoria(Long id, CategoriaDto dto) {
        // Reutilizamos la validación de existencia
        Categoria categoriaExistente = buscarPorId(id);

        // Regla de negocio: El nombre es obligatorio al actualizar
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio.");
        }

        // Regla de negocio: Límite de caracteres también al actualizar
        if (dto.getNombre().length() < 10 || dto.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre de la categoría debe tener entre 2 y 50 caracteres.");
        }

        categoriaExistente.setNombre(dto.getNombre());
        return categoriaRepository.save(categoriaExistente);
    }

    public void eliminarCategoria(Long id) {
        // Validamos que exista antes de eliminar
        Categoria categoria = buscarPorId(id);
        categoriaRepository.delete(categoria);
    }
}