package com.hotel.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hotel.app.entity.ServizioAggiuntivo;
import com.hotel.app.repository.ServizioAggiuntivoRepository;

@Service
public class ServizioAggiuntivoService {

    private final ServizioAggiuntivoRepository servizioAggiuntivoRepository;

    public ServizioAggiuntivoService(ServizioAggiuntivoRepository servizioAggiuntivoRepository) {
        this.servizioAggiuntivoRepository = servizioAggiuntivoRepository;
    }

    public List<ServizioAggiuntivo> findAll() {
        return servizioAggiuntivoRepository.findAll();
    }

    public Optional<ServizioAggiuntivo> findById(Long id) {
        return servizioAggiuntivoRepository.findById(id);
    }

    public List<ServizioAggiuntivo> findByStrutturaId(Long strutturaId) {
        return servizioAggiuntivoRepository.findByStruttura_IdAndAttivoTrue(strutturaId);
    }
}
