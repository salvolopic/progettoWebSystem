package com.hotel.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.app.dto.request.RegistrazioneRequest;
import com.hotel.app.dto.response.UtenteResponse;
import com.hotel.app.entity.Struttura;
import com.hotel.app.entity.Utente;
import com.hotel.app.mapper.UtenteMapper;
import com.hotel.app.repository.StrutturaRepository;
import com.hotel.app.repository.UtenteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
public class AuthService {

    private final UtenteRepository utenteRepository;
    private final StrutturaRepository strutturaRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UtenteRepository utenteRepository,
                       StrutturaRepository strutturaRepository,
                       PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.strutturaRepository = strutturaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UtenteResponse registraUtente(RegistrazioneRequest request) {
        // Converti request a entity
        Utente nuovoUtente = UtenteMapper.fromRegistrazioneRequest(request);

        // Encode password
        nuovoUtente.setPassword(passwordEncoder.encode(request.getPassword()));

        // Gestisci struttura assegnata se presente
        if (request.getStrutturaAssegnataId() != null) {
            Struttura struttura = strutturaRepository.findById(request.getStrutturaAssegnataId())
                    .orElseThrow(() -> new IllegalArgumentException("Struttura non trovata"));
            nuovoUtente.setStrutturaAssegnata(struttura);
        }
        // Salva utente
        Utente utenteRegistrato = utenteRepository.save(nuovoUtente);

        // Converti entity a response e ritorna
        return UtenteMapper.toResponse(utenteRegistrato);
    }

    //login
    public UtenteResponse getUtenteByUsername(String username) {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        return UtenteMapper.toResponse(utente);
    }

}