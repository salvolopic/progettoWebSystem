package com.hotel.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrutturaResponse {
    private Long id;
    private String nome;
    private String indirizzo;
    private String citta;
    private String provincia;
    private String cap;
    private String telefono;
    private String email;
    private String descrizione;
    private Integer stelle;
    private Boolean attiva;
}
