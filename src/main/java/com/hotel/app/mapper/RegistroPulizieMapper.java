package com.hotel.app.mapper;

import com.hotel.app.dto.response.RegistroPulizieResponse;
import com.hotel.app.entity.RegistroPulizie;

public class RegistroPulizieMapper{

    public static RegistroPulizieResponse toResponse(RegistroPulizie registro){
        return RegistroPulizieResponse.builder()
                .id(registro.getId())
                .cameraId(registro.getCamera().getId())
                .cameraNumero(registro.getCamera().getNumero())
                   .personaleId(registro.getPersonale().getId())
                .personaleUsername(registro.getPersonale().getUsername())
                .dataOra(registro.getDataOra())
                .note(registro.getNote())
                .build();
    }
}