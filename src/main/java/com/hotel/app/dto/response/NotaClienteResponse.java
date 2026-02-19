package com.hotel.app.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NotaClienteResponse {
    private Long id;
    private Long prenotazioneId;
    private String clienteNome;
    private String clienteCognome;
    private String testo;
    private LocalDateTime dataCreazione;
    private Boolean lettaDalPersonale;
    private LocalDateTime dataLettura;
    private Long lettaDaId;
    private String lettaDaUsername;
}