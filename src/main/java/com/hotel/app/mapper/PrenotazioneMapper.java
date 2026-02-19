package com.hotel.app.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.hotel.app.dto.request.PrenotazioneRequest;
import com.hotel.app.dto.response.PrenotazioneResponse;
import com.hotel.app.entity.Prenotazione;

public class PrenotazioneMapper {

    public static PrenotazioneResponse toResponse(Prenotazione prenotazione) {
        if (prenotazione == null) {
            return null;
        }

        PrenotazioneResponse.PrenotazioneResponseBuilder builder = PrenotazioneResponse.builder()
                .id(prenotazione.getId())
                .codicePrenotazione(prenotazione.getCodicePrenotazione())
                .dataCheckIn(prenotazione.getDataCheckIn())
                .dataCheckOut(prenotazione.getDataCheckOut())
                .dataCheckInEffettivo(prenotazione.getDataCheckInEffettivo())
                .dataCheckOutEffettivo(prenotazione.getDataCheckOutEffettivo())
                .stato(prenotazione.getStato())
                .numeroOspiti(prenotazione.getNumeroOspiti())
                .costoCamera(prenotazione.getCostoCamera())
                .costoServizi(prenotazione.getCostoServizi())
                .costoTotale(prenotazione.getCostoTotale())
                .dataCreazione(prenotazione.getDataCreazione());

        // Dati cliente
        if (prenotazione.getCliente() != null) {
            builder.clienteId(prenotazione.getCliente().getId());
            builder.clienteNome(prenotazione.getCliente().getNome() + " " + prenotazione.getCliente().getCognome());
        }

        // Dati camera e struttura
        if (prenotazione.getCamera() != null) {
            builder.cameraId(prenotazione.getCamera().getId());
            builder.cameraNumero(prenotazione.getCamera().getNumero());

            if (prenotazione.getCamera().getTipoCamera() != null) {
                builder.tipoCameraNome(prenotazione.getCamera().getTipoCamera().getNome());
            }

            if (prenotazione.getCamera().getStruttura() != null) {
                builder.strutturaNome(prenotazione.getCamera().getStruttura().getNome());
            }
        }

        // Lista ospiti
        if (prenotazione.getOspiti() != null && !prenotazione.getOspiti().isEmpty()) {
            List<com.hotel.app.dto.response.OspiteResponse> ospiti = prenotazione.getOspiti().stream()
                    .map(OspiteMapper::toResponse)
                    .collect(Collectors.toList());
            builder.ospiti(ospiti);
        } else {
            builder.ospiti(Collections.emptyList());
        }

        return builder.build();
    }

    public static Prenotazione fromRequest(PrenotazioneRequest request) {
        if (request == null) {
            return null;
        }

        // NON impostiamo cliente e camera qui - li imposteremo nel service
        return Prenotazione.builder()
                .dataCheckIn(request.getDataCheckIn())
                .dataCheckOut(request.getDataCheckOut())
                .numeroOspiti(request.getNumeroOspiti())
                .build();
    }
}
