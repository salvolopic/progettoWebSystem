package com.hotel.app.dto.response;

import java.time.LocalDate;

import com.hotel.app.entity.enums.TipoDocumento;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OspiteResponse {

    private Long id;
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String luogoNascita;
    private String cittadinanza;
    private Boolean isCapogruppo;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
}
