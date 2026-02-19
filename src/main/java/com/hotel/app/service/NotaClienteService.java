package com.hotel.app.service;

import com.hotel.app.entity.enums.StatoPrenotazione;
import java.time.LocalDateTime;
import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.app.entity.NotaCliente;
import com.hotel.app.entity.Prenotazione;
import com.hotel.app.entity.Utente;
import com.hotel.app.repository.NotaClienteRepository;
import com.hotel.app.repository.PrenotazioneRepository;
import com.hotel.app.repository.UtenteRepository;


@Service
public class NotaClienteService {

    private final NotaClienteRepository notaClienteRepository;
    private final PrenotazioneRepository prenotazioneRepository;
    private final UtenteRepository utenteRepository;

    public NotaClienteService(NotaClienteRepository notaClienteRepository,
                              PrenotazioneRepository prenotazioneRepository,
                              UtenteRepository utenteRepository) {
        this.notaClienteRepository = notaClienteRepository;
        this.prenotazioneRepository = prenotazioneRepository;
        this.utenteRepository = utenteRepository;
    }

    @Transactional
    public NotaCliente aggiungiNota(Long prenotazioneId, String testo) {
        Prenotazione prenotazione = prenotazioneRepository.findById(prenotazioneId)
                .orElseThrow(() -> new IllegalArgumentException("Prenotazione non trovata"));

        if (prenotazione.getStato() != StatoPrenotazione.CHECK_IN_EFFETTUATO){
            throw new IllegalStateException("Le note possono essere inserite solo durante il soggiorno");
        }

        NotaCliente nota = new NotaCliente();
        nota.setPrenotazione(prenotazione);
        nota.setTesto(testo);
        nota.setDataCreazione(LocalDateTime.now());
        nota.setLettaDalPersonale(false);

        return notaClienteRepository.save(nota);
    }

    @Transactional(readOnly = true)
    public List<NotaCliente> findByPrenotazioneId(Long prenotazioneId) {
        return notaClienteRepository.findByPrenotazione_Id(prenotazioneId);
    }

    @Transactional(readOnly = true)
    public List<NotaCliente> findNonLette() {
        return notaClienteRepository.findByLettaDalPersonaleFalse();
    }

    @Transactional
    public NotaCliente segnaComeLetta(Long notaId, Long utenteId) {
        NotaCliente nota = notaClienteRepository.findById(notaId)
                .orElseThrow(() -> new IllegalArgumentException("Nota non trovata"));

        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        nota.setLettaDalPersonale(true);
        nota.setLettaDa(utente);
        nota.setDataLettura(LocalDateTime.now());

        return notaClienteRepository.save(nota);
    }
}
