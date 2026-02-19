package com.hotel.app.mapper;

import com.hotel.app.dto.response.OspiteResponse;
import com.hotel.app.entity.Ospite;

public class OspiteMapper {

    public static OspiteResponse toResponse(Ospite ospite) {
        if (ospite == null) {
            return null;
        }

        return OspiteResponse.builder()
                .id(ospite.getId())
                .nome(ospite.getNome())
                .cognome(ospite.getCognome())
                .dataNascita(ospite.getDataNascita())
                .luogoNascita(ospite.getLuogoNascita())
                .cittadinanza(ospite.getCittadinanza())
                .isCapogruppo(ospite.getIsCapogruppo())
                .tipoDocumento(ospite.getTipoDocumento())
                .numeroDocumento(ospite.getNumeroDocumento())
                .build();
    }
}
