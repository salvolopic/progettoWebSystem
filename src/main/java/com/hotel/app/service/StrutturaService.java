package com.hotel.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hotel.app.entity.Struttura;
import com.hotel.app.repository.StrutturaRepository;

@Service
public class StrutturaService {

    private final StrutturaRepository strutturaRepository;

    public StrutturaService(StrutturaRepository strutturaRepository) {
        this.strutturaRepository = strutturaRepository;
    }

    public List<Struttura> findAll() {
        return strutturaRepository.findAll();
    }

    public Optional<Struttura> findById(Long id) {
        return strutturaRepository.findById(id);
    }
}
