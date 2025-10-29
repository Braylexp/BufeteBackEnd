package com.bufete.backend.Dtos.usuario;

import java.time.Instant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
public class UsuarioDTO {
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String apellido;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 200, message = "El email no puede exceder 200 caracteres")
    private String email;
    
    @Size(max = 100, message = "La identificación no puede exceder 100 caracteres")
    private String identificacion;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
    
    private String direccion;
    
    @NotNull(message = "El rol es obligatorio")
    private Long rolId;
    
    private String rolNombre;
    
    private Boolean activo;
    private Boolean nuevoUsuario;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastLogin;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String nombre, String apellido, String email, Long rolId) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rolId = rolId;
    }
    
}