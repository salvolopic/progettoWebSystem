package com.hotel.app.dto.response;


import com.hotel.app.entity.enums.StatoCamera;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CameraResponse {
    private Long id;
    private String numero;
    private Integer piano;
    private Long strutturaId;
    private String strutturaNome;
    private Long tipoCameraId;
    private String tipoCameraNome;
    private String tipoCameraDescrizione;
    private Integer capacitaMinima;
    private Integer capacitaMassima;
    private BigDecimal prezzoBase;
    private Boolean haVista;
    private Boolean haBalcone;
    private Boolean haAriaCondizionata;
    private StatoCamera stato;
    private String note;
    private Boolean attiva;
}
