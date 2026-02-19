package com.hotel.app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrenotazioneRequest {
    @NotNull
    private Long clienteId;
    @NotNull
    private Long cameraId;
    @NotNull
    private LocalDate dataCheckIn;
    @NotNull
    private LocalDate dataCheckOut;
    @NotNull
    private Integer numeroOspiti;
}
