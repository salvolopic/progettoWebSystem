package com.hotel.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.hotel.app.entity.Utente;
import com.hotel.app.repository.UtenteRepository;


import com.hotel.app.dto.OspiteDTO;
import com.hotel.app.dto.response.PrenotazioneResponse;
import com.hotel.app.entity.Prenotazione;
import com.hotel.app.mapper.PrenotazioneMapper;
import com.hotel.app.service.CheckInOutService;

@RestController
@RequestMapping("/api/checkinout")
public class CheckInOutController {

    private final CheckInOutService checkInOutService;
    private final UtenteRepository utenteRepository;


    public CheckInOutController(CheckInOutService checkInOutService,UtenteRepository utenteRepository) {
        this.checkInOutService = checkInOutService;
        this.utenteRepository = utenteRepository;
    
    }

   private Utente getUtenteAutenticato() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return utenteRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
}

    @PostMapping("/checkin/{prenotazioneId}")
public ResponseEntity<?> effettuaCheckIn(@PathVariable Long prenotazioneId, @RequestBody List<OspiteDTO> ospiti) {
    Utente utente = getUtenteAutenticato();

    if (utente.getRuolo().name().equals("CLIENTE")) {
        Prenotazione verifica = checkInOutService.trovaPrenotazione(prenotazioneId);
        if (verifica == null || !verifica.getCliente().getId().equals(utente.getId())) {
            return ResponseEntity.status(403).body("Non autorizzato");
        }
    }

    try {
        Prenotazione prenotazione = checkInOutService.effettuaCheckIn(prenotazioneId, ospiti);
        PrenotazioneResponse response = PrenotazioneMapper.toResponse(prenotazione);
        return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

    @PostMapping("/checkout/{prenotazioneId}")
    public ResponseEntity<?> effettuaCheckOut(@PathVariable Long prenotazioneId) {
        Utente utente = getUtenteAutenticato();

        if (utente.getRuolo().name().equals("CLIENTE")) {
            Prenotazione verifica = checkInOutService.trovaPrenotazione(prenotazioneId);
            if (verifica == null || !verifica.getCliente().getId().equals(utente.getId())) {
                return ResponseEntity.status(403).body("Non autorizzato");
            }
        }

        try {
            Prenotazione prenotazione = checkInOutService.effettuaCheckOut(prenotazioneId);
            PrenotazioneResponse response = PrenotazioneMapper.toResponse(prenotazione);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
