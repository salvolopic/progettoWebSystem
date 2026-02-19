package com.hotel.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotel.app.dto.response.RegistroPulizieResponse;
import com.hotel.app.mapper.RegistroPulizieMapper;
import com.hotel.app.service.PulizieService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.hotel.app.entity.Utente;
import com.hotel.app.repository.UtenteRepository;


@RestController
@RequestMapping("/api/pulizie")
public class PulizieController {

    private final PulizieService pulizieService;
    private final UtenteRepository utenteRepository;


    public PulizieController(PulizieService pulizieService,UtenteRepository utenteRepository) {
        this.pulizieService = pulizieService;
        this.utenteRepository = utenteRepository;
    }

    private Utente getUtenteAutenticato() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return utenteRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
    }

    @PostMapping
    public ResponseEntity<RegistroPulizieResponse> registraPulizia(@RequestParam Long cameraId, @RequestParam(required = false) String note) {
        Utente utente = getUtenteAutenticato();

        try {
            RegistroPulizieResponse registro = RegistroPulizieMapper.toResponse(pulizieService.registraPulizia(cameraId, utente.getId(), note));
            return ResponseEntity.ok(registro);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/camera/{cameraId}")
    public List<RegistroPulizieResponse> getByCameraId(@PathVariable Long cameraId) {
        return pulizieService.findByCameraId(cameraId).stream()
                .map(RegistroPulizieMapper::toResponse)
                .collect(Collectors.toList());
    
    }
}
