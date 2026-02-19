package com.hotel.app.mapper;

import com.hotel.app.dto.response.ServizioAggiuntivoResponse;
import com.hotel.app.entity.ServizioAggiuntivo;

public class ServizioAggiuntivoMapper {
    
    public static ServizioAggiuntivoResponse toResponse(ServizioAggiuntivo servizio){
        return ServizioAggiuntivoResponse.builder()
                .id(servizio.getId())
                .tipo(servizio.getTipo())
                .nome(servizio.getNome())
                .descrizione(servizio.getDescrizione())
                .prezzo(servizio.getPrezzo())
                .perGiorno(servizio.getPerGiorno())
                .strutturaId(servizio.getStruttura().getId())
                .strutturaNome(servizio.getStruttura().getNome())
                .attivo(servizio.getAttivo())
                .build();
    }
}