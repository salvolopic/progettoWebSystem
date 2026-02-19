package com.hotel.app.mapper;

import com.hotel.app.dto.request.RegistrazioneRequest;
import com.hotel.app.dto.response.UtenteResponse;
import com.hotel.app.entity.Utente;

public class UtenteMapper {

    public static UtenteResponse toResponse(Utente utente) {
        if (utente == null) {
            return null;
        }

        UtenteResponse.UtenteResponseBuilder builder = UtenteResponse.builder()
                .id(utente.getId())
                .username(utente.getUsername())
                .nome(utente.getNome())
                .cognome(utente.getCognome())
                .email(utente.getEmail())
                .ruolo(utente.getRuolo())
                .attivo(utente.getAttivo())
                .dataRegistrazione(utente.getDataRegistrazione());

        if (utente.getStrutturaAssegnata() != null) {
            builder.strutturaAssegnataId(utente.getStrutturaAssegnata().getId());
            builder.strutturaAssegnataNome(utente.getStrutturaAssegnata().getNome());
        }

        return builder.build();
    }

    public static Utente fromRegistrazioneRequest(RegistrazioneRequest request) {
        if (request == null) {
            return null;
        }

        return Utente.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .nome(request.getNome())
                .cognome(request.getCognome())
                .email(request.getEmail())
                .ruolo(request.getRuolo())
                .attivo(true)
                .build();
    }
}
