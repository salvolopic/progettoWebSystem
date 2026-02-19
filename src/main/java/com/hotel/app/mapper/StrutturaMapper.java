package com.hotel.app.mapper;

import com.hotel.app.dto.response.StrutturaResponse;
import com.hotel.app.entity.Struttura;

public class StrutturaMapper {

public static StrutturaResponse toResponse(Struttura struttura) {
    return StrutturaResponse.builder()
        .id(struttura.getId())
        .nome(struttura.getNome())
        .indirizzo(struttura.getIndirizzo())
        .citta(struttura.getCitta())
        .provincia(struttura.getProvincia())
        .cap(struttura.getCap())
        .telefono(struttura.getTelefono())
        .email(struttura.getEmail())
        .descrizione(struttura.getDescrizione())
        .stelle(struttura.getStelle())
        .attiva(struttura.getAttiva())
        .build();
    }
}
