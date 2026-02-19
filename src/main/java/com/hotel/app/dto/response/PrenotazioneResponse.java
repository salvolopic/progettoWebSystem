package com.hotel.app.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.hotel.app.entity.enums.StatoPrenotazione;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PrenotazioneResponse {

    private Long id;
    private String codicePrenotazione;
    private Long clienteId;
    private String clienteNome;
    private Long cameraId;
    private String cameraNumero;
    private String tipoCameraNome;
    private String strutturaNome;
    private LocalDate dataCheckIn;
    private LocalDate dataCheckOut;
    private LocalDateTime dataCheckInEffettivo;
    private LocalDateTime dataCheckOutEffettivo;
    private StatoPrenotazione stato;
    private Integer numeroOspiti;
    private BigDecimal costoCamera;
    private BigDecimal costoServizi;
    private BigDecimal costoTotale;
    private List<OspiteResponse> ospiti;
    private LocalDateTime dataCreazione;
}
