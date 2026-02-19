package com.hotel.app.mapper;

import com.hotel.app.dto.response.NotaClienteResponse;
import com.hotel.app.entity.NotaCliente;

public class NotaClienteMapper {
    public static NotaClienteResponse toResponse(NotaCliente nota) {
        return NotaClienteResponse.builder()
                .id(nota.getId())
                .prenotazioneId(nota.getPrenotazione().getId())
                .clienteNome(nota.getPrenotazione().getCliente() != null ? nota.getPrenotazione().getCliente().getNome() : null)
                .clienteCognome(nota.getPrenotazione().getCliente() != null ? nota.getPrenotazione().getCliente().getCognome() : null)
                .testo(nota.getTesto())
                .dataCreazione(nota.getDataCreazione())
                .lettaDalPersonale(nota.getLettaDalPersonale())
                .dataLettura(nota.getDataLettura())
                .lettaDaId(nota.getLettaDa() != null ? nota.getLettaDa().getId() : null)
                .lettaDaUsername(nota.getLettaDa() != null ? nota.getLettaDa().getUsername() : null)
                .build();
    }
}