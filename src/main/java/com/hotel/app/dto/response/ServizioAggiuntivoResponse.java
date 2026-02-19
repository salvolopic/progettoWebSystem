package com.hotel.app.dto.response;

import com.hotel.app.entity.enums.TipoServizio;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ServizioAggiuntivoResponse {
    private Long id;
    private TipoServizio tipo;
    private String nome;
    private String descrizione;
    private BigDecimal prezzo;
    private Boolean perGiorno;
    private Long strutturaId;
    private String strutturaNome;
    private Boolean attivo;

}