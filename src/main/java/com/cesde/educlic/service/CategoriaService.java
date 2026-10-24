package com.cesde.educlic.service;

import com.cesde.educlic.dto.CategoriaDto;
import com.cesde.educlic.model.entity.Categoria;
import com.cesde.educlic.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    public Categoria guardarCategoria(CategoriaDto dto) {
        Categoria categoria = Categoria.builder()
                .nombre(dto.getNombre())
                .build();
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizarCategoria(Long id, CategoriaDto dto) {
        return categoriaRepository.findById(id).map(categoriaExistente -> {
            categoriaExistente.setNombre(dto.getNombre());
            return categoriaRepository.save(categoriaExistente);
        }).orElseThrow(() -> new RuntimeException("Categoría no encontrada con el ID: " + id));
    }

    public void eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }
}