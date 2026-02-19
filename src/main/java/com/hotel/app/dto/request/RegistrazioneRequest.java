package com.hotel.app.dto.request;

import com.hotel.app.entity.enums.Ruolo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrazioneRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String nome;
    @NotBlank
    private String cognome;
    @NotBlank
    @Email
    private String email;
    @NotNull
    private Ruolo ruolo;
    private Long strutturaAssegnataId;
}
