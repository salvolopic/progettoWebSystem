package com.hotel.app.dto.response;

import java.time.LocalDateTime;

import com.hotel.app.entity.enums.Ruolo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UtenteResponse {

    private Long id;
    private String username;
    private String nome;
    private String cognome;
    private String email;
    private Ruolo ruolo;
    private Boolean attivo;
    private LocalDateTime dataRegistrazione;
    private Long strutturaAssegnataId;
    private String strutturaAssegnataNome;
}
