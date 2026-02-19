package com.hotel.app.controller;

import com.hotel.app.dto.response.NotaClienteResponse;
import com.hotel.app.mapper.NotaClienteMapper;
import com.hotel.app.service.NotaClienteService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.hotel.app.entity.Utente;
import com.hotel.app.entity.Prenotazione;
import com.hotel.app.repository.UtenteRepository;
import com.hotel.app.service.PrenotazioneService;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/notacliente")
public class NotaClienteController {

    private final NotaClienteService notaClienteService;
    private final UtenteRepository utenteRepository;
    private final PrenotazioneService prenotazioneService;

    public NotaClienteController(NotaClienteService notaClienteService,UtenteRepository utenteRepository,PrenotazioneService prenotazioneService) {
        this.notaClienteService = notaClienteService;
        this.utenteRepository = utenteRepository;
        this.prenotazioneService = prenotazioneService;
    }

    private Utente getUtenteAutenticato() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return utenteRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
}


    @PostMapping("/{prenotazioneId}")
    public ResponseEntity<NotaClienteResponse> aggiungiNota(@PathVariable Long prenotazioneId, @RequestBody String nota) {
        Utente utente = getUtenteAutenticato();

        if (utente.getRuolo().name().equals("CLIENTE")) {
            Prenotazione prenotazione = prenotazioneService.findById(prenotazioneId).orElse(null);
            if (prenotazione == null || !prenotazione.getCliente().getId().equals(utente.getId())) {
                return ResponseEntity.status(403).build();
            }
        }

        try {
            NotaClienteResponse nuovaNota = NotaClienteMapper.toResponse(notaClienteService.aggiungiNota(prenotazioneId, nota));
            return ResponseEntity.ok(nuovaNota);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/prenotazione/{prenotazioneId}")
    public ResponseEntity<List<NotaClienteResponse>> getNotaByPrenotazioneId(@PathVariable Long prenotazioneId) {
        Utente utente = getUtenteAutenticato();

        if (utente.getRuolo().name().equals("CLIENTE")) {
            Prenotazione prenotazione = prenotazioneService.findById(prenotazioneId).orElse(null);
            if (prenotazione == null || !prenotazione.getCliente().getId().equals(utente.getId())) {
                return ResponseEntity.status(403).build();
            }
        }

        List<NotaClienteResponse> lista = notaClienteService.findByPrenotazioneId(prenotazioneId).stream()
                .map(NotaClienteMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }


    @GetMapping("/nonlette") 
    public List<NotaClienteResponse> getNoteNonLette() {
        return notaClienteService.findNonLette().stream()   
                .map(NotaClienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PutMapping("/{notaId}/letta")
    public ResponseEntity<NotaClienteResponse> segnaNotaComeLetta(@PathVariable Long notaId) {
        Utente utente = getUtenteAutenticato();

        try {
            NotaClienteResponse nota = NotaClienteMapper.toResponse(notaClienteService.segnaComeLetta(notaId, utente.getId()));
            return ResponseEntity.ok(nota);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
