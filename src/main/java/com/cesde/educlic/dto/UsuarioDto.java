package com.cesde.educlic.dto;

import com.cesde.educlic.model.enums.Rol;
import lombok.Data;

@Data
public class UsuarioDto {
    private String nombre;
    private String email;
    private String password;
    private Rol rol;
}