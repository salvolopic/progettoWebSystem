package com.hotel.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hotel.app.entity.Prenotazione;
import com.hotel.app.entity.Utente;
import com.hotel.app.repository.UtenteRepository;


import com.hotel.app.dto.request.PrenotazioneRequest;
import com.hotel.app.dto.response.PrenotazioneResponse;
import com.hotel.app.mapper.PrenotazioneMapper;
import com.hotel.app.service.PrenotazioneService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/prenotazioni")
public class PrenotazioneController {
    private final PrenotazioneService prenotazioneService;
    private final UtenteRepository utenteRepository;


    public PrenotazioneController(PrenotazioneService prenotazioneService,UtenteRepository utenteRepository) {
        this.prenotazioneService = prenotazioneService;
        this.utenteRepository = utenteRepository;
    }

    private Utente getUtenteAutenticato() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return utenteRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
    }

    @GetMapping
    public List<PrenotazioneResponse> getAllPrenotazioni() {
        Utente utente = getUtenteAutenticato();

        if(utente.getRuolo().name().equals("CLIENTE")){
            return prenotazioneService.findByClienteId(utente.getId()).stream()
                    .map(PrenotazioneMapper::toResponse)
                    .collect(Collectors.toList());
        }

        return prenotazioneService.findAll().stream()
                .map(PrenotazioneMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<List<PrenotazioneResponse>> getPrenotazioneById(@PathVariable Long utenteId) {
        Utente utente = getUtenteAutenticato();

        if(utente.getRuolo().name().equals("CLIENTE") && !utente.getId().equals(utenteId)){
            return ResponseEntity.status(403).build();
        }

        List<PrenotazioneResponse> lista = prenotazioneService.findByClienteId(utenteId).stream()
                .map(PrenotazioneMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<PrenotazioneResponse> creaPrenotazione(@Valid @RequestBody PrenotazioneRequest request) {
        Utente utente = getUtenteAutenticato();
        if(utente.getRuolo().name().equals("CLIENTE")){
            request.setClienteId(utente.getId());
        }

        try {
            PrenotazioneResponse nuovaPrenotazione = prenotazioneService.creaPrenotazione(request);
            return ResponseEntity.ok(nuovaPrenotazione);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancellaPrenotazione(@PathVariable Long id) {
        Utente utente = getUtenteAutenticato();

        if (utente.getRuolo().name().equals("CLIENTE")) {
            Prenotazione prenotazione = prenotazioneService.findById(id).orElse(null);
            if (prenotazione == null || !prenotazione.getCliente().getId().equals(utente.getId())) {
                return ResponseEntity.status(403).build();
            }
        }

        try {
            prenotazioneService.cancellaPrenotazione(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
