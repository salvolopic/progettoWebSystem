package com.hotel.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.app.dto.response.StrutturaResponse;
import com.hotel.app.mapper.StrutturaMapper;
import com.hotel.app.service.StrutturaService;

@RestController
@RequestMapping("/api/strutture")
public class StrutturaController {

    private final StrutturaService strutturaService;

    public StrutturaController(StrutturaService strutturaService) {
        this.strutturaService = strutturaService;
    }

    @GetMapping
    public List<StrutturaResponse> getAllStrutture() {
        return strutturaService.findAll().stream()
                .map(StrutturaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StrutturaResponse> getStrutturaById(@PathVariable Long id) {
        return strutturaService.findById(id)
                .map(StrutturaMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
