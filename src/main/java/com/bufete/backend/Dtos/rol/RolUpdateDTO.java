package com.bufete.backend.Dtos.rol;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
public class RolUpdateDTO{

    private Set<String> permisos;
}