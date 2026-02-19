package com.hotel.app.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RegistroPulizieResponse {
    private Long id;
    private Long cameraId;
    private String cameraNumero;
    private Long personaleId;
    private String personaleUsername;
    private LocalDateTime dataOra;
    private String note;
}